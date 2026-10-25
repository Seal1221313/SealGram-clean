package org.telegram.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SealAutoReply;
import org.telegram.messenger.SealStt;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.TranslateController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.SealGram;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Switch;

public class SealGramHubActivity extends BaseFragment {

    public static final String CHANNEL = "https://t.me/+s_LUlXlgeNk5NTc6";
    public static final String UPDATE_CHANNEL = "https://t.me/renewal_SealGram";

    static void setupBar(ActionBar bar, String title, Runnable back) {
        bar.setBackButtonImage(R.drawable.ic_ab_back);
        bar.setAllowOverlayTitle(true);
        bar.setTitle(title);
        bar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    back.run();
                }
            }
        });
    }

    static LinearLayout card(Context c, LinearLayout root) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable g = new GradientDrawable();
        g.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        g.setCornerRadius(AndroidUtilities.dp(20));
        l.setBackground(g);
        l.setClipToOutline(true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(12), AndroidUtilities.dp(12));
        root.addView(l, lp);
        return l;
    }

    static void header(Context c, LinearLayout root, String text) {
        TextView t = new TextView(c);
        t.setText(text);
        t.setTextSize(15);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
        t.setPadding(AndroidUtilities.dp(28), AndroidUtilities.dp(12), AndroidUtilities.dp(16), AndroidUtilities.dp(8));
        root.addView(t);
    }

    static void note(Context c, LinearLayout root, String text) {
        TextView t = new TextView(c);
        t.setText(text);
        t.setTextSize(13);
        t.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        t.setPadding(AndroidUtilities.dp(28), AndroidUtilities.dp(2), AndroidUtilities.dp(24), AndroidUtilities.dp(14));
        root.addView(t);
    }

    static void divider(Context c, LinearLayout card, boolean withIcon) {
        if (card.getChildCount() == 0) return;
        View d = new View(c);
        d.setBackgroundColor(Theme.getColor(Theme.key_divider));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp.leftMargin = AndroidUtilities.dp(withIcon ? 62 : 18);
        card.addView(d, lp);
    }

    static void row(Context c, LinearLayout card, int iconRes, String title, String value, Runnable click) {
        divider(c, card, true);
        LinearLayout r = new LinearLayout(c);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setMinimumHeight(AndroidUtilities.dp(56));
        r.setPadding(AndroidUtilities.dp(18), 0, AndroidUtilities.dp(18), 0);
        r.setBackground(Theme.getSelectorDrawable(false));
        ImageView i = new ImageView(c);
        i.setImageResource(iconRes);
        i.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
        r.addView(i, new LinearLayout.LayoutParams(AndroidUtilities.dp(24), AndroidUtilities.dp(24)));
        TextView t = new TextView(c);
        t.setText(title);
        t.setTextSize(16);
        t.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tp.leftMargin = AndroidUtilities.dp(20);
        r.addView(t, tp);
        if (value != null) {
            TextView v = new TextView(c);
            v.setText(value);
            v.setTextSize(15);
            v.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));
            r.addView(v);
        }
        r.setOnClickListener(v -> click.run());
        card.addView(r);
    }

    static void toggle(Context c, LinearLayout card, String title, String sub, boolean checked, java.util.function.Consumer<Boolean> change) {
        divider(c, card, false);
        LinearLayout r = new LinearLayout(c);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setMinimumHeight(AndroidUtilities.dp(56));
        r.setPadding(AndroidUtilities.dp(18), AndroidUtilities.dp(10), AndroidUtilities.dp(18), AndroidUtilities.dp(10));
        r.setBackground(Theme.getSelectorDrawable(false));
        LinearLayout texts = new LinearLayout(c);
        texts.setOrientation(LinearLayout.VERTICAL);
        TextView t = new TextView(c);
        t.setText(title);
        t.setTextSize(16);
        t.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        texts.addView(t);
        if (sub != null) {
            TextView s = new TextView(c);
            s.setText(sub);
            s.setTextSize(13);
            s.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            texts.addView(s);
        }
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tp.rightMargin = AndroidUtilities.dp(12);
        r.addView(texts, tp);
        Switch sw = new Switch(c);
        sw.setColors(Theme.key_switchTrack, Theme.key_switchTrackChecked, Theme.key_windowBackgroundWhite, Theme.key_windowBackgroundWhite);
        sw.setChecked(checked, false);
        sw.setOnCheckedChangeListener((view, v) -> change.accept(v));
        r.addView(sw, new LinearLayout.LayoutParams(AndroidUtilities.dp(37), AndroidUtilities.dp(20)));
        r.setOnClickListener(v -> sw.setChecked(!sw.isChecked(), true));
        card.addView(r);
    }

    static ScrollView scroll(Context c, LinearLayout root) {
        ScrollView s = new ScrollView(c);
        s.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        s.addView(root);
        return s;
    }

    @Override
    public View createView(Context context) {
        setupBar(actionBar, "SealGram", () -> finishFragment());
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, 0, 0, AndroidUtilities.dp(24));

        ImageView logo = new ImageView(context);
        logo.setImageResource(R.drawable.seal_logo);
        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        logo.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(View view, android.graphics.Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        logo.setClipToOutline(true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AndroidUtilities.dp(112), AndroidUtilities.dp(112));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = AndroidUtilities.dp(12);
        root.addView(logo, lp);

        TextView title = new TextView(context);
        title.setText("SealGram");
        title.setTextSize(28);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        title.setPadding(0, AndroidUtilities.dp(14), 0, 0);
        root.addView(title);

        TextView ver = new TextView(context);
        ver.setText(SealGramI18n.t("HubVersionPrefix") + BuildVars.BUILD_VERSION_STRING);
        ver.setTextSize(15);
        ver.setGravity(Gravity.CENTER);
        ver.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        ver.setPadding(0, AndroidUtilities.dp(2), 0, AndroidUtilities.dp(14));
        root.addView(ver);

        header(context, root, SealGramI18n.t("HubHeaderCategories"));
        LinearLayout cats = card(context, root);
        row(context, cats, R.drawable.msg_settings, SealGramI18n.t("HubGeneral"), null, () -> presentFragment(new Page(0)));
        row(context, cats, R.drawable.msg_palette, SealGramI18n.t("HubAppearance"), null, () -> presentFragment(new Page(1)));
        row(context, cats, R.drawable.msg_text_outlined, SealGramI18n.t("HubChats"), null, () -> presentFragment(new Page(2)));
        row(context, cats, R.drawable.msg_translate, SealGramI18n.t("HubChatTranslation"), null, () -> presentFragment(new Page(6)));
        row(context, cats, R.drawable.msg_voice_unmuted, SealGramI18n.t("HubVoiceToText"), null, () -> presentFragment(new Page(4)));
        row(context, cats, R.drawable.msg_edit, SealGramI18n.t("HubAutoReply"), null, () -> presentFragment(new Page(5)));
        row(context, cats, R.drawable.msg_secret, SealGramI18n.t("HubGhostPrivacy"), null, () -> presentFragment(new SealGramSettingsActivity()));
        row(context, cats, R.drawable.msg_voicechat, SealGramI18n.t("VoiceTitle"), null, () -> presentFragment(new SealGramVoiceActivity()));
        row(context, cats, R.drawable.msg_palette, SealGramI18n.t("StyleTitle"), null, () -> presentFragment(new SealGramStyleActivity()));
        row(context, cats, R.drawable.msg_delete, SealGramI18n.t("KeepDeleted"), null, () -> presentFragment(new SealGramKeepActivity()));
        row(context, cats, R.drawable.msg_archive, SealGramI18n.t("LogTitle"), null, () -> presentFragment(new SealGramLogActivity()));
        row(context, cats, R.drawable.msg_info, SealGramI18n.t("HubOther"), null, () -> presentFragment(new Page(3)));

        header(context, root, SealGramI18n.t("HubLinks"));
        LinearLayout links = card(context, root);
        row(context, links, R.drawable.msg_channel, SealGramI18n.t("HubChannel"), "SealGram", () -> Browser.openUrl(getParentActivity(), CHANNEL));
        row(context, links, R.drawable.msg_link, SealGramI18n.t("HubWebsite"), "seal-hub.pages.dev", () -> Browser.openUrl(getParentActivity(), SealGram.SITE));

        header(context, root, "GitHub и исходный код");
        LinearLayout sourceCode = card(context, root);
        row(context, sourceCode, R.drawable.msg_link, "Репозиторий SealGram", "GitHub", () -> Browser.openUrl(getParentActivity(), "https://github.com/Seal1221313/SealGram-clean"));
        row(context, sourceCode, R.drawable.msg_link, "Где находится код", "Исходники", () -> Browser.openUrl(getParentActivity(), "https://github.com/Seal1221313/SealGram-clean/tree/main/TMessagesProj/src/main/java/org/telegram"));
        note(context, root, "Открытый репозиторий содержит исходный код мода и историю изменений. Перед сборкой можно просмотреть файлы и коммиты.");

        header(context, root, SealGramI18n.t("HubTelegramServices"));
        LinearLayout services = card(context, root);
        row(context, services, R.drawable.settings_premium, SealGramI18n.t("TelegramPremium"), SealGramI18n.t("HubOpen"), () -> presentFragment(new PremiumPreviewFragment("settings")));
        row(context, services, R.drawable.settings_business, SealGramI18n.t("TelegramBusiness"), SealGramI18n.t("HubOpen"), () -> presentFragment(new PremiumPreviewFragment(PremiumPreviewFragment.FEATURES_BUSINESS, "settings")));

        header(context, root, SealGramI18n.t("HubUpdates"));
        LinearLayout updates = card(context, root);
        row(context, updates, R.drawable.msg_channel, SealGramI18n.t("HubUpdateChannel"), SealGramI18n.t("HubOpen"), () -> Browser.openUrl(getParentActivity(), UPDATE_CHANNEL));
        note(context, root, SealGramI18n.t("HubUpdateChannelDesc"));

        fragmentView = scroll(context, root);
        return fragmentView;
    }

    public static class Page extends BaseFragment {
        private final int page;
        private Context ctx;
        private LinearLayout root;
        private TextView status;
        private ProgressBar bar;

        public Page(int page) {
            this.page = page;
        }

        @Override
        public View createView(Context context) {
            ctx = context;
            String[] names = {SealGramI18n.t("HubGeneral"), SealGramI18n.t("HubAppearance"), SealGramI18n.t("HubChats"), SealGramI18n.t("HubOther"), SealGramI18n.t("HubVoiceToText"), SealGramI18n.t("HubAutoReply"), SealGramI18n.t("HubChatTranslation")};
            setupBar(actionBar, names[page], () -> finishFragment());
            root = new LinearLayout(context);
            root.setOrientation(LinearLayout.VERTICAL);
            root.setPadding(0, AndroidUtilities.dp(12), 0, AndroidUtilities.dp(24));
            fill();
            fragmentView = scroll(context, root);
            return fragmentView;
        }

        private void fill() {
            root.removeAllViews();
            LinearLayout c = card(ctx, root);
            String noteText = SealGramI18n.t("HubChangesRestart");
            if (page == 0) {
                toggle(ctx, c, SealGramI18n.t("HubBlockAds"), SealGramI18n.t("HubBlockAdsDesc"), SealGram.pref("block_ads", true), v -> SealGram.setPref("block_ads", v));
                toggle(ctx, c, SealGramI18n.t("HubHidePhone"), SealGramI18n.t("HubHidePhoneDesc"), SealGram.hideOwnPhoneInProfile(), SealGram::setHideOwnPhoneInProfile);
                toggle(ctx, c, SealGramI18n.t("HubProfileInfo"), SealGramI18n.t("HubProfileInfoDesc"), SealGram.pref("show_id", true), v -> SealGram.setPref("show_id", v));
                toggle(ctx, c, SealGramI18n.t("HubNoRound"), SealGramI18n.t("HubNoRoundDesc"), SealGram.pref("no_round", false), v -> SealGram.setPref("no_round", v));
                toggle(ctx, c, SealGramI18n.t("HubNoUnarchive"), SealGramI18n.t("HubNoUnarchiveDesc"), SealGram.pref("no_unarchive_swipe", false), v -> SealGram.setPref("no_unarchive_swipe", v));
                toggle(ctx, c, SealGramI18n.t("HubPerf"), SealGramI18n.t("HubPerfDesc"), SealGram.pref("perf_mode", false), v -> {
                    SealGram.setPref("perf_mode", v);
                    org.telegram.messenger.LiteMode.setAllFlags(v ? org.telegram.messenger.LiteMode.PRESET_LOW : org.telegram.messenger.LiteMode.PRESET_MEDIUM);
                });
                toggle(ctx, c, SealGramI18n.t("HubLazyArchive"), SealGramI18n.t("HubLazyArchiveDesc"), SealGram.pref("lazy_archive", false), v -> SealGram.setPref("lazy_archive", v));
                toggle(ctx, c, SealGramI18n.t("HubHideStories"), SealGramI18n.t("HubHideStoriesDesc"), SealGram.pref("hide_stories", false), v -> SealGram.setPref("hide_stories", v));
                toggle(ctx, c, SealGramI18n.t("HubHidePremium"), SealGramI18n.t("HubHidePremiumDesc"), SealGram.pref("hide_premium", false), v -> SealGram.setPref("hide_premium", v));
                toggle(ctx, c, SealGramI18n.t("HubConfirmCall"), SealGramI18n.t("HubConfirmCallDesc"), SealGram.pref("confirm_call", false), v -> SealGram.setPref("confirm_call", v));
            } else if (page == 1) {
                toggle(ctx, c, SealGramI18n.t("HubNoDividers"), SealGramI18n.t("HubNoDividersDesc"), SealGram.pref("no_dividers", false), v -> SealGram.setPref("no_dividers", v));
            } else if (page == 2) {
                toggle(ctx, c, SealGramI18n.t("HubHideShare"), SealGramI18n.t("HubHideShareDesc"), SealGram.pref("hide_share", false), v -> SealGram.setPref("hide_share", v));
                toggle(ctx, c, SealGramI18n.t("HubHideKeyboardScroll"), null, SealGram.pref("hide_kb_scroll", false), v -> SealGram.setPref("hide_kb_scroll", v));
                toggle(ctx, c, SealGramI18n.t("HubNoNextChannel"), SealGramI18n.t("HubNoNextChannelDesc"), SealGram.pref("no_jump", false), v -> SealGram.setPref("no_jump", v));
                toggle(ctx, c, SealGramI18n.t("HubCommaAfterMention"), SealGramI18n.t("HubCommaAfterMentionDesc"), SealGram.pref("comma_mention", false), v -> SealGram.setPref("comma_mention", v));
                toggle(ctx, c, SealGramI18n.t("HubTimeSeconds"), SealGramI18n.t("HubTimeSecondsDesc"), SealGram.pref("time_seconds", false), v -> SealGram.setPref("time_seconds", v));
            } else if (page == 4) {
                noteText = SealGramI18n.t("HubVoiceToTextNote");
                fillStt(c);
            } else if (page == 5) {
                noteText = SealGramI18n.t("HubAutoReplyNote");
                fillAuto(c);
            } else if (page == 6) {
                noteText = SealGramI18n.t("HubTranslationNote");
                fillTranslation(c);
            } else {
                row(ctx, c, R.drawable.msg_voicechat, SealGramI18n.t("HubCircularVideos"), SealGramI18n.t("HubHoldToSave"), () -> {});
                noteText = SealGramI18n.t("HubDisclaimer");
            }
            note(ctx, root, noteText);
        }

        private void fillStt(LinearLayout c) {
            toggle(ctx, c, SealGramI18n.t("HubOneTapText"), SealGramI18n.t("HubOneTapTextDesc"), SealGram.pref("stt_inline_button", true), v -> SealGram.setPref("stt_inline_button", v));
            toggle(ctx, c, SealGramI18n.t("HubVoiceMenu"), SealGramI18n.t("HubVoiceMenuDesc"), SealGram.pref("stt_enabled", true), v -> SealGram.setPref("stt_enabled", v));
            header(ctx, root, SealGramI18n.t("HubRecognitionModel"));
            LinearLayout sc = card(ctx, root);
            sc.setPadding(AndroidUtilities.dp(18), AndroidUtilities.dp(10), AndroidUtilities.dp(18), AndroidUtilities.dp(10));
            status = new TextView(ctx);
            status.setTextSize(14);
            status.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            status.setText(SealStt.isBusy()
                    ? SealGramI18n.t("HubModelLoading")
                    : SealStt.installed(SealStt.lang())
                        ? SealGramI18n.format("HubModelReady", SealStt.langName(SealStt.lang()))
                        : SealGramI18n.t("HubChooseModel"));
            sc.addView(status);
            bar = new ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal);
            bar.setMax(100);
            bar.setVisibility(SealStt.isBusy() ? View.VISIBLE : View.GONE);
            sc.addView(bar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(8)));
            LinearLayout langs = card(ctx, root);
            final String cur = SealStt.lang();
            for (String code : SealStt.codes()) {
                String st = SealStt.installed(code) ? (code.equals(cur) ? SealGramI18n.t("HubSelected") : SealGramI18n.t("HubInstalled")) : SealStt.sizeMb(code) + " " + SealGramI18n.t("UnitMb");
                row(ctx, langs, R.drawable.msg_voice_unmuted, SealStt.langName(code), st, () -> onModelClick(code));
            }
        }

        private void fillTranslation(LinearLayout c) {
            TranslateController controller = MessagesController.getInstance(currentAccount).getTranslateController();
            toggle(ctx, c, SealGramI18n.t("HubRealTimeTranslation"), SealGramI18n.t("HubRealTimeTranslationDesc"),
                    controller.isChatTranslateEnabled(), enabled -> {
                        if (enabled && !UserConfig.getInstance(currentAccount).isPremium()) {
                            controller.setChatTranslateEnabled(false);
                            AndroidUtilities.runOnUIThread(this::fill);
                            new AlertDialog.Builder(ctx)
                                    .setTitle(SealGramI18n.t("HubPremiumRequiredTitle"))
                                    .setMessage(SealGramI18n.t("HubPremiumRequiredMessage"))
                                    .setPositiveButton(SealGramI18n.t("TelegramPremium"), (dialog, which) -> presentFragment(new PremiumPreviewFragment("settings")))
                                    .setNegativeButton(SealGramI18n.t("CommonCancel"), null)
                                    .show();
                            return;
                        }
                        controller.setChatTranslateEnabled(enabled);
                        NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 0);
                    });
            toggle(ctx, c, SealGramI18n.t("HubTranslateIndividual"), SealGramI18n.t("HubTranslateIndividualDesc"),
                    controller.isContextTranslateEnabled(), enabled -> {
                        controller.setContextTranslateEnabled(enabled);
                        NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 0);
                    });
            row(ctx, c, R.drawable.settings_premium, SealGramI18n.t("TelegramPremium"), SealGramI18n.t("HubOpen"),
                    () -> presentFragment(new PremiumPreviewFragment("settings")));
            row(ctx, c, R.drawable.settings_business, SealGramI18n.t("TelegramBusiness"), SealGramI18n.t("HubOpen"),
                    () -> presentFragment(new PremiumPreviewFragment(PremiumPreviewFragment.FEATURES_BUSINESS, "settings")));
        }

        private void onModelClick(String code) {
            if (SealStt.installed(code)) {
                SealStt.setLang(code);
                fill();
                return;
            }
            if (SealStt.isBusy()) {
                Toast.makeText(ctx, SealGramI18n.t("SttAnotherModelLoading"), Toast.LENGTH_SHORT).show();
                return;
            }
            if (bar != null) {
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(0);
            }
            SealStt.download(code, (text, pct) -> {
                if (isFinished || isFinishing()) return;
                if (status != null) status.setText(text);
                if (bar != null && pct >= 0) bar.setProgress(pct);
                if (pct < 0 && (text.startsWith("Помилка") || text.startsWith("Немає"))) {
                    new AlertDialog.Builder(ctx).setTitle(SealGramI18n.t("HubModelDownloadFailed")).setMessage(text).setPositiveButton(SealGramI18n.t("CommonOK"), null).show();
                }
            }, () -> {
                if (isFinished || isFinishing()) return;
                if (SealStt.installed(code)) SealStt.setLang(code);
                fill();
            });
        }

        private String userName(long id) {
            TLRPC.User u = MessagesController.getInstance(currentAccount).getUser(id);
            return u != null ? UserObject.getUserName(u) : "ID " + id;
        }

        private void fillAuto(LinearLayout c) {
            toggle(ctx, c, SealGramI18n.t("HubAutoReply"), SealGramI18n.t("HubAutoReplyNote"), SealAutoReply.enabled(), v -> SealAutoReply.setEnabled(v));

            header(ctx, root, SealGramI18n.t("HubMessages"));
            LinearLayout pc = card(ctx, root);
            LinearLayout pv = new LinearLayout(ctx);
            pv.setOrientation(LinearLayout.VERTICAL);
            pv.setPadding(AndroidUtilities.dp(18), AndroidUtilities.dp(14), AndroidUtilities.dp(18), AndroidUtilities.dp(14));
            TextView cap = new TextView(ctx);
            cap.setText(SealGramI18n.t("HubPreviewCaption"));
            cap.setTextSize(12);
            cap.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            pv.addView(cap);
            TextView bubble = new TextView(ctx);
            bubble.setText(SealAutoReply.text());
            bubble.setTextSize(16);
            bubble.setTextColor(Theme.getColor(Theme.key_chat_messageTextOut));
            GradientDrawable bg = new GradientDrawable();
            bg.setColor(Theme.getColor(Theme.key_chat_outBubble));
            bg.setCornerRadius(AndroidUtilities.dp(18));
            bubble.setBackground(bg);
            bubble.setPadding(AndroidUtilities.dp(14), AndroidUtilities.dp(10), AndroidUtilities.dp(14), AndroidUtilities.dp(10));
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bp.gravity = Gravity.END;
            bp.topMargin = AndroidUtilities.dp(10);
            pv.addView(bubble, bp);
            TextView time = new TextView(ctx);
            time.setText(SealGramI18n.t("HubAutoReplyBubble"));
            time.setTextSize(11);
            time.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tp.gravity = Gravity.END;
            tp.topMargin = AndroidUtilities.dp(4);
            pv.addView(time, tp);
            pc.addView(pv);
            row(ctx, pc, R.drawable.msg_edit, SealGramI18n.t("HubChangeText"), null, this::editText);

            header(ctx, root, SealGramI18n.t("HubWhenReply"));
            LinearLayout dc = card(ctx, root);
            row(ctx, dc, R.drawable.msg_info, SealGramI18n.t("HubIfAwayFor"), SealAutoReply.delayName(), this::pickDelay);

            header(ctx, root, SealGramI18n.t("HubWhoNot"));
            LinearLayout ec = card(ctx, root);
            toggle(ctx, ec, SealGramI18n.t("HubMyContacts"), SealGramI18n.t("HubMyContactsDesc"), SealAutoReply.skipContacts(), v -> SealAutoReply.setSkipContacts(v));
            for (long id : SealAutoReply.excluded()) {
                row(ctx, ec, R.drawable.msg_delete, userName(id), SealGramI18n.t("HubRemove"), () -> {
                    SealAutoReply.removeExcluded(id);
                    fill();
                });
            }
            row(ctx, ec, R.drawable.msg_add, SealGramI18n.t("HubAddExclusion"), null, this::pickExcluded);
        }

        private void editText() {
            EditText et = new EditText(ctx);
            et.setText(SealAutoReply.text());
            et.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
            et.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));
            et.setMinLines(3);
            et.setGravity(Gravity.TOP);
            et.setPadding(AndroidUtilities.dp(22), AndroidUtilities.dp(8), AndroidUtilities.dp(22), AndroidUtilities.dp(8));
            new AlertDialog.Builder(ctx)
                    .setTitle(SealGramI18n.t("HubEditAutoReplyText"))
                    .setView(et)
                    .setPositiveButton(SealGramI18n.t("CommonSave"), (d, w) -> {
                        String t = et.getText().toString().trim();
                        if (!t.isEmpty()) {
                            SealAutoReply.setText(t);
                            fill();
                        }
                    })
                    .setNegativeButton(SealGramI18n.t("CommonCancel"), null)
                    .show();
        }

        private void pickDelay() {
            new AlertDialog.Builder(ctx)
                    .setTitle(SealGramI18n.t("HubIfAwayFor"))
                    .setItems(SealGramI18n.autoReplyDelayNames(), (d, which) -> {
                        SealAutoReply.setDelayMinutes(SealAutoReply.DELAYS[which]);
                        fill();
                    })
                    .show();
        }

        private void pickExcluded() {
            Bundle args = new Bundle();
            args.putBoolean("onlyUsers", true);
            args.putBoolean("destroyAfterSelect", true);
            args.putBoolean("returnAsResult", true);
            args.putBoolean("allowSelf", false);
            ContactsActivity f = new ContactsActivity(args);
            f.setDelegate((user, param, activity) -> {
                if (user != null) {
                    SealAutoReply.addExcluded(user.id);
                    fill();
                }
            });
            presentFragment(f);
        }
    }
}
