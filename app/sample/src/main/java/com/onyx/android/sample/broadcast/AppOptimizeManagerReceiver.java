package com.onyx.android.sample.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by anypwx on 2018/3/26 15:47.
 * setting the app Optimize
 * example : set enableContrastEnhance , forceFullScreen
 *
 */

public class AppOptimizeManagerReceiver extends BroadcastReceiver {
    static final String TAG = AppOptimizeManagerReceiver.class.getSimpleName();
    public static final String APP_OPTIMIZE_MANAGER = "com.onyx.app.optimize.setting";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"AppOptimizeManagerReceiver onReceive...");
        if (null == intent) {
            return;
        }

        boolean fullScreen = intent.getBooleanExtra("optimize_fullScreen",true);
        String pkgName = intent.getStringExtra("optimize_pkgName");
        Log.i(TAG,"fullScreen -" + fullScreen+", pkgName - "+pkgName);
    }
}
