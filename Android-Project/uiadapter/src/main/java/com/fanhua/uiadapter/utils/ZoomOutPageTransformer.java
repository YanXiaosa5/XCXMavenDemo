package com.fanhua.uiadapter.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * viewpager的item缩放效果
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    //左中右切换过渡
    @Override
    public void transformPage(View view, float position) {

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setScaleX(0.9f);
            view.setScaleY(0.9f);
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            float scaleFactor = 0.9f
                    + (1 - 0.9f) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            float scaleFactor = 0.9f
                    + (1 - 0.9f) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setScaleX(0.9f);
            view.setScaleY(0.9f);
        }
    }


    //渐隐和渐显
//    @Override
//    public void transformPage(View view, float position) {
//        int pageWidth = view.getWidth();
//
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(0);
//
//        } else if (position <= 0) { // [-1,0]
//            // Use the default slide transition when moving to the left page
//            view.setAlpha(1);
//            view.setTranslationX(0);
//            view.setScaleX(1);
//            view.setScaleY(1);
//
//        } else if (position <= 1) { // (0,1]
//            // Fade the page out.
//            view.setAlpha(1 - position);
//
//            // Counteract the default slide transition
//            view.setTranslationX(pageWidth * -position);
//
//            // Scale the page down (between MIN_SCALE and 1)
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(0);
//        }
//    }

}
