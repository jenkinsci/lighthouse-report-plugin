set -ex

exec google-chrome --user-data-dir=$TMP_PROFILE_DIR \
    --start-maximized \
    --no-first-run \
    --no-sandbox \
    --remote-debugging-port=9222 'about:blank'
