package com.fanhua.tominiprogram;


import android.content.pm.ActivityInfo;

import com.baidu.mobstat.StatService;
import com.fanhua.uiadapter.LibApplication;
import com.fanhua.uiadapter.Resolution;
import com.fanhua.uiadapter.http.HttpClient;

public class MyApplication extends LibApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //配置服务器url
        HttpClient.setBaseUrl("http://47.105.61.82/");
        StatService.start(this);
        System.out.println("onCreate走了");
    }

    @Override
    public Resolution designResolutionForApp() {
        //配置基础的适配信息
        return new Resolution(750, 1334, 1.0F, 160, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
