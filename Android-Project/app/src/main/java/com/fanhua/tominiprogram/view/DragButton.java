package com.fanhua.tominiprogram.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * 可以拖动的布局
 */
public class DragButton extends RelativeLayout{

    private int parentHeight;
    //悬浮的父布局高度
    private int parentWidth;

    public DragButton(Context context) {
        super(context);
    }

    public DragButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int lastX;
    private int lastY;
    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);//默认是点击事件 isDrag=false;
                //默认是非拖动而是点击事件
                getParent().requestDisallowInterceptTouchEvent(true);
                //父布局不要拦截子布局的监听
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag = (parentHeight > 0 && parentWidth > 0);
                //只有父布局存在你才可以拖动
                if(!isDrag){
                    break;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                isDrag = distance > 0;
                //只有位移大于0说明拖动了
                if (!isDrag) break;
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = y < 0 ? 0 : y > parentHeight - getHeight() ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                if(!isNotDrag()){
                    System.out.println("是否拖动"+!isNotDrag());
                    setPressed(false);
                    if(rawX>=parentWidth/2){
                        //靠右吸附
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(300)
                                .xBy(parentWidth-getWidth()-getX())
                                .start();
                    } else {
                        //靠左吸附
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this,"x",getX(),0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(300);
                        oa.start();
                    }
                    isDrag = false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 拖动时返回false
     * @return
     */
    private boolean isNotDrag(){
        return !isDrag&&(getX()==0
                ||(getX()==parentWidth-getWidth()));
    }

}
