package com.swaglabs.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Converts a recorded AVI to MP4 with ffmpeg, because browsers cannot play the screen-capture
 * codec and the Allure report could then only offer a download. If ffmpeg is missing or the
 * conversion fails, the original AVI is returned, so nothing is lost.
 */
public final class VideoConverter {

    private static final Logger LOG = LogManager.getLogger(VideoConverter.class);
    private static final long CONVERSION_TIMEOUT_SECONDS = 120;

    private static Boolean ffmpegAvailable;

    private VideoConverter() {
    }

    /** Returns the MP4 next to {@code source}, or {@code source} itself when it cannot be converted. */
    public static File toMp4(File source) {
        if (source == null || !source.exists() || !hasFfmpeg()) {
            return source;
        }
        String name = source.getName();
        int dot = name.lastIndexOf('.');
        File target = new File(source.getParentFile(), (dot < 0 ? name : name.substring(0, dot)) + ".mp4");
        File log = new File(source.getParentFile(), "ffmpeg.log");
        try {
            Process process = new ProcessBuilder(
                    "ffmpeg", "-y", "-loglevel", "error",
                    "-i", source.getAbsolutePath(),
                    // yuv420p needs even dimensions
                    "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2",
                    "-c:v", "libx264", "-preset", "veryfast", "-crf", "28",
                    "-pix_fmt", "yuv420p", "-movflags", "+faststart",
                    target.getAbsolutePath())
                    .redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(log))
                    .start();
            if (!process.waitFor(CONVERSION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                LOG.warn("ffmpeg timed out converting {}, keeping the AVI", name);
                target.delete();
                return source;
            }
            if (process.exitValue() == 0 && target.length() > 0) {
                VideoRecorder.discard(source);
                return target;
            }
            LOG.warn("ffmpeg could not convert {} (see {}), keeping the AVI", name, log.getName());
        } catch (IOException e) {
            LOG.warn("ffmpeg could not be started for {}: {}", name, e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        target.delete();
        return source;
    }

    private static synchronized boolean hasFfmpeg() {
        if (ffmpegAvailable == null) {
            try {
                Process process = new ProcessBuilder("ffmpeg", "-version")
                        .redirectErrorStream(true)
                        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                        .start();
                ffmpegAvailable = process.waitFor(10, TimeUnit.SECONDS) && process.exitValue() == 0;
            } catch (IOException e) {
                ffmpegAvailable = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            if (!ffmpegAvailable) {
                LOG.info("ffmpeg not found on the PATH, failed-test videos stay in AVI format");
            }
        }
        return ffmpegAvailable;
    }
}
