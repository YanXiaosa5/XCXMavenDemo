package com.fanhua.uiadapter.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

public class ViewUtils {

    // temp variable
    private static TypedValue mTmpValue = new TypedValue();

    // This intro hides the system bars.
    @TargetApi(19)
    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hideSelf and show.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hideSelf nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hideSelf status bar
                | 0x00000800);// View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    // This intro shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    @TargetApi(19)
    public static void showSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * 23      * Returns true if view's layout direction is right-to-left.
     * 24      *
     * 25      * @param view the View whose layout is being considered
     * 26
     */
    public static boolean isLayoutRtl(View view) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            return view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        } else {
            // All layouts are LTR before JB MR1.
            return false;
        }
    }

    public static Point getScreenRawSize(Display display) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            Point outPoint = new Point();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            outPoint.x = metrics.widthPixels;
            outPoint.y = metrics.heightPixels;
            return outPoint;
        } else {
            Point outPoint = new Point();
            Method mGetRawH;
            try {
                mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                outPoint.x = (Integer) mGetRawW.invoke(display);
                outPoint.y = (Integer) mGetRawH.invoke(display);
                return outPoint;
            } catch (Throwable e) {
                return new Point(0, 0);
            }
        }
    }

    public static int getNavBarHeightInPX(Context context) {
        int navBarHeight = 0;
        Resources resources = context.getResources();
        int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid > 0) {
            //获取导航栏是否显示true or false
            boolean isNavBarShow = resources.getBoolean(rid);
            System.out.println("isNavBarShow = " + isNavBarShow);
            if (isNavBarShow) {
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //获取高度
                    navBarHeight = resources.getDimensionPixelSize(resourceId);
                }
            }
        }

        return navBarHeight;
    }

    public static int getActionBarHeightInDp(Context context) {


        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = (int) TypedValue.complexToFloat(tv.data);
            }
        } else {
            tv.data = 48;
            actionBarHeight = (int) TypedValue.complexToFloat(tv.data);
        }
        return actionBarHeight;
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, dm);
            }
        } else {
            tv.data = 48;
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, dm);
        }
        return actionBarHeight;
    }

    public static int getSystemBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("system_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取状态栏高度单位px
     */
    public static int getStatusBarHeightInPx(Context context) {
        return DensityUtils.dp2px(context, getStatusBarHeightInDp(context));
    }

    /**
     * 获取状态栏高度单位像素值
     */
    public static int getStatusBarHeightInDp(Context context) {
        int result = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    public static int getSystemBarHeightInDp(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("system_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResourceValue(context, resourceId);
        }
        return result;
    }

    /**
     * 获取资源中的数值，没有经过转换，比如dp,sp等
     */
    public static int getResourceValue(Context context, int resId) {
        TypedValue value = mTmpValue;
        context.getResources().getValue(resId, value, true);
        return (int) TypedValue.complexToFloat(value.data);
    }

    /**
     * set view height
     *
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }

    /**
     * set view width
     *
     * @param view
     * @param width
     */
    public static void setViewWidth(View view, int width) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
    }


    /**
     * 功能 设置view的宽度和高度
     *
     * @param view view对象
     * @param width 宽度
     *              @param height 高度
     */
    public static void setViewSize(View view, float width, float height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (width > 0)
            params.width = (int) width;
        if (height > 0)
            params.height = (int) height;
    }

    public static void setViewMargin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
