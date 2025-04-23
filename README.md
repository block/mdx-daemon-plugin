# mdx-daemon-plugin

An IDE plugin for Android Studio that integrates with the MDX Daemon, a background task runner designed to improve local Android developer performance.

## 🧠 What It Does

The plugin listens for key Gradle lifecycle events inside Android Studio and notifies the MDX Daemon to either cancel or trigger background work. This ensures the daemon doesn't interfere with critical operations like syncs or builds, and can opportunistically do background work appropriate.

## 🎯 Trigger Points

This plugin hooks into the following IDE events:

- **Gradle Sync Started** → Cancels background builds
- **Gradle Sync Succeeded** → Triggers a background build
- **Gradle Build Started** → Cancels background builds

These actions are coordinated via the daemon's local API.

## 📦 Installation

1. Clone this repo and build the plugin:

   ```bash
   ./gradlew buildPlugin
   ```
2. In Android Studio:

Go to Preferences > Plugins > ⚙️ > Install Plugin from Disk

Choose the ZIP file from build/distributions/mdxdaemon-ide-plugin-<version>.zip

Restart Android Studio

## 🛠 Development
To run a sandbox instance of Android Studio with the plugin preloaded:
```bash
./gradlew runIde
```

Make sure you’ve configured your Android Studio path in gradle.properties:
```properties
square.developerToolkit.studioDir=/Applications/Android Studio.app/Contents
```

## ⚠️ Requirements

1. Android Studio version: 2024.2.1.10 or newer
2. Kotlin Plugin: Compatible with K2 mode
3. JDK: 17 or 21 (depending on the target AS version)

## 🔑 Feature Flags
1. `mdxdaemon.jobId={job-id}`
2. `mdxdaemon.{job-id}.enabled=true`

## License

This plugin is licensed under the [Apache 2.0 License](./LICENSE).
