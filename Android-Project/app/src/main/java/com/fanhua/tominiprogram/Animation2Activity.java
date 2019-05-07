package com.fanhua.tominiprogram;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fanhua.tominiprogram.anim.HiddenAnimUtils;
import com.fanhua.tominiprogram.anim.SwingAnimation;
import com.fanhua.uiadapter.utils.DensityUtils;

public class Animation2Activity extends Activity {

    /**
     * 摇摆的布局
     */
    private LinearLayout ll_anim;

    /**
     * 隐藏和显示的线条
     */
    private View view_line;

    /**
     * 底部图标
     */
    private ImageView iv_icon;

    /**
     * 显示和隐藏view_line的动画
     */
    private HiddenAnimUtils hiddenAnimUtils;

    /**
     * 摇摆动画
     */
    private SwingAnimation swingAnimation;

    /**
     * 消息通知
     */
    private TimeHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation2);
        initView();
        //第一种(帧动画摇摆、属性动画拉伸收缩,通过handler在两种动画之间选择)
//        initAnim();
//        initAnimLife();
        //第二种(属性动画摇摆和拉伸收缩)
        yB();

        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Animation2Activity.this, "点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initView(){
        ll_anim = findViewById(R.id.ll_anim);
        view_line = findViewById(R.id.view_line);
        iv_icon = findViewById(R.id.iv_icon);
    }

    public void initAnim(){
        hiddenAnimUtils = HiddenAnimUtils.newInstance(this, view_line, 80);

        //摇摆动画
        swingAnimation = new SwingAnimation(
                0f, 30f, -30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        swingAnimation.setDuration(3000);//动画持续时间
        swingAnimation.setInterpolator(new LinearInterpolator());
        swingAnimation.setRepeatCount(Animation.INFINITE);
        swingAnimation.setRepeatMode(Animation.RESTART);
        ll_anim.startAnimation(swingAnimation);
    }

    public void initAnimLife(){
        handler = new TimeHandler(hiddenAnimUtils,swingAnimation,view_line,ll_anim);

        view_line.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 6000);
    }

    static class TimeHandler extends Handler{

        private HiddenAnimUtils hiddenAnimUtils;
        private SwingAnimation swingAnimation;
        private View view_line;
        private LinearLayout ll_anim;

        public TimeHandler(HiddenAnimUtils hiddenAnimUtils,SwingAnimation swingAnimation,View view_line,LinearLayout ll_anim){
            this.hiddenAnimUtils = hiddenAnimUtils;
            this.swingAnimation = swingAnimation;
            this.view_line = view_line;
            this.ll_anim = ll_anim;
        }

        @Override
        public void handleMessage(final Message msg) {
            final int what = msg.what;//0展开,1关闭
            if(what == 0){//展开时先暂停摇摆动画
                swingAnimation.cancel();
                ll_anim.clearAnimation();
            }
            hiddenAnimUtils.toggle();
            hiddenAnimUtils.setOnAnimationEnd(new HiddenAnimUtils.OnAnimationEnd() {
                @Override
                public void end() {
                    if(what == 0){
                        sendEmptyMessageDelayed(1,3000);
                    }else {
                        ll_anim.startAnimation(swingAnimation);
                        sendEmptyMessageDelayed(0, 6000);
                    }
                }

                @Override
                public void delay() {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        handler = null;
        super.onDestroy();
    }

    /**
     * 属性动画实现摇摆
     */
    private int repeat = 0;

    /**
     * 属性动画实现摇摆和伸缩
     */
    public void yB(){
        final HiddenAnimUtils animUtils = HiddenAnimUtils.newInstance(this, view_line, 80);
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_anim,
                "rotation"
                , 0, 30, 0.0f, -30,0.0f);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(-1);
        ll_anim.setPivotY(0);
        ll_anim.setPivotX(DensityUtils.dp2px(this,55)/2);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                repeat = 0;
                animUtils.toggle();
                animUtils.setOnAnimationEnd(new HiddenAnimUtils.OnAnimationEnd() {
                    @Override
                    public void end() {
                        if(view_line.getVisibility() == View.GONE){
                            objectAnimator.start();
                        }
                    }

                    @Override
                    public void delay() {
                        animUtils.toggle();
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                repeat++;
                if(repeat == 2){
                    objectAnimator.cancel();//会调用onAnimationEnd
                }
            }
        });
        objectAnimator.start();
    }
}
