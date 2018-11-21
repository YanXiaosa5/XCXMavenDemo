package com.fanhua.tominiprogram.async;

import java.util.concurrent.Callable;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 9:17
 */
public class AsyncClient implements Call.Factory {
    private Dispatcher dispatcher;
    private MainThread mainThread;
    private String defaultTaskTag;

    public AsyncClient() {
        this(new Builder());
    }

    public AsyncClient(Builder builder) {
        dispatcher = builder.dispatcher;
        if (dispatcher == null) {
            dispatcher = new Dispatcher(builder.maxRequests, builder.coreRequests, builder.keepAliveSeconds);
        }
        mainThread = builder.mainThread;
        defaultTaskTag = builder.defaultTaskTag;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public MainThread mainThread() {
        return mainThread;
    }

    public String defaultTaskTag() {
        return defaultTaskTag;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public <R> Call<R> newCall(Task<R> task) {
        return RealCall.newRealCall(this, task);
    }

    public <R> Call<R> newCall(Callable<R> callable, String tag) {
        return RealCall.newRealCall(this, new CallableTask<R>(callable, tag));
    }

    public Call<Object> newCall(Runnable runnable, String tag) {
        return RealCall.newRealCall(this, new RunnableTask(runnable, tag));
    }

    public void cancelTask(String tag) {
        dispatcher.cancel(tag);
    }

    public void cancelTask(Call call) {
        call.cancel();
    }

    public void cancelAllTasks() {
        dispatcher.cancelAll();
    }

    public void runOnMainThread(Runnable r) {
        mainThread.post(r);
    }

    public void runOnMainThreadDelayed(Runnable r, long delayMillis) {
        mainThread.postDelayed(r, delayMillis);
    }

    public boolean isOnMainThread() {
        return mainThread.isOnMainThread();
    }

    public static class Builder {
        Dispatcher dispatcher;
        MainThread mainThread;
        int maxRequests = Dispatcher.DEFAULT_MAX_REQUESTS;
        int coreRequests = Dispatcher.DEFAULT_CORE_REQUESTS;
        long keepAliveSeconds = Dispatcher.DEFAULT_KEEP_ALIVE_SECONDS;
        String defaultTaskTag;

        public Builder() {
            mainThread = new AndroidMainThread();
        }

        public Builder(AsyncClient client) {
            dispatcher = client.dispatcher;
            mainThread = client.mainThread;
        }

        public Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder mainThread(MainThread mainThread) {
            if (mainThread == null) {
                throw new IllegalArgumentException("mainThread == null");
            }
            this.mainThread = mainThread;
            return this;
        }

        public Builder maxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public Builder coreRequests(int coreRequests) {
            this.coreRequests = coreRequests;
            return this;
        }

        public Builder keepAliveSeconds(long keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
            return this;
        }

        public Builder defaultTaskTag(String defaultTag) {
            this.defaultTaskTag = defaultTag;
            return this;
        }

        public AsyncClient build() {
            return new AsyncClient(this);
        }
    }
}
