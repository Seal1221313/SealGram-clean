# SealGram

SealGram is an unofficial Android client fork based on Telegram for Android. It is not affiliated with Telegram.

## Project status

This is a public source-code repository. A successful build is **not** guaranteed until the GitHub Actions workflow completes successfully on the current commit. The generated APK is a test artifact, not an endorsed or production release.

## Build an APK in GitHub Actions

1. Open the [SealGram APK workflow](https://github.com/Seal1221313/SealGram-clean/actions/workflows/android.yml).
2. Choose **Run workflow** on the `main` branch, or push a commit to `main` / `sealgram-fork`.
3. Open the completed run and download the `SealGram-apk` artifact. It contains `SealGram.apk` and its SHA-256 checksum.

The workflow generates a **temporary signing key for each run** and does not save it in a GitHub Actions cache or publish a rolling GitHub Release. This avoids exposing a private signing key in this public repository. Because the key changes each run, an APK from a later run generally cannot update an APK from an earlier run in place; uninstalling the earlier test build may be necessary.

For repeatable app updates and production distribution, the repository owner must configure a persistent signing keystore and its passwords as protected GitHub Actions secrets, then update the workflow to use those secrets. Do not commit a production keystore or store its passwords in cache.

## Before you build

- Use your own Telegram API ID and API hash. Do not publish personal credentials or reuse credentials belonging to Telegram's official app.
- `TMessagesProj/config/release.keystore` in the source tree is a **dummy development/reproducible-build key**, not a production signing key. Never use it to sign a public release.
- Do not commit `local.properties`, `.env` files, signing keys, access tokens, Firebase configuration files, or private API credentials.
- The workflow optionally reads `TG_API_ID` and `TG_API_HASH` from repository Actions secrets. Without them, the build may compile but may not be able to log in to Telegram.

## Build environment

Use Android Studio with Android SDK 36, Android Build Tools 36.0.0, and Android NDK 27.2.12479018. Open the repository as a Gradle project. Keep machine-specific settings in ignored local files and configure release signing with a keystore you control.

## Privacy and network behavior

SealGram is based on Telegram's Android client and retains its normal network behavior, including connections to Telegram services and any enabled integrations. Firebase / Google Play Services libraries are still present for features such as push notifications, maps, location, and related integrations. Removing Firebase Analytics does not mean every Google service or network request has been removed. Do not claim that the app is fully offline or has no third-party network traffic without auditing the exact build and its dependencies.

## Licensing

This project contains upstream Telegram for Android code. See `LICENSE` and upstream copyright notices for licensing terms. Publish corresponding source changes as required by the applicable licenses.

## Security reports

Do not post secrets or personal data in public issues. Revoke and rotate any credential that may have been exposed, even if a file or commit was later removed.

<!-- Trigger a public GitHub Actions build for the current source tree. -->
