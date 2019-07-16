package com.fanhua.tominiprogram.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.fanhua.tominiprogram.MyApplication;


import java.util.List;

/**
 * Created by debug on 2018/12/7.
 */

public class AppUtils {
    public static boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(MyApplication.getInstance().getPackageName())) {
                    /*
                    BACKGROUND=400 EMPTY=500 FOREGROUND=100
                    GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                     */
                Log.i(MyApplication.getInstance().getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + MyApplication.getInstance().getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(MyApplication.getInstance().getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(MyApplication.getInstance().getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
