#!/usr/bin/env bash
# Measures sequential vs parallel run time. Run from the project root: ./compare-runtime.sh
set -e
run() {
  local start=$(date +%s)
  mvn -q clean test -Dheadless=true -Dsuite.file="$1" > /dev/null 2>&1 || true   # failures must not stop the timing
  echo $(( $(date +%s) - start ))
}
echo "Sequential..."; SEQ=$(run testng-sequential.xml)
echo "Parallel (3 threads)..."; PAR=$(run testng.xml)
echo
echo "Sequential: ${SEQ}s"
echo "Parallel:   ${PAR}s"
awk -v s="$SEQ" -v p="$PAR" 'BEGIN{printf "Speed-up:   %.1fx (%.0f%% faster)\n", s/p, (1-p/s)*100}'
