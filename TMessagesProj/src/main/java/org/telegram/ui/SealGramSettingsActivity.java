package org.telegram.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.util.function.Consumer;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SealGram;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

public class SealGramSettingsActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(SealGramI18n.t("PrivacyTitle"));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, AndroidUtilities.dp(12), 0, AndroidUtilities.dp(24));
        LinearLayout c = SealGramHubActivity.card(context, root);
        SealGramHubActivity.toggle(context, c, SealGramI18n.t("PrivacyGhostMode"), SealGramI18n.t("PrivacyGhostModeDesc"), SealGram.ghost(), SealGram::setGhost);
        SealGramHubActivity.toggle(context, c, SealGramI18n.t("PrivacyHideOnline"), SealGramI18n.t("PrivacyHideOnlineDesc"), SealGram.getSub("ghost_online"), v -> SealGram.setSub("ghost_online", v));
        SealGramHubActivity.toggle(context, c, SealGramI18n.t("PrivacyHideTyping"), SealGramI18n.t("PrivacyHideTypingDesc"), SealGram.getSub("ghost_typing"), v -> SealGram.setSub("ghost_typing", v));
        SealGramHubActivity.toggle(context, c, SealGramI18n.t("PrivacyHideRead"), SealGramI18n.t("PrivacyHideReadDesc"), SealGram.getSub("ghost_read"), v -> SealGram.setSub("ghost_read", v));
        SealGramHubActivity.toggle(context, c, SealGramI18n.t("ReadAfterSend"), SealGramI18n.t("ReadAfterSendDesc"), SealGram.pref("read_after_send", true), v -> SealGram.setPref("read_after_send", v));
        SealGramHubActivity.toggle(context, c, SealGramI18n.t("GhostDelay"), SealGramI18n.t("GhostDelayDesc"), SealGram.pref("ghost_delay", false), v -> SealGram.setPref("ghost_delay", v));
        SealGramHubActivity.row(context, c, R.drawable.msg_edit, SealGramI18n.t("GhostDelayWait"), String.format(SealGramI18n.t("SecN"), Math.max(12, SealGram.intPref("ghost_delay_sec", 12))), () -> {
            final int[] secs = {12, 30, 60, 300};
            CharSequence[] labels = new CharSequence[secs.length];
            for (int i = 0; i < secs.length; i++) labels[i] = String.format(SealGramI18n.t("SecN"), secs[i]);
            new org.telegram.ui.ActionBar.AlertDialog.Builder(getParentActivity())
                    .setTitle(SealGramI18n.t("GhostDelayWait"))
                    .setItems(labels, (d, which) -> {
                        SealGram.setIntPref("ghost_delay_sec", secs[which]);
                        presentFragment(new SealGramSettingsActivity(), true);
                    })
                    .show();
        });
        LinearLayout c2 = SealGramHubActivity.card(context, root);
        SealGramHubActivity.toggle(context, c2, SealGramI18n.t("PrivacyHidePhone"), SealGramI18n.t("PrivacyHidePhoneDesc"), SealGram.hideOwnPhoneInProfile(), SealGram::setHideOwnPhoneInProfile);
        SealGramHubActivity.note(context, root, SealGramI18n.t("HubDisclaimer"));
        fragmentView = SealGramHubActivity.scroll(context, root);
        return fragmentView;
    }
}
