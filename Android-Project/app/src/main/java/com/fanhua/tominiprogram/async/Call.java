package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/25
 * Time: 17:00
 */
public interface Call<R> {

    Task<R> task();

    R execute() throws Exception;

    void enqueue();

    void enqueue(Callback<R> callback);

    void enqueue(Callback<R> callback, boolean callbackOnMain);

    void cancel();

    boolean isExecuted();

    boolean isCanceled();

    interface Factory {
        <R> Call<R> newCall(Task<R> task);
    }
}
