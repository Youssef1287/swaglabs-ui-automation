package com.swaglabs.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaType;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.KeyFrameIntervalKey;
import static org.monte.media.VideoFormatKeys.QualityKey;

/**
 * Optional screen recording, off unless -Drecord.video=true.
 * <p>
 * It records the whole desktop, so it needs a display (on a headless machine it is skipped;
 * on a Linux CI runner start the build under Xvfb). A recording is started under a temporary
 * name, because the test it belongs to is not known yet when the browser is still being set up,
 * and is renamed in {@link #stop(String)}.
 */
public final class VideoRecorder {

    private static final Logger LOG = LogManager.getLogger(VideoRecorder.class);
    private static final File OUTPUT_DIR = new File("target/videos");
    private static final ThreadLocal<org.monte.screenrecorder.ScreenRecorder> ACTIVE = new ThreadLocal<>();
    private static final AtomicInteger SEQUENCE = new AtomicInteger();
    private static final AtomicBoolean HEADLESS_WARNED = new AtomicBoolean();
    private static final int MAX_NAME_LENGTH = 120;

    private VideoRecorder() {
    }

    /** True while the current thread has a recording running. */
    public static boolean isRecording() {
        return ACTIVE.get() != null;
    }

    /** Starts recording for the current thread. Does nothing if it is already recording. */
    public static void start() {
        if (isRecording()) {
            return;
        }
        if (GraphicsEnvironment.isHeadless()) {
            if (HEADLESS_WARNED.compareAndSet(false, true)) {
                LOG.warn("No display available, skipping video recording "
                        + "(on Linux run the build under xvfb-run and with -Dheadless=false)");
            }
            return;
        }
        try {
            OUTPUT_DIR.mkdirs();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
            String temporaryName = "recording-" + SEQUENCE.incrementAndGet();

            org.monte.screenrecorder.ScreenRecorder recorder = new NamedRecorder(
                    gc,
                    new Rectangle(0, 0, screen.width, screen.height),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f, KeyFrameIntervalKey, 15),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    OUTPUT_DIR,
                    temporaryName);
            recorder.start();
            ACTIVE.set(recorder);
        } catch (Exception e) {
            LOG.warn("Could not start video recording: {}", e.getMessage());
        }
    }

    /**
     * Stops the recording of this thread and gives the video a unique, readable name built from
     * {@code name}. Returns the video file, or null if nothing was recorded.
     */
    public static File stop(String name) {
        org.monte.screenrecorder.ScreenRecorder recorder = ACTIVE.get();
        ACTIVE.remove();
        if (recorder == null) {
            return null;
        }
        try {
            recorder.stop();
        } catch (IOException e) {
            LOG.warn("Could not stop video recording: {}", e.getMessage());
            return null;
        }
        if (recorder.getCreatedMovieFiles().isEmpty()) {
            return null;
        }
        return rename(recorder.getCreatedMovieFiles().get(0), name);
    }

    /** Deletes a video that is not needed (for example one of a passed test). */
    public static void discard(File video) {
        if (video != null && video.exists() && !video.delete()) {
            LOG.warn("Could not delete video {}", video.getName());
        }
    }

    private static File rename(File recorded, String name) {
        String extension = recorded.getName().substring(recorded.getName().lastIndexOf('.'));
        File target = new File(recorded.getParentFile(),
                sanitize(name) + "-" + SEQUENCE.incrementAndGet() + extension);
        try {
            Files.move(recorded.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return target;
        } catch (IOException e) {
            LOG.warn("Could not rename video to {}: {}", target.getName(), e.getMessage());
            return recorded;
        }
    }

    /** Keeps the name safe for every file system and short enough for Windows paths. */
    static String sanitize(String name) {
        String clean = name == null ? "" : name.replaceAll("[^A-Za-z0-9._-]+", "_").replaceAll("^_+|_+$", "");
        if (clean.isEmpty()) {
            clean = "test";
        }
        return clean.length() > MAX_NAME_LENGTH ? clean.substring(0, MAX_NAME_LENGTH) : clean;
    }

    /** Names the movie file itself instead of using a timestamp. */
    private static final class NamedRecorder extends org.monte.screenrecorder.ScreenRecorder {
        private final String fileName;

        NamedRecorder(GraphicsConfiguration cfg, Rectangle area, Format fileFormat, Format screenFormat,
                      Format mouseFormat, File folder, String fileName) throws IOException, AWTException {
            super(cfg, area, fileFormat, screenFormat, mouseFormat, null, folder);
            this.fileName = fileName;
        }

        @Override
        protected File createMovieFile(Format fileFormat) throws IOException {
            if (!movieFolder.exists()) {
                movieFolder.mkdirs();
            } else if (!movieFolder.isDirectory()) {
                throw new IOException("\"" + movieFolder + "\" is not a directory.");
            }
            return new File(movieFolder, fileName + "." + Registry.getInstance().getExtension(fileFormat));
        }
    }
}
