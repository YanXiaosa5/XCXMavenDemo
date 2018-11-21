package com.fanhua.tominiprogram.async;

import java.util.concurrent.Callable;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 14:50
 */
public class CallableTask<R> extends TagTask<R> {
    private Callable<R> callable;

    public CallableTask(Callable<R> callable, String tag) {
        super(tag);
        if (callable == null) {
            throw new IllegalArgumentException("callable == null");
        }
        this.callable = callable;
    }

    @Override
    public R call(Call<R> originalCall) throws Exception {
        return callable.call();
    }
}
