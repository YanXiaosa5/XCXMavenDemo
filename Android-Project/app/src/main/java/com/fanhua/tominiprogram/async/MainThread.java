package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 13:24
 */
public interface MainThread {
    boolean isOnMainThread();

    void post(Runnable r);

    void postDelayed(Runnable r, long delayMillis);
}
