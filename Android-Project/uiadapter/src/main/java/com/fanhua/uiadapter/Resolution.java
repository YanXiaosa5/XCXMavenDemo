package com.fanhua.uiadapter;

import android.content.Context;



/**
 * 屏幕参数
 * Created by yxs on 2016/1/4.
 */
public class Resolution {


    /**
     * 屏幕宽度
     */
    protected int width;

    /**
     * 屏幕高度
     */
    protected int height;

    /**
     * 屏幕的密度比例
     */
    protected float density;

    /**
     * 屏幕的密度
     */
    protected int densityDpi;

    /**
     * 屏幕方式适配
     */
    protected int screenOrientation;

    public Resolution(int w, int h, float density, int densityDpi, int screenOrientation) {
        width = w;
        height = h;
        this.density = density;
        this.densityDpi = densityDpi;
        this.screenOrientation = screenOrientation;
    }

    /**
     * 像素密度DPI
     *
     * @param densityDpi
     */
    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    /**
     * 设置宽度
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 设置像素密度
     *
     * @param density
     */
    public void setDensity(float density) {
        this.density = density;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getDensity() {
        return density;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public void setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    /**
     * 获取屏幕参数，大小，密度和密度系数等
     *
     * @param context
     * @return
     */
    public static Resolution getResolution(Context context) {

        Resolution result = null;

        // 屏幕宽度（像素）
        int width = ScreenUtils.getScreenWidth(context);

        // 屏幕高度（像素）
        int height = ScreenUtils.getScreenHeight(context);

        // 屏幕密度（0.75 / 1.0 / 1.5）
        float density = ScreenUtils.getScreenDensity(context);

        // 屏幕密度DPI（120 / 160 / 240）
        int densityDpi = ScreenUtils.getScreenDensityDpi(context);

        result = new Resolution(width, height, density, densityDpi, ScreenUtils.getOrientation(context));

        L.d(result.toString());
        return result;
    }

    @Override
    public String toString() {
        return "Resolution : {" +
                "width=" + width +
                ", height=" + height +
                ", density=" + density +
                ", densityDpi=" + densityDpi +
                '}';
    }

    public void switchOrientation(int curOrientation) {

        if (this.screenOrientation == curOrientation) return;

        this.setScreenOrientation(curOrientation);

        int temp = this.width;
        this.width = this.height;
        this.height = temp;
    }
}
