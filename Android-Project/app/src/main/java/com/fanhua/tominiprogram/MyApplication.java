package com.fanhua.tominiprogram;

import com.fanhua.uiadapter.LibApplication;
import com.fanhua.uiadapter.Resolution;

public class MyApplication extends LibApplication {

    @Override
    public Resolution designResolutionForApp() {
        return new Resolution(750, 1334, 1.0F, 160, 1);
    }
}
