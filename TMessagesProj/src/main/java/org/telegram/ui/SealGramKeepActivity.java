package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SealGram;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.SealKeep;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

public class SealGramKeepActivity extends BaseFragment {

    private static final int[] DAYS = {1, 3, 7, 14, 30};

    private LinearLayout root;
    private Context ctx;

    @Override
    public View createView(Context context) {
        ctx = context;
        SealGramHubActivity.setupBar(actionBar, SealGramI18n.t("KeepDeleted"), this::finishFragment);
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, AndroidUtilities.dp(12), 0, AndroidUtilities.dp(24));
        render();
        fragmentView = SealGramHubActivity.scroll(context, root);
        return fragmentView;
    }

    private static boolean bgEnabled() {
        SharedPreferences p = MessagesController.getGlobalNotificationsSettings();
        if (p.contains("pushService")) return p.getBoolean("pushService", true);
        return MessagesController.getMainSettings(UserConfig.selectedAccount).getBoolean("keepAliveService", false);
    }

    private String colorLabel(int c) {
        return c == 0 ? SealGramI18n.t("KeepColorDefault") : org.telegram.messenger.SealTheme.hex(c);
    }

    private String daysLabel(int d) {
        return String.format(SealGramI18n.t("KeepDaysN"), d);
    }

    private void render() {
        root.removeAllViews();
        LinearLayout c = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, c, SealGramI18n.t("KeepDeleted"), SealGramI18n.t("KeepDeletedDesc"), SealGram.pref("keep_deleted", false), v -> SealGram.setPref("keep_deleted", v));

        SealGramHubActivity.row(ctx, c, R.drawable.msg_archive, SealGramI18n.t("LogTitle"), String.valueOf(SealKeep.count()), () -> presentFragment(new SealGramLogActivity()));

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("KeepWhat"));
        LinearLayout w = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, w, SealGramI18n.t("KeepText"), null, SealGram.pref("keep_text", true), v -> SealGram.setPref("keep_text", v));
        SealGramHubActivity.toggle(ctx, w, SealGramI18n.t("KeepPhotos"), null, SealGram.pref("keep_photo", true), v -> SealGram.setPref("keep_photo", v));
        SealGramHubActivity.toggle(ctx, w, SealGramI18n.t("KeepVoice"), null, SealGram.pref("keep_voice", true), v -> SealGram.setPref("keep_voice", v));

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("KeepLook"));
        LinearLayout l = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, l, SealGramI18n.t("KeepTrash"), SealGramI18n.t("KeepTrashDesc"), SealGram.pref("keep_trash", true), v -> SealGram.setPref("keep_trash", v));
        SealGramHubActivity.row(ctx, l, R.drawable.msg_palette, SealGramI18n.t("KeepTrashColor"), colorLabel(SealGram.intPref("keep_trash_color", 0)), () ->
                SealColorDialog.show(getParentActivity(), SealGramI18n.t("KeepTrashColor"), SealGram.intPref("keep_trash_color", 0), col -> {
                    SealGram.setIntPref("keep_trash_color", col);
                    render();
                }));
        SealGramHubActivity.row(ctx, l, R.drawable.msg_palette, SealGramI18n.t("KeepMsgColor"), colorLabel(SealGram.intPref("keep_msg_color", 0)), () ->
                SealColorDialog.show(getParentActivity(), SealGramI18n.t("KeepMsgColor"), SealGram.intPref("keep_msg_color", 0), col -> {
                    SealGram.setIntPref("keep_msg_color", col);
                    render();
                }));

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("KeepStorage"));
        LinearLayout s = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.row(ctx, s, R.drawable.msg_delete, SealGramI18n.t("KeepDays"), daysLabel(SealKeep.days()), this::pickDays);
        SealGramHubActivity.row(ctx, s, R.drawable.msg_delete, SealGramI18n.t("KeepClear"), String.valueOf(SealKeep.count()), this::confirmClear);

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("KeepMore"));
        LinearLayout m = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, m, SealGramI18n.t("EditHistory"), SealGramI18n.t("EditHistoryDesc"), SealGram.pref("edit_history", false), v -> SealGram.setPref("edit_history", v));

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("KeepBgHeader"));
        LinearLayout b = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, b, SealGramI18n.t("KeepBg"), SealGramI18n.t("KeepBgDesc"), bgEnabled(), v -> {
            MessagesController.getGlobalNotificationsSettings().edit().putBoolean("pushService", v).commit();
            ApplicationLoader.startPushService();
        });
        SealGramHubActivity.toggle(ctx, b, SealGramI18n.t("KeepAutostart"), SealGramI18n.t("KeepAutostartDesc"), SealGram.pref("keep_autostart", false), v -> SealGram.setPref("keep_autostart", v));
    }

    private void pickDays() {
        Activity a = getParentActivity();
        if (a == null) return;
        CharSequence[] items = new CharSequence[DAYS.length];
        for (int i = 0; i < DAYS.length; i++) items[i] = daysLabel(DAYS[i]);
        new AlertDialog.Builder(a)
                .setTitle(SealGramI18n.t("KeepDays"))
                .setItems(items, (d, which) -> {
                    SealGram.setIntPref("keep_days", DAYS[which]);
                    new Thread(() -> SealKeep.cleanup(false)).start();
                    render();
                })
                .show();
    }

    private void confirmClear() {
        Activity a = getParentActivity();
        if (a == null) return;
        new AlertDialog.Builder(a)
                .setTitle(SealGramI18n.t("KeepClear"))
                .setMessage(SealGramI18n.t("KeepClearConfirm"))
                .setPositiveButton(SealGramI18n.t("CommonOK"), (d, w) -> {
                    new Thread(() -> SealKeep.cleanup(true)).start();
                    AndroidUtilities.runOnUIThread(this::render, 600);
                })
                .setNegativeButton(SealGramI18n.t("CommonCancel"), null)
                .show();
    }
}
