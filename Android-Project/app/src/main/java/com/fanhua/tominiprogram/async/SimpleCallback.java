package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 14:46
 */
public class SimpleCallback<R> implements Callback<R> {
    @Override
    public void onFailed(Call<R> call, Exception e) {

    }

    @Override
    public void onSuccessed(Call<R> call, R result) {

    }

    @Override
    public void onCanceled(Call<R> call) {

    }
}
