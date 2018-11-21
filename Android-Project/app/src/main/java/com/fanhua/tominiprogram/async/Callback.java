package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/25
 * Time: 17:01
 */
public interface Callback<R> {
    void onFailed(Call<R> call, Exception e);

    void onSuccessed(Call<R> call, R result);

    void onCanceled(Call<R> call);
}
