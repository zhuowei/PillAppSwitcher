A prototype for a new Android Recent Apps switcher.

Currently can obtain Recent Apps screenshots when tethered to a computer.

## Running

Build and install app to Android 8.1 device from Android Studio. Instant Run should be disabled.

In adb, run

```
CLASSPATH="$(pm path net.zhuoweizhang.pill|sed -e s/^package//)" app_process /sdcard net.zhuoweizhang.pill.PillServer
```

which serves all recent app images over localhost HTTP. (This is obviously insecure.)

## License

Apache v2.
