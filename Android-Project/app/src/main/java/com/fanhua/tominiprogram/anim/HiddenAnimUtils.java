package com.fanhua.tominiprogram.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * https://blog.csdn.net/debbytang/article/details/68496728
 */
public class HiddenAnimUtils {

    /**
     * 伸展高度
     */
    private int mHeight;

    /**
     * 需要展开隐藏的布局
     */
    private View hideView;

    /**
     *
     * @param context  上下文
     * @param hideView 需要隐藏或显示的布局view
     * @param height   布局展开的高度(dp)
     */
    public static HiddenAnimUtils newInstance(Context context, View hideView, int height) {
        return new HiddenAnimUtils(context, hideView, height);
    }

    private HiddenAnimUtils(Context context, View hideView,  int height) {
        this.hideView = hideView;
        float mDensity = context.getResources().getDisplayMetrics().density;
        mHeight = (int) (mDensity * height + 0.5);//伸展高度
    }

    /**
     * 开关
     */
    public void toggle() {
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }

    /**
     * 展开动画
     * @param v  view对象
     */
    private void openAnim(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(onAnimationEnd != null){
                    onAnimationEnd.end();
                    delay3Millens(hideView);
                }
            }
        });
        animator.start();
    }

    /**
     * 关闭动画
     * @param view  view对象
     */
    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        final ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if(onAnimationEnd != null){
                    onAnimationEnd.end();
                }
            }
        });
        animator.start();
    }

    /**
     * 创建属性动画
     * @param v  view
     * @param start  开始位置
     * @param end  结束位置
     * @return
     */
    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    /**
     * 停留3秒
     * @param v
     */
    public void delay3Millens(View v){
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(onAnimationEnd != null){
                    onAnimationEnd.delay();
                }
            }
        },3000);
    }

    public interface OnAnimationEnd{
        void end();

        void delay();
    }

    private OnAnimationEnd onAnimationEnd;

    public void setOnAnimationEnd(OnAnimationEnd onAnimationEnd){
        this.onAnimationEnd = onAnimationEnd;
    }


}

