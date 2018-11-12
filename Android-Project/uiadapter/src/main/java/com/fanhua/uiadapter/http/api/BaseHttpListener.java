package com.fanhua.uiadapter.http.api;


import com.fanhua.uiadapter.http.BaseInterceptor;

/**
 * Created by yxs on 2017/3/31.
 */

public interface BaseHttpListener {

    void onSucceed(String data, BaseInterceptor baseInterceptor, String reqTag);

    void onCompleted(String reqTag);

    void onFailed(Throwable e, String reqTag);
}
