package com.fanhua.tominiprogram.presenter;

import com.fanhua.uiadapter.http.BaseInterceptor;

public interface OnBaseHttpPresenter {

    void onSucceed(String data,BaseInterceptor baseInterceptor, String tag);

    void onCompleted(String tag);

    void onFailed(Throwable throwable, String tag);
}
