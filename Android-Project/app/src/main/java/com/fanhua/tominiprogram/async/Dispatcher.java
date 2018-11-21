package com.fanhua.tominiprogram.async;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Dispatcher {
    public static final int DEFAULT_MAX_REQUESTS = 4;
    public static final int DEFAULT_CORE_REQUESTS = 0;
    public static final long DEFAULT_KEEP_ALIVE_SECONDS = 60;

    private int maxRequests = DEFAULT_MAX_REQUESTS;
    private int coreRequests = DEFAULT_CORE_REQUESTS;
    private long keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;

    private @Nullable
    Runnable idleCallback;

    private @Nullable
    ExecutorService executorService;

    private final Deque<RealCall.AsyncCall> readyCalls = new ArrayDeque<>();

    private final Deque<RealCall.AsyncCall> runningCalls = new ArrayDeque<>();

    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();

    public Dispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Dispatcher() {

    }

    public Dispatcher(int maxRequests, int coreRequests, long keepAliveSeconds) {
        if (maxRequests < 1) {
            throw new IllegalArgumentException("max < 1: " + maxRequests);
        }
        if (coreRequests < 0) {
            coreRequests = 0;
        } else {
            if (coreRequests > maxRequests) {
                coreRequests = maxRequests;
            }
        }
        if (keepAliveSeconds < 0) {
            keepAliveSeconds = 0;
        }
        this.maxRequests = maxRequests;
        this.coreRequests = coreRequests;
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(coreRequests, Integer.MAX_VALUE, keepAliveSeconds, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    return new Thread(r, "TaskManager Dispatcher");
                }
            });
        }
        return executorService;
    }

    public synchronized void setMaxRequests(int maxRequests) {
        if (maxRequests < 1) {
            throw new IllegalArgumentException("max < 1: " + maxRequests);
        }
        this.maxRequests = maxRequests;
        promoteCalls();
    }

    public synchronized int getMaxRequests() {
        return maxRequests;
    }

    public synchronized void setIdleCallback(@Nullable Runnable idleCallback) {
        this.idleCallback = idleCallback;
    }

    synchronized void enqueue(RealCall.AsyncCall call) {
        if (runningCalls.size() < maxRequests) {
            runningCalls.add(call);
            executorService().execute(call);
        } else {
            readyCalls.add(call);
        }
    }

    public synchronized void cancelAll() {
        for (RealCall.AsyncCall call : readyCalls) {
            call.get().cancel();
        }

        for (RealCall.AsyncCall call : runningCalls) {
            call.get().cancel();
        }

        for (RealCall call : runningSyncCalls) {
            call.cancel();
        }
    }

    public synchronized void cancel(String tag) {
        for (RealCall.AsyncCall readyCall : readyCalls) {
            if (isTagEquals(readyCall.get().tag(), tag)) {
                readyCall.get().cancel();
            }
        }

        for (RealCall.AsyncCall runningCall : runningCalls) {
            if (isTagEquals(runningCall.get().tag(), tag)) {
                runningCall.get().cancel();
            }
        }

        for (RealCall syncCall : runningSyncCalls) {
            if (isTagEquals(syncCall.tag(), tag)) {
                syncCall.cancel();
            }
        }
    }

    private boolean isTagEquals(String callTag, String tag) {
        return (callTag == null && tag == null) || (tag != null && tag.equals(callTag));
    }

    private void promoteCalls() {
        if (runningCalls.size() >= maxRequests) {
            // Already running max capacity.
            return;
        }
        if (readyCalls.isEmpty()) {
            // No ready calls to promote.
            return;
        }

        for (Iterator<RealCall.AsyncCall> i = readyCalls.iterator(); i.hasNext(); ) {
            RealCall.AsyncCall call = i.next();
            i.remove();
            runningCalls.add(call);
            executorService().execute(call);
            if (runningCalls.size() >= maxRequests) {
                // Reached max capacity.
                return;
            }
        }
    }

    synchronized void executed(RealCall call) {
        runningSyncCalls.add(call);
    }

    void finishedAsync(RealCall.AsyncCall call) {
        finished(runningCalls, call, true);
    }

    void finishedSync(RealCall call) {
        finished(runningSyncCalls, call, false);
    }

    private <T> void finished(Deque<T> calls, T call, boolean promoteCalls) {
        int runningCallsCount;
        Runnable idleCallback;
        synchronized (this) {
            if (!calls.remove(call)) {
                throw new AssertionError("Call wasn't in-flight!");
            }
            if (promoteCalls) {
                promoteCalls();
            }
            runningCallsCount = runningCallsCount();
            idleCallback = this.idleCallback;
        }

        if (runningCallsCount == 0 && idleCallback != null) {
            idleCallback.run();
        }
    }

    public synchronized List<Call> queuedCalls() {
        List<Call> result = new ArrayList<>();
        for (RealCall.AsyncCall readyCall : readyCalls) {
            result.add(readyCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Call> runningCalls() {
        List<Call> result = new ArrayList<>();
        result.addAll(runningSyncCalls);
        for (RealCall.AsyncCall asyncCall : runningCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized int queuedCallsCount() {
        return readyCalls.size();
    }

    public synchronized int runningCallsCount() {
        return runningCalls.size() + runningSyncCalls.size();
    }
}

