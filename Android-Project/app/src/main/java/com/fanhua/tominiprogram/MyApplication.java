package com.fanhua.tominiprogram;


import android.content.pm.ActivityInfo;

import com.fanhua.uiadapter.LibApplication;
import com.fanhua.uiadapter.Resolution;
import com.fanhua.uiadapter.ScreenUtils;

public class MyApplication extends LibApplication {

    @Override
    public Resolution designResolutionForApp() {
        return new Resolution(750, 1334, 1.0F, 160, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        return new Resolution(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this), ScreenUtils.getScreenDensity(this), ScreenUtils.getScreenDensityDpi(this), ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
