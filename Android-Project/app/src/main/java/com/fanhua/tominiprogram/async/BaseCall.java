package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 9:56
 */
public abstract class BaseCall<Res> implements Call<Res> {
    protected boolean executed;
    protected boolean canceled;

    @Override
    public synchronized void cancel() {
        canceled = true;
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Override
    public synchronized boolean isCanceled() {
        return canceled;
    }

    @Override
    public void enqueue() {
        enqueue(null);
    }

    @Override
    public void enqueue(Callback<Res> callback) {
        enqueue(callback, true);
    }

    @Override
    public void enqueue(Callback<Res> callback, boolean callbackOnMain) {

    }
}
