package com.fanhua.tominiprogram.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.fanhua.tominiprogram.R;

public class AnimPointView extends View {

    public static final float sRADIUS = 20F;
    private Point mCurrentPoint;
    private Paint mPaint;
    private Paint mTextPaint;
    //动画持续时间 默认5S
    private int mAnimDuration;
    private int mDefaultAnimDuration = 5;
    //小球序号
    private String mBallText;
    private String mDefaultBallText = "1";
    //初始颜色
    private String mBallStartColor;
    private String mDefaultBallStartColor = "#0000FF";
    //结束颜色
    private String mBallEndColor;
    private String mDefaultBallEndColor = "#FF0000";

    public AnimPointView(Context context) {
        super(context);
        init();
    }

    public AnimPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ball);
        mAnimDuration = typedArray.getInt(R.styleable.Ball_anim_duration, mDefaultAnimDuration);
        mBallText = typedArray.getString(R.styleable.Ball_ball_text);
        mBallStartColor = typedArray.getString(R.styleable.Ball_start_color);
        mBallEndColor = typedArray.getString(R.styleable.Ball_end_color);
        if (TextUtils.isEmpty(mBallText)) {
            mBallText = mDefaultBallText;
        }
        if (TextUtils.isEmpty(mBallStartColor)) {
            mBallStartColor = mDefaultBallStartColor;
        }
        if (TextUtils.isEmpty(mBallEndColor)) {
            mBallEndColor = mDefaultBallEndColor;
        }
        //回收typedArray
        typedArray.recycle();
        init();
    }

    public AnimPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //画圆的画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        //画文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(sRADIUS);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentPoint == null) {
            mCurrentPoint = new Point((int) sRADIUS, (int) sRADIUS);
            drawCircle(canvas);
            startAnimation();
        } else {
            drawCircle(canvas);
        }
    }

    //绘制圆球
    private void drawCircle(Canvas canvas) {
        float x = mCurrentPoint.x;
        float y = mCurrentPoint.y;
        canvas.drawCircle(x, y, sRADIUS, mPaint);
        canvas.drawText(mBallText, x, y + 5, mTextPaint);
    }

    // 调用了invalidate()方法，这样的话 onDraw()方法就会重新调用，并且由于currentPoint 对象的坐标已经改变了，
    // 那么绘制的位置也会改变，于是一个平移的动画效果也就实现了；
    private void startAnimation() {
        //改变小球的位置
        Point startPoint = new Point(getWidth() / 2, (int) sRADIUS);
        Point endPoint = new Point(getWidth() / 2, getHeight() - (int) sRADIUS);
        Log.i("TEST", "startPoint:" + startPoint.x + "-" + startPoint.y);
        Log.i("TEST", "endPoint:" + endPoint.x + "-" + endPoint.y);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        //动画监听事件，不断重绘view
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPoint = (Point) animation.getAnimatedValue();
                //invalidate() 与 requestLayout()的区别，这个地方也可以用requestLayout()；
                invalidate();
            }
        });
        //设置动画的弹跳差值器
        anim.setInterpolator(new BounceInterpolator());
        //改变小球的颜色
        ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(), mBallStartColor, mBallEndColor);
        //组合动画
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(anim2);
        animSet.setDuration(mAnimDuration * 1000);
        animSet.start();
    }

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public class PointEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //fraction 与时间有关的系数，该值由差值器计算得出，由ValueAnimator调用 animateValue
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
            float y = startPoint.y + fraction * (endPoint.y - startPoint.y);
            return new Point((int) x, (int) y);
        }
    }

    public class ColorEvaluator implements TypeEvaluator {
        //将十六进制的颜色表示切割成三段，分别为红色段、绿色段、蓝色段，分别计算其随时间改变而对应的值；
        private int mCurrentRed = -1;
        private int mCurrentGreen = -1;
        private int mCurrentBlue = -1;

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            String startColor = (String) startValue;
            String endColor = (String) endValue;
            // Integer.parseInt(String s ,int radix)方法: 输出一个十进制数; radix 表示原来的进制；
            int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
            int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
            int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
            int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
            int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
            int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);
            // 初始化颜色的值
            if (mCurrentRed == -1) {
                mCurrentRed = startRed;
            }
            if (mCurrentGreen == -1) {
                mCurrentGreen = startGreen;
            }
            if (mCurrentBlue == -1) {
                mCurrentBlue = startBlue;
            } // 计算初始颜色和结束颜色之间的差值
            int redDiff = Math.abs(startRed - endRed);
            int greenDiff = Math.abs(startGreen - endGreen);
            int blueDiff = Math.abs(startBlue - endBlue);
            int colorDiff = redDiff + greenDiff + blueDiff;
            if (mCurrentRed != endRed) {
                mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0, fraction);
            } else if (mCurrentGreen != endGreen) {
                mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff, redDiff, fraction);
            } else if (mCurrentBlue != endBlue) {
                mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff, redDiff + greenDiff, fraction);
            }
            // 将计算出的当前颜色的值组装返回
            String currentColor = "#" + getHexString(mCurrentRed) + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
            return currentColor;
        }

        /**
         * 根据fraction 值来计算当前的颜色。
         */
        private int getCurrentColor(int startColor, int endColor, int colorDiff, int offset, float fraction) {
            int currentColor;
            if (startColor > endColor) {
                currentColor = (int) (startColor - (fraction * colorDiff - offset));
                if (currentColor < endColor) {
                    currentColor = endColor;
                }
            } else {
                currentColor = (int) (startColor + (fraction * colorDiff - offset));
                if (currentColor > endColor) {
                    currentColor = endColor;
                }
            }
            return currentColor;
        }

        /**
         * 将10进制颜色值转换成16进制。
         */
        private String getHexString(int value) {
            String hexString = Integer.toHexString(value);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            return hexString;
        }
    }
}

