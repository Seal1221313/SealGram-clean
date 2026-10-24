/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppStartReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        boolean boot = Intent.ACTION_BOOT_COMPLETED.equals(action);
        boolean replaced = Intent.ACTION_MY_PACKAGE_REPLACED.equals(action);
        boolean restart = "org.telegram.start".equals(action);
        if (!boot && !replaced && !restart) return;
        AndroidUtilities.runOnUIThread(() -> {
            if (boot) {
                SharedConfig.loadConfig();
                if (SharedConfig.passcodeHash.length() > 0) {
                    SharedConfig.appLocked = true;
                    SharedConfig.saveConfig();
                }
            }
            if (SealGram.pref("keep_autostart", false)) {
                ApplicationLoader.startPushService();
            }
        });
    }
}
