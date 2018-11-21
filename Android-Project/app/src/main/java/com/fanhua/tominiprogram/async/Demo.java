package com.fanhua.tominiprogram.async;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 15:46
 */
public class Demo {
    public static final String TAG = "Demo";
    private AsyncClient mClient;

    public Demo() {
        mClient = new AsyncClient.Builder().coreRequests(1).maxRequests(5).build();
    }

    public void demo() {
        mClient.newCall(new TagTask<String>(mClient.defaultTaskTag()) {
            @Override
            public String call(Call<String> originalCall) throws Exception {
                Log.d(TAG, Thread.currentThread() + " task start: call is " + originalCall);
                SystemClock.sleep(6000);
                Log.d(TAG, Thread.currentThread() + " task end: call is " + originalCall);
                return "It's OK";
            }
        }).enqueue(new CallbackImpl<String>());
    }

    private static class CallbackImpl<R> implements Callback<R> {

        @Override
        public void onFailed(Call<R> call, Exception e) {
            Log.e(TAG, Thread.currentThread() + " onFailed: call is " + call, e);
        }

        @Override
        public void onSuccessed(Call<R> call, R result) {
            Log.d(TAG, Thread.currentThread() + " onSuccessed: call is " + call + ", result is " + result);
        }

        @Override
        public void onCanceled(Call<R> call) {
            Log.d(TAG, Thread.currentThread() + " onCanceled: call is " + call);
        }
    }
}
