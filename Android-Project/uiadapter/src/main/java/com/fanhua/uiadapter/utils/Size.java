package com.fanhua.uiadapter.utils;

import java.io.Serializable;

/**
 * Created by misl on 2017/5/24.
 */
public class Size implements Serializable {

    public float w;

    public float h;

    public Size(float w, float h) {
        this.w = w;
        this.h = h;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }
}
