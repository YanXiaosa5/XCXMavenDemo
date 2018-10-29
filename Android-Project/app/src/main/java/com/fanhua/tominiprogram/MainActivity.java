package com.fanhua.tominiprogram;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 打开第三方小程序
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appId = "wx52b2de8d97070688"; // 填应用AppId  本应用secret:bb499a48938acdc04b0fd49e50086436
        final IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, appId);
        api.registerApp(appId);

        findViewById(R.id.tv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_1fd06885a21d"; // 小程序原始id
//                req.path = "pages/ad/index?xsl_third_appid=wxe57bfaaa884daed6&xsl_third_path=%3Ffrom%3D1264";
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                api.sendReq(req);
                Intent intent = getIntent();
                intent.setClass(MainActivity.this, Main2Activity.class);
                intent.putExtra("ss","这是数据");
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("1-onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("1 - onSaveInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("1onResume");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("1走了onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("1走了OnPause");
    }

}
