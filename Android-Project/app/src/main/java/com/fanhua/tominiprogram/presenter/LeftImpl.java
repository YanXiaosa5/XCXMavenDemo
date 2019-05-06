package com.fanhua.tominiprogram.presenter;

import android.app.Activity;

import com.fanhua.tominiprogram.demo.encry.aes.AesException;
import com.fanhua.tominiprogram.demo.encry.aes.EncryAndroid;
import com.fanhua.tominiprogram.demo.encry.sgin.SignUtil;
import com.fanhua.uiadapter.http.GsonUtils;
import com.fanhua.uiadapter.http.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class LeftImpl extends OnBaseHttpListenerImpl implements LeftPresenter {

    public LeftImpl(Activity activity, OnBaseHttpPresenter baseHttpPresenter) {
        super(activity, baseHttpPresenter);
    }

    @Override
    public void getDataList(int size, int page, String requestTag) {
        Map<String, String> params = new HashMap<>();
        params.put("appKey", APP_KEY);
        params.put("size", size + "");
        params.put("page", page + "");
        params.put("channelId", "fangyuan46");
        try {
            //参数加密
            String encryptstr = EncryAndroid.encrypt(GsonUtils.toJson(params));
            //时间戳
            String timestamp = System.currentTimeMillis() + "";
            //随机码
            String echostr = createRandom(false, 32);

            //对新的参数集再次加密
            SortedMap<String, String> signMap = new TreeMap<>();
            signMap.put(ECHOSTR, echostr);
            signMap.put(ENCRYPTSTR, encryptstr);
            signMap.put(TIMESTAMP, timestamp);
            String sign = SignUtil.createSign(signMap, APP_SECRET);
            signMap.put(SIGN, sign);
            HttpClient.string("api/ad/list/v9",GsonUtils.toJson(signMap),this,requestTag);
        } catch (AesException e) {
            e.printStackTrace();
        }
    }
}
