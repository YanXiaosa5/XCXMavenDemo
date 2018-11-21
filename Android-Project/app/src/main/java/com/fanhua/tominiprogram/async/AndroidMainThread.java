package com.fanhua.tominiprogram.async;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 10:32
 */
public class AndroidMainThread implements MainThread {
    private Handler mMainHandler;

    public AndroidMainThread() {
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void post(Runnable r) {
        if (r != null) {
            mMainHandler.post(r);
        }
    }

    public void postDelayed(Runnable r, long delayMillis) {
        if (r != null) {
            mMainHandler.postDelayed(r, delayMillis);
        }
    }
}
