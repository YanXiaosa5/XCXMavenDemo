package com.fanhua.tominiprogram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fanhua.tominiprogram.fragment.BottomMenu2;

/**
 * 模仿抖音评论布局
 */
public class BottomSheetActivity extends AppCompatActivity {

    BottomSheetBehavior behavior;

    private BottomMenu2 bottomMenu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        final View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomMenu2 = new BottomMenu2();
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变
                System.out.println("BottomSheetActivity.onStateChanged"+newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
                System.out.println("BottomSheetActivity.onSlide"+slideOffset);
            }
        });

        findViewById(R.id.btn_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new BottomMenu().show(getSupportFragmentManager(), "dialog");
                setBundle("1");
                bottomMenu2.show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_bottom2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBundle("2");
                bottomMenu2.show(getSupportFragmentManager(), "dialog2");
            }
        });

        findViewById(R.id.btn_bottom3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setBundle(String id){
        Bundle arguments = bottomMenu2.getArguments();
        if(arguments == null){
            arguments = new Bundle();
        }
        arguments.putString("id",id);
        bottomMenu2.setArguments(arguments);
    }
}
