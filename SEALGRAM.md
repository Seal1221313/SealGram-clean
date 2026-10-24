# SealGram

SealGram is an unofficial, GPLv2-licensed fork of Telegram for Android. It preserves the upstream Telegram Android interface and protocol implementation while adding only client-side, opt-in privacy preferences.

## Required configuration

`TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java` intentionally ships with no usable Telegram API credentials. A maintainer must supply an application-specific `APP_ID` and `APP_HASH` at release build time. Do not use Telegram's official application credentials in an unofficial fork.

## Branding and licensing

The upstream `LICENSE` file and all upstream copyright notices are retained. SealGram is not affiliated with Telegram. Release artwork must use SealGram branding (a seal) rather than Telegram's protected logo.

## Privacy posture

SealGram has no additional backend: it connects only through Telegram's native MTProto implementation. Planned mod settings must remain local. Optional, off by default: "Keep deleted messages" can keep, on this device only, the text, photos and voice messages that someone deleted for everyone, marked with a trash icon. Files, self-destructing media and secret chats are never kept, and nothing is uploaded anywhere.

## Mod features
- Ghost mode (Settings > SealGram): no read receipts, no typing status, no online status. Local preference only.
- Save to Gallery for round videos in the message menu. Protected content and view-once media are excluded.
- The app states that it is an unofficial client and links to https://seal-hub.pages.dev (developer site with all projects).
