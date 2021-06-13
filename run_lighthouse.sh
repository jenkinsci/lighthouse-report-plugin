set -ex
SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
URL=${1:-https://www.github.com}

# clear it out
> errors
touch errors auth
trap "{ cat errors auth; }" EXIT

# start up chromium inside xvfb
xvfb-run --error-file errors --auth-file auth -l --server-args='-screen 0, 1024x768x16' "$SCRIPTPATH/run_chrome.sh" &
XVFB_PID=$!
trap "{ kill $XVFB_PID; }" EXIT

sleep 3

export XAUTHORITY="$PWD/auth"
export DISPLAY=:99.0

rm -rf recording.mkv
ffmpeg -hide_banner -loglevel error \
    -video_size 1024x768 -framerate 25 -f x11grab -i :99.0 \
   -an -vcodec libx264 -preset ultrafast -qp 0 -pix_fmt yuv444p \
    recording.mkv &

FFMPEG_PID=$!
trap "{ kill $FFMPEG_PID; }" EXIT

# Kick off Lighthouse run on same port as debugging port.
rm -rf reports.json
npx lighthouse --port=9222 $URL --output=json --output-path=reports.json

kill $FFMPEG_PID
kill $XVFB_PID
exit 0


