package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SealGram;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.SealVoice;
import org.telegram.ui.ActionBar.BaseFragment;

public class SealGramVoiceActivity extends BaseFragment {

    private LinearLayout root;
    private Context ctx;

    @Override
    public View createView(Context context) {
        ctx = context;
        SealGramHubActivity.setupBar(actionBar, SealGramI18n.t("VoiceTitle"), this::finishFragment);
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, AndroidUtilities.dp(12), 0, AndroidUtilities.dp(24));
        render();
        fragmentView = SealGramHubActivity.scroll(context, root);
        return fragmentView;
    }

    private void render() {
        root.removeAllViews();
        LinearLayout c = SealGramHubActivity.card(ctx, root);
        int cur = SealVoice.preset();
        for (int i = 0; i < SealVoice.KEYS.length; i++) {
            final int id = i;
            SealGramHubActivity.row(ctx, c, R.drawable.msg_voicechat, SealGramI18n.t(SealVoice.KEYS[i]), i == cur ? "✓" : null, () -> {
                SealVoice.setPreset(id);
                render();
            });
        }
        LinearLayout c2 = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, c2, SealGramI18n.t("VoiceInCalls"), SealGramI18n.t("VoiceInCallsDesc"), SealGram.pref("voice_calls", true), v -> SealGram.setPref("voice_calls", v));
        SealGramHubActivity.toggle(ctx, c2, SealGramI18n.t("VoiceInMessages"), SealGramI18n.t("VoiceInMessagesDesc"), SealGram.pref("voice_messages", true), v -> SealGram.setPref("voice_messages", v));
        SealGramHubActivity.toggle(ctx, c2, SealGramI18n.t("VoiceInRound"), SealGramI18n.t("VoiceInRoundDesc"), SealGram.pref("voice_round", true), v -> SealGram.setPref("voice_round", v));
        SealGramHubActivity.note(ctx, root, SealGramI18n.t("VoiceNote"));
    }
}
