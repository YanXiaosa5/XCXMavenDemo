package com.fanhua.tominiprogram;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fanhua.tominiprogram.ai.FaceAiDemo;
import com.fanhua.uiadapter.enums.ViewScaleType;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 打开第三方小程序,适配测试
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = "wx52b2de8d97070688"; // 填应用AppId  本应用secret:bb499a48938acdc04b0fd49e50086436
        final IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, appId);
        api.registerApp(appId);

        TextView viewById = findViewById(R.id.tv_open);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_1fd06885a21d"; // 小程序原始id
                req.path = "pages/ad/index?xsl_third_appid=wxe57bfaaa884daed6&xsl_third_path=%3Ffrom%3D1264";
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                api.sendReq(req);
            }
        });

        //resolutionAdapter在baseActivity中已经初始化
        //字体大小适配
//        resolutionAdapter.setTextSize(viewById, 28);

        //view适配
//        resolutionAdapter.setup(viewById,500,120,ViewScaleType.AS_TWO_EDGES_SCALE);

    }

}
