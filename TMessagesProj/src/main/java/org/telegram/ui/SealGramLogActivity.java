package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SealEdits;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.SealKeep;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

import java.util.ArrayList;

public class SealGramLogActivity extends BaseFragment {

    private LinearLayout root;
    private Context ctx;

    @Override
    public View createView(Context context) {
        ctx = context;
        SealGramHubActivity.setupBar(actionBar, SealGramI18n.t("LogTitle"), this::finishFragment);
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, AndroidUtilities.dp(12), 0, AndroidUtilities.dp(24));
        fragmentView = SealGramHubActivity.scroll(context, root);
        load();
        return fragmentView;
    }

    private void load() {
        SealKeep.loadItems(currentAccount, 200, items -> {
            if (root != null) render(items);
        });
    }

    private String name(long dialogId) {
        try {
            if (dialogId > 0) {
                TLRPC.User u = getMessagesController().getUser(dialogId);
                if (u != null) return UserObject.getUserName(u);
            } else {
                TLRPC.Chat c = getMessagesController().getChat(-dialogId);
                if (c != null && c.title != null) return c.title;
            }
        } catch (Throwable ignore) {
        }
        return String.valueOf(dialogId);
    }

    private static String preview(String s) {
        if (s == null) return "";
        s = s.replace('\n', ' ').trim();
        return s.length() > 90 ? s.substring(0, 90) + "\u2026" : s;
    }

    private static String when(long ts) {
        return android.text.format.DateFormat.format("dd.MM HH:mm", ts).toString();
    }

    private void render(ArrayList<SealKeep.Item> items) {
        root.removeAllViews();
        SealGramHubActivity.header(ctx, root, SealGramI18n.t("LogDeleted"));
        LinearLayout d = SealGramHubActivity.card(ctx, root);
        if (items.isEmpty()) {
            SealGramHubActivity.row(ctx, d, R.drawable.msg_delete, SealGramI18n.t("LogEmpty"), null, () -> {
            });
        }
        for (SealKeep.Item it : items) {
            final long dialogId = it.dialogId;
            final int mid = it.mid;
            SealGramHubActivity.row(ctx, d, R.drawable.msg_delete, name(dialogId) + ": " + preview(it.text), when(it.ts), () -> {
                Bundle args = new Bundle();
                if (dialogId > 0) args.putLong("user_id", dialogId);
                else args.putLong("chat_id", -dialogId);
                args.putInt("message_id", mid);
                presentFragment(new ChatActivity(args));
            });
        }

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("LogEdited"));
        LinearLayout e = SealGramHubActivity.card(ctx, root);
        ArrayList<SealEdits.Entry> edits = SealEdits.entries(currentAccount);
        if (edits.isEmpty()) {
            SealGramHubActivity.row(ctx, e, R.drawable.msg_edit, SealGramI18n.t("LogEmpty"), null, () -> {
            });
        }
        for (SealEdits.Entry en : edits) {
            final long dialogId = en.dialogId;
            final int mid = en.mid;
            SealGramHubActivity.row(ctx, e, R.drawable.msg_edit, name(dialogId) + ": " + preview(en.text), "\u00D7" + en.versions + "  " + when(en.ts), () -> {
                Activity a = getParentActivity();
                if (a == null) return;
                AlertDialog.Builder b = new AlertDialog.Builder(a);
                b.setTitle(SealGramI18n.t("EditHistory"));
                b.setMessage(SealEdits.format(currentAccount, dialogId, mid));
                b.setPositiveButton(SealGramI18n.t("CommonOK"), null);
                b.setNeutralButton(SealGramI18n.t("KeepOpenChat"), (dlg, w) -> {
                    Bundle args = new Bundle();
                    if (dialogId > 0) args.putLong("user_id", dialogId);
                    else args.putLong("chat_id", -dialogId);
                    args.putInt("message_id", mid);
                    presentFragment(new ChatActivity(args));
                });
                showDialog(b.create());
            });
        }
        if (!edits.isEmpty()) {
            SealGramHubActivity.row(ctx, e, R.drawable.msg_delete, SealGramI18n.t("LogClearEdits"), null, () -> {
                SealEdits.clear();
                load();
            });
        }
    }
}
