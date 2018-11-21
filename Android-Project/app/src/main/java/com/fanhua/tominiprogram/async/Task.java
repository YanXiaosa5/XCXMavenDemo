package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 9:24
 */
public interface Task<R> {
    R call(Call<R> originalCall) throws Exception;

    String tag();
}
