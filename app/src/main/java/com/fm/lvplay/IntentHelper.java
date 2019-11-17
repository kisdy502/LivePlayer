package com.fm.lvplay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class IntentHelper {

    /**
     * 拉起蜂蜜直播TV
     *
     * @param context
     */
    public void launchBeeLive(Context context) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.fengmizhibo.live", "cn.beelive.ui.LoadingActivity");
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
