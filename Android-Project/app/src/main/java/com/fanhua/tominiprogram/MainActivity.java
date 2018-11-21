package com.fanhua.tominiprogram;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

        /**
         * 注册应用
         * 填应用AppId  本应用secret:bb499a48938acdc04b0fd49e50086436  微信开发平台注册审核通过后，才能配置小程序信息
         */
        String appId = "wx52b2de8d97070688";
        final IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, appId);
        api.registerApp(appId);

        TextView viewById = findViewById(R.id.tv_open);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();

                //// 小程序原始id
                req.userName = "gh_1fd06885a21d";

                //小程序路径,不填默认为小程序主页
                req.path = "pages/ad/index?xsl_third_appid=wxe57bfaaa884daed6&xsl_third_path=%3Ffrom%3D1264";

                // 可选打开 开发版，体验版和正式版
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;

                api.sendReq(req);
            }
        });

        /**
         * resolutionAdapter在baseActivity中已经初始化
         */

        /**
         * 字体大小适配
         */
        resolutionAdapter.setTextSize(viewById, 28);

        /**
         * view适配
         */
        resolutionAdapter.setup(viewById, 500, 120, ViewScaleType.AS_TWO_EDGES_SCALE);

        getAllApps();
    }

    private void getAllApps() {
        Log.d("yxs","获取所有非系统应用");
        String result = "";
        PackageManager pManager = getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                if (TextUtils.isEmpty(result)) {
                    result = pak.applicationInfo.loadLabel(pManager).toString();
                } else {
                    result = result + "," + pak.applicationInfo.loadLabel(pManager).toString();
                }
            }
        }
        Log.d("yxs","设备安装非系统应用信息：" + result);
    }

}
