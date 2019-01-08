package com.fanhua.tominiprogram;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fanhua.tominiprogram.ai.GsonUtils;
import com.fanhua.tominiprogram.demo.encry.sgin.MD5Util;
import com.fanhua.tominiprogram.signature.SignaUtils;

public class WebViewActivity extends BaseActivity {

    WebView mWebview;
    WebSettings mWebSettings;

    private String mCurrentUrl;

    private boolean isSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        setContentView(R.layout.activity_web_view);
        testMethod();
        mWebview = (WebView) findViewById(R.id.webview);
        mWebSettings = mWebview.getSettings();
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setSupportZoom(true);//是否可以缩放，默认true
        mWebSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mWebSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
        mWebSettings.setDomStorageEnabled(true);//开启本地DOM存储
        mWebSettings.setLoadsImagesAutomatically(true); // 加载图片
        mWebSettings.setMediaPlaybackRequiresUserGesture(false);//播放音频，多媒体需要用户手动？设置为false为可自动播放

        mWebview.loadUrl("https://v.6.cn");
        //设置不用系统浏览器打开,直接显示在当前Webview
        //https://www.cnblogs.com/zimengfang/p/6183869.html   解决重定向问题
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                System.out.println("地址" + url);
                if(!isSave) {
                    mCurrentUrl = url;
                    isSave = true;
                }
                //http://m.v.6.cn/?referrer=
                if(url.equals(mCurrentUrl) && hitTestResult == null) {
                    view.loadUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }

    public void testMethod(){
        Signature[] signature = SignaUtils.getSignature(this, "com.fanhua.fhbox");
        for (int i = 0; i < signature.length; i++) {
            Signature signature1 = signature[i];
            System.out.println("这是第"+i+"个签名"+ GsonUtils.toJson(signature1));
        }
    }

    //https://blog.csdn.net/mockingbirds/article/details/44903155     getPackageArchiveInfo得到未安装apk的信息

    //得到PackageInfo对象，其中包含了该apk包含的activity和service
    public  PackageInfo getPackageInfo(String apkFilepath) {
        PackageManager pm = getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_SIGNATURES);
        } catch (Exception e) {
            // should be something wrong with parse
            e.printStackTrace();
        }
        return pkgInfo;
    }
}
