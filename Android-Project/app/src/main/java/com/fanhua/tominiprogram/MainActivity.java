package com.fanhua.tominiprogram;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fanhua.tominiprogram.demo.encry.aes.AesException;
import com.fanhua.tominiprogram.demo.encry.aes.EncryAndroid;
import com.fanhua.tominiprogram.demo.encry.sgin.SignUtil;
import com.fanhua.uiadapter.enums.ViewScaleType;
import com.fanhua.uiadapter.http.BaseInterceptor;
import com.fanhua.uiadapter.http.GsonUtils;
import com.fanhua.uiadapter.http.HttpClient;
import com.fanhua.uiadapter.http.api.BaseHttpListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 打开第三方小程序,适配测试,网络请求与加密测试
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
        ListView lv_list = findViewById(R.id.lv_list);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_1fd06885a21d"; // 小程序原始id
//                req.path = "pages/ad/index?xsl_third_appid=wxe57bfaaa884daed6&xsl_third_path=%3Ffrom%3D1264";
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                api.sendReq(req);
                Intent intent = getIntent();
                intent.setClass(MainActivity.this, Main2Activity.class);
                intent.putExtra("ss", "这是数据");
                startActivityForResult(intent, 1);
            }
        });

        //适配textView的字体大小
//        resolutionAdapter.setTextSize(viewById, 28);

        //view适配
        resolutionAdapter.setup(viewById,500,120,ViewScaleType.AS_TWO_EDGES_SCALE);

        List<String> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            data.add("这是第" + i + "条数据");
        }
        //adapter中的字体大小和view高度是配
        lv_list.setAdapter(new MyAdapter(data, this));


//        Map<String, String> params = new HashMap<>();
//        params.put("appKey", "121bcdaadcc74a959c5402a1825ef74f");
//        params.put("size", 10 + "");
//        params.put("page", 1 + "");
//
//        try {
//            String encryptstr = EncryAndroid.encrypt(GsonUtils.toJson(params));
//
//            String timestamp = System.currentTimeMillis() + "";
//            String echostr = createRandom(false, 32);
//            SortedMap<String, String> signMap = new TreeMap<>();
//            signMap.put(ECHOSTR, echostr);
//            signMap.put(ENCRYPTSTR, encryptstr);
//            signMap.put(TIMESTAMP, timestamp);
//            String sign = SignUtil.createSign(signMap, "7908b2179af04e1099877643ad7c83a2");
//            signMap.put(SIGN, sign);
//
//            HttpClient.string("", GsonUtils.toJson(signMap), new BaseHttpListener() {
//                @Override
//                public void onSucceed(String s, BaseInterceptor baseInterceptor, String s1) {
//                    if(s != null){
//                        ResponseBean responseBean = GsonUtils.json2Class(s,ResponseBean.class);
//                        if(responseBean == null){
//                            return;
//                        }
//
//                        if(responseBean.getData() == null){
//                            return;
//                        }
//
//                        String resultJson = getResult(responseBean.getData().getEncryptstr());
//                        System.out.println("解密后数据"+resultJson);
//                    }
//                }
//
//                @Override
//                public void onCompleted(String s) {
//
//                }
//
//                @Override
//                public void onFailed(Throwable throwable, String s) {
//
//                }
//            }, "ad");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected boolean isAutoAdapter() {
        return false;
    }

    private final static String ENCRYPTSTR = "encryptstr";
    private final static String TIMESTAMP = "timestamp";
    private final static String ECHOSTR = "echostr";
    private final static String SIGN = "sign";

    /**
     * 解密结果
     * @param data
     * @return
     */
    private String getResult(String data) {
        String result = "";
        if (data != null && data.length() > 0 && !data.equals("null")) {
            try {
                result = EncryAndroid.decrypt(data);
            } catch (AesException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

}
