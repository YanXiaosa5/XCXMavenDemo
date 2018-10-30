package com.fanhua.tominiprogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result","结果");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected boolean isAutoAdapter() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("2-onResume");
    }

    @Override
    protected void onPause() {
        System.out.println("2-Main2Activity-onPause");
        super.onPause();
    }
}
