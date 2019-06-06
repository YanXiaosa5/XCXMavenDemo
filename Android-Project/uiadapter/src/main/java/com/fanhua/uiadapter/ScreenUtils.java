package com.fanhua.uiadapter;

/**
 * 获得屏幕相关的辅助类
 * Created by yxs on 2016/2/22.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fanhua.uiadapter.utils.ViewUtils;

public class ScreenUtils {
    private ScreenUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 设置全屏
     *
     * @param activity 调用者页面
     */
    public static void setFullScreen(Activity activity) {
        //隐藏标题栏
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = activity.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }


    /**
     * 获得屏幕宽度
     *
     * @param context 上下文引用
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文引用
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕密度 屏幕密度（0.75 / 1.0 / 1.5）
     *
     * @param context 上下文引用
     * @return 屏幕像素
     */
    public static float getScreenDensity(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

    /**
     * 获得屏幕密度DPI 屏幕密度DPI（120 / 160 / 240） 开发基准屏的DPI为160 以此计算字体缩放大小
     *
     * @param context 上下文引用
     * @return 屏幕像素密度dpi
     */
    public static int getScreenDensityDpi(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.densityDpi;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文引用
     * @return 状态栏高度
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity 页面
     * @return 图片
     */
    @SuppressWarnings("deprecation")
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity 页面引用
     * @return 图片
     */
    @SuppressWarnings("deprecation")
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }


    /**
     * API19以上的的设备上透明化状态栏和导航栏
     * @param activity  页面引用
     * @param statusColor  状态栏颜色
     */
    @TargetApi(19)
    public static void alphaStatusBarAndNavBar(Activity activity, int statusColor) {
        // 如果不希望 APP 的内容被上拉到状态列 (Status bar) 的话，要记得在界面 (Layout) XML 档中，最外面的那层，要再加上一个属性 fitsSystemWindows为true
        if (Build.VERSION.SDK_INT > 18) {
            Window window = activity.getWindow();
            // 状态栏透明
            window.setFlags(0x04000000, 0x04000000);
            changeStatusNavBarColor(activity, statusColor);
        }
    }

    /**
     * 4.4以上可以自定义状态栏和导航栏颜色
     * @param activity  页面引用
     * @param statusColor  状态栏颜色
     */
    private static void changeStatusNavBarColor(Activity activity, int statusColor) {
        View statusBarTintView = new View(activity);
        FrameLayout.LayoutParams statusParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewUtils.getStatusBarHeightInDp(activity)+1);
        statusParams.gravity = Gravity.TOP;
        FrameLayout.LayoutParams navParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewUtils.getActionBarHeight(activity)+1);
        navParams.gravity = Gravity.BOTTOM;
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        statusBarTintView.setLayoutParams(statusParams);
        statusBarTintView.setBackgroundColor(statusColor);
        decorView.addView(statusBarTintView);
    }

    public static int getOrientation(Context context) {//返回当前屏幕方向。
        return context.getResources().getConfiguration().orientation;
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context 上下文引用
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}