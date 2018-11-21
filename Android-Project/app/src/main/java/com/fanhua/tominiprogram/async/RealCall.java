package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 9:52
 */
final class RealCall<R> extends BaseCall<R> {
    private final AsyncClient client;
    private Task<R> originalTask;

    private RealCall(AsyncClient client, Task<R> task) {
        this.client = client;
        this.originalTask = task;
    }

    static <R> RealCall<R> newRealCall(AsyncClient client, Task<R> originalTask) {
        return new RealCall<>(client, originalTask);
    }

    @Override
    public Task<R> task() {
        return originalTask;
    }

    public String tag() {
        return originalTask.tag();
    }

    @Override
    public R execute() throws Exception {
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Executed");
            }
            executed = true;
        }
        try {
            client.dispatcher().executed(this);
            return originalTask.call(this);
        } catch (Exception e) {
            throw e;
        } finally {
            client.dispatcher().finishedSync(this);
        }
    }

    @Override
    public void enqueue(Callback<R> callback, boolean callbackOnMain) {
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Executed");
            }
            executed = true;
        }
        client.dispatcher().enqueue(new AsyncCall(callback, callbackOnMain));
    }

    final class AsyncCall extends NamedRunnable {
        private final Callback<R> callback;
        private boolean callbackOnMain;

        AsyncCall(Callback<R> callback, boolean callbackOnMain) {
            super("AsyncClient [" + originalTask.tag() + ", " + RealCall.this.hashCode() + "]");
            this.callback = callback;
            this.callbackOnMain = callbackOnMain;
        }

        Task<R> task() {
            return originalTask;
        }

        RealCall<R> get() {
            return RealCall.this;
        }

        @Override
        public void execute() {
            boolean signalledCallback = false;
            try {
                if (isCanceled()) {
                    signalledCallback = true;
                    processCallback("cancel", null, null);
                } else {
                    R result = originalTask.call(get());
                    if (isCanceled()) {
                        signalledCallback = true;
                        processCallback("cancel", null, null);
                    } else {
                        signalledCallback = true;
                        processCallback("success", null, result);
                    }
                }
            } catch (Exception e) {
                if (!signalledCallback) {
                    processCallback("fail", e, null);
                }
            } finally {
                client.dispatcher().finishedAsync(this);
            }
        }

        private void processCallback(final String method, final Exception e, final R result) {
            if (callback != null) {
                if (callbackOnMain) {
                    client.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(method, e, result);
                        }
                    });
                } else {
                    invokeMethod(method, e, result);
                }
            }
        }

        private void invokeMethod(String method, Exception e, R result) {
            switch (method) {
                case "success":
                    callback.onSuccessed(get(), result);
                    break;
                case "fail":
                    callback.onFailed(get(), e);
                    break;
                case "cancel":
                    callback.onCanceled(get());
                    break;
            }
        }
    }
}
