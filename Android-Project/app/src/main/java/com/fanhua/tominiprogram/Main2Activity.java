package com.fanhua.tominiprogram;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.fanhua.tominiprogram.demo.encry.aes.AesException;
import com.fanhua.tominiprogram.demo.encry.aes.EncryAndroid;
import com.fanhua.tominiprogram.demo.encry.sgin.SignUtil;
import com.fanhua.uiadapter.http.BaseInterceptor;
import com.fanhua.uiadapter.http.GsonUtils;
import com.fanhua.uiadapter.http.HttpClient;
import com.fanhua.uiadapter.http.api.BaseHttpListener;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 网络请求demo(加密请忽略),请确保在application中配置BaseUrl:  HttpClient.setBaseUrl("");
 */
public class Main2Activity extends BaseActivity{

    /**
     * 主体内容
     */
    private final static String ENCRYPTSTR = "encryptstr";

    /**
     * 时间戳
     */
    private final static String TIMESTAMP = "timestamp";

    /**
     * 随机码
     */
    private final static String ECHOSTR = "echostr";

    /**
     * 根绝指定规则对params参数集加密
     */
    private final static String SIGN = "sign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Map<String, String> params = new HashMap<>();
        params.put("appKey", "121bcdaadcc74a959c5402a1825ef74f");
        params.put("size", 10 + "");
        params.put("page", 1 + "");
        params.put("channelId", "fangyuan46");

        try {
            String encryptstr = EncryAndroid.encrypt(GsonUtils.toJson(params));

            String timestamp = System.currentTimeMillis() + "";
            String echostr = createRandom(false, 32);
            SortedMap<String, String> signMap = new TreeMap<>();
            signMap.put(ECHOSTR, echostr);
            signMap.put(ENCRYPTSTR, encryptstr);
            signMap.put(TIMESTAMP, timestamp);
            String sign = SignUtil.createSign(signMap, "7908b2179af04e1099877643ad7c83a2");
            signMap.put(SIGN, sign);

            HttpClient.string("api/ad/list/v2", GsonUtils.toJson(signMap), new BaseHttpListener() {
                @Override
                public void onSucceed(String s, BaseInterceptor baseInterceptor, String s1) {
                    if(s != null){
                        ResponseBean responseBean = GsonUtils.json2Class(s,ResponseBean.class);
                        if(responseBean == null){
                            return;
                        }

                        if(responseBean.getData() == null){
                            return;
                        }

                        String resultJson = getResult(responseBean.getData().getEncryptstr());
                        System.out.println("解密后数据"+resultJson);
                    }
                }

                @Override
                public void onCompleted(String s) {

                }

                @Override
                public void onFailed(Throwable throwable, String s) {

                }
            }, "ad");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        //如果用户没有读取存储的权限,则提示
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected boolean isAutoAdapter() {
        return true;
    }

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
