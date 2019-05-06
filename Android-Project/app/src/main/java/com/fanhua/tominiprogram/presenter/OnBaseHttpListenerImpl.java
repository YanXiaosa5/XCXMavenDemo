package com.fanhua.tominiprogram.presenter;

import android.app.Activity;

import com.fanhua.tominiprogram.ResponseBean;
import com.fanhua.tominiprogram.demo.encry.aes.AesException;
import com.fanhua.tominiprogram.demo.encry.aes.EncryAndroid;
import com.fanhua.uiadapter.http.BaseInterceptor;
import com.fanhua.uiadapter.http.GsonUtils;
import com.fanhua.uiadapter.http.api.BaseHttpListener;

public class OnBaseHttpListenerImpl implements BaseHttpListener {


    private OnBaseHttpPresenter mOnBaseHttpPresenter;

    protected Activity mActivity;

    /**
     * appkey
     */
    protected static final String APP_KEY = "121bcdaadcc74a959c5402a1825ef74f";

    /**
     * 秘钥
     */
    protected static final String APP_SECRET = "7908b2179af04e1099877643ad7c83a2";

    /**
     * 没数据的标识
     */
    private static final int NO_DATA = 101;

    /**
     * 主体内容
     */
    protected final static String ENCRYPTSTR = "encryptstr";

    /**
     * 时间戳
     */
    protected final static String TIMESTAMP = "timestamp";

    /**
     * 随机码
     */
    protected final static String ECHOSTR = "echostr";

    /**
     * 根绝指定规则对params参数集加密
     */
    protected final static String SIGN = "sign";

    public OnBaseHttpListenerImpl(Activity activity, OnBaseHttpPresenter baseHttpPresenter) {
        this.mOnBaseHttpPresenter = baseHttpPresenter;
        this.mActivity = activity;
    }

    @Override
    public void onSucceed(String s, BaseInterceptor baseInterceptor, String s1) {
        if (s == null) {
            mOnBaseHttpPresenter.onFailed(new Throwable(NO_DATA + ""), s1);
            return;
        }
        ResponseBean responseBean = GsonUtils.json2Class(s, ResponseBean.class);
        if (responseBean == null) {
            mOnBaseHttpPresenter.onFailed(new Throwable(NO_DATA + ""), s1);
            return;
        }

        if (responseBean.getData() == null) {
            mOnBaseHttpPresenter.onFailed(new Throwable(NO_DATA + ""), s1);
            return;
        }

        String resultJson = getResult(responseBean.getData().getEncryptstr());
        mOnBaseHttpPresenter.onSucceed(s, baseInterceptor, s1);

    }

    @Override
    public void onCompleted(String s) {
        mOnBaseHttpPresenter.onCompleted(s);
    }

    @Override
    public void onFailed(Throwable throwable, String s) {
        mOnBaseHttpPresenter.onFailed(throwable, s);
    }

    /**
     * 解密结果
     *
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
