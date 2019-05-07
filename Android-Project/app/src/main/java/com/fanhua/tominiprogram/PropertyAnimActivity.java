package com.fanhua.tominiprogram;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

public class PropertyAnimActivity extends AppCompatActivity {

    LinearLayout llhidv;

    int hidviewHeight;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim);
        llhidv = (LinearLayout) findViewById(R.id.llhiddview);
        btn = findViewById(R.id.btn_demo);

        float disn = getResources().getDisplayMetrics().density;
        hidviewHeight = (int) (disn * 40 + 0.5);
        Log.e("lc---", hidviewHeight + "hidviewHeight");
    }

    public void llClick(View view) {
        if (llhidv.getVisibility() == View.GONE) {
            Log.e("lc---", llhidv.getVisibility() + "GONE");
            open(llhidv);
        } else {
            Log.e("lc---", llhidv.getVisibility() + "fei gone");
            close(llhidv);
        }
    }

    public void demoClick(View view){
        selfAnim();
    }

    /**
     * 关闭--动画
     *
     * @param view
     */
    private void close(final View view) {
        int origheight = view.getHeight();
        Log.e("lc---", origheight + "origheight");
        ValueAnimator animator = createDropAnimator(view, origheight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                Log.e("lc---", "" + "close");
            }
        });
        animator.start();
    }

    /**
     * 打开--动画
     *
     * @param view
     */
    private void open(View view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0, hidviewHeight);
        Log.e("lc---", hidviewHeight + "hidviewHeight---open");
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //   int value = (Integer)valueAnimator.getAnimatedValue();
                int value = (Integer) valueAnimator.getAnimatedValue();// 得到的值
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                //layoutParams.height = Float.floatToIntBits(value);
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    public void selfAnim(){
        btn.animate()
                .translationY(200)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(1000)
                .start();
    }

}
