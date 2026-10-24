# DexGuard integration prerequisites (SealGram)

DexGuard is a commercial product. This repository does not currently contain a DexGuard plugin declaration, the licensed Maven repository configuration, a DexGuard license, or an approved version/configuration supplied by Guardsquare. Do not guess plugin coordinates, add an unlicensed dependency, or commit license credentials. For that reason, the release build continues to use the existing R8/ProGuard configuration until the project owner supplies the official integration details.

## Required from the project owner

1. A valid DexGuard license and the current official Gradle integration instructions for the licensed version.
2. The exact plugin version and Maven repository endpoint supplied by Guardsquare.
3. Required repository credentials/license material stored only as GitHub Actions Secrets (never committed).
4. A vendor-supported dexguard-project.txt configuration matching that exact version.
5. A test device matrix and a release validation plan for Telegram login, notifications, media, native libraries, Firebase and any reflection/JNI code.

After those are available, integrate the official plugin only into the intended release variant, preserve R8 for variants not using DexGuard, and validate the final signed APK. Avoid blanket rules that rename/remove Telegram classes used via reflection, JNI, serialization or generated code.

## Current repository safeguards

- Release and standalone variants currently have minifyEnabled true and shrinkResources true with the existing ProGuard rules.
- .github/workflows/android.yml passes TG_API_ID and TG_API_HASH through GitHub Actions Secrets as environment variables and injects them during the build. Their values must never be printed in logs.
- The Android app must contain Telegram API ID/hash to use the Telegram client protocol; these client credentials are not equivalent to a server-side secret. Do not place private backend keys or privileged service credentials in the APK. Put privileged operations behind a server-side API.
- String/class obfuscation and anti-tamper measures can raise reverse-engineering costs but cannot make an APK impossible to inspect or guarantee protection against JADX, Bytecode Viewer, runtime instrumentation, rooted devices, or repackaging. Anti-root/anti-debug policies can also block legitimate users and should be tested before enabling.

## Build cache

The workflow uses a disk cleanup action pinned to a commit SHA and keeps the Android SDK installed. It limits ccache to 2 GB after restore, during compilation, and before saving the cache.
