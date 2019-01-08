package com.fanhua.tominiprogram;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.fanhua.tominiprogram.anim.SwingAnimation;
import com.fanhua.uiadapter.ScreenUtils;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class AnimationActivity extends AppCompatActivity {

    private View viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        viewById = findViewById(R.id.iv_swing);
        //摇摆动画使用
        //参数取值说明：中间度数、摆到左侧的度数、摆到右侧的度数、圆心X坐标类型、圆心X坐标相对比例、圆心Y坐标类型、圆心Y坐标相对比例
        //坐标类型有三种：ABSOLUTE 绝对坐标，RELATIVE_TO_SELF 相对自身的坐标，RELATIVE_TO_PARENT 相对上级视图的坐标
        //X坐标相对比例，为0时表示左边顶点，为1表示右边顶点，为0.5表示水平中心点
        //Y坐标相对比例，为0时表示上边顶点，为1表示下边顶点，为0.5表示垂直中心点

        final SwingAnimation swingAnimation = new SwingAnimation(
                0f, 30f, -30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        swingAnimation.setDuration(3000);//动画持续时间
        swingAnimation.setInterpolator(new LinearInterpolator());
        swingAnimation.setRepeatCount(Animation.INFINITE);
        swingAnimation.setRepeatMode(Animation.RESTART);
        viewById.startAnimation(swingAnimation);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimationActivity.this, "点击了"+v.getX()+"==="+v.getY(), Toast.LENGTH_SHORT).show();
            }
        });

        //位移动画
        TranslateAnimation animation = new TranslateAnimation(viewById.getX(),0.5f, viewById.getY(),0.6f);
        animation.setDuration(2000);
        animation.setInterpolator(new BounceInterpolator());
        animation.setFillAfter(true);

        //属性动画实现位移
        final ObjectAnimator translationX = ObjectAnimator.ofFloat(viewById, "translationY", viewById.getY(), ScreenUtils.getScreenHeight(AnimationActivity.this)/10*6);
        translationX.setInterpolator(new BounceInterpolator());
        translationX.setDuration(1500);

        viewById.postDelayed(new Runnable() {
            @Override
            public void run() {
                swingAnimation.cancel();
                translationX.start();
            }
        },6000);


        //放大效果
//        ViewUtils.setViewMargin(viewById, 0, 100, 0, 0);
//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(viewById, "scaleX", 1.0f, 1.5f);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(viewById, "scaleY", 1.0f, 1.5f);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setInterpolator(new SpringScaleInterpolator(0.3f));//值越小弹性效果越明显
//        animatorSet.setDuration(2000);
//        animatorSet.playTogether(animatorX, animatorY);
//        animatorSet.start();

        //放大缩小效果
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f,1.0f,0.6f,1.0f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,1.0f);
//        scaleAnimation.setDuration(3000);
//        scaleAnimation.setRepeatMode(ScaleAnimation.REVERSE);
//        scaleAnimation.setRepeatCount(-1);

        //一边摇摆，一边放大和缩小
//        AnimationSet animationSet = new AnimationSet(false);
//        animationSet.addAnimation(swingAnimation);
//        animationSet.addAnimation(scaleAnimation);
//        viewById.startAnimation(animationSet);

    }

    //结果性的动画
    public class SpringScaleInterpolator implements Interpolator {
        private float factor;

        public SpringScaleInterpolator(float factor) {
            this.factor = factor;
        }

        @Override
        public float getInterpolation(float x) {
            return (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1);
        }
    }

}
