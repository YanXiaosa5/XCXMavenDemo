package com.fanhua.tominiprogram;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import java.io.File;

import okhttp3.Call;

public class Main3Activity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,View.OnClickListener, TextureView.SurfaceTextureListener{

    private WebView mWebView;
    private Button btn_controller;
    private Button btn_jump;
    private DragButton db_layout;
    private TextureView texture_ad;
    private VideoPlayer mVideoPlayer;

    private boolean mStarted;

    private ImageView iv_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mWebView = findViewById(R.id.webSite);
        db_layout = findViewById(R.id.db_layout);
        texture_ad = findViewById(R.id.texture_ad);
        initDown();
        //核心代码
        mWebView = (WebView) this.findViewById(R.id.webSite);
        //启动缓存
        mWebView.getSettings().setAppCacheEnabled(true);
        //设置缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //设置js可以直接打开窗口，如window.open()，默认为false
        mWebView.getSettings().setJavaScriptEnabled(true);
        //是否允许执行js，默认为false。
        // 设置true时，会提醒可能造成XSS漏洞
        mWebView.getSettings().setSupportZoom(true);
        //是否可以缩放，默认true
        mWebView.getSettings().setBuiltInZoomControls(true);
        //是否显示缩放按钮，默认false
        mWebView.getSettings().setUseWideViewPort(true);
        //设置此属性，可任意比例缩放。大视图模式
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //和setUseWideViewPort(true)一起解决网页自适应问题
        mWebView.getSettings().setDomStorageEnabled(true);
        //DOM Storage //在webview内部跳转web页面
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl("https://www.baidu.com/");
    }

    private void initDown() {
        btn_controller = findViewById(R.id.btn_controller);
        btn_jump = findViewById(R.id.btn_jump);
        iv_clear = findViewById(R.id.iv_clear);
        btn_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownLoad();
            }
        });

        btn_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onClick"+mVideoPlayer.hashCode());
                if(mVideoPlayer.isPlaying()) {
                    mVideoPlayer.pause();
                }else{
                    mVideoPlayer.start();
                }
            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlayer.pause();
            }
        });

        initVideoPlayer();
        texture_ad.setSurfaceTextureListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mVideoPlayer.start();
        System.out.println("走了onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("走了onStop");
    }

    private void initVideoPlayer() {
        mVideoPlayer = new VideoPlayer(this);
        System.out.println("player"+mVideoPlayer.hashCode());
        mVideoPlayer.setOnPreparedListener(this);
        mVideoPlayer.setOnCompletionListener(this);
        mVideoPlayer.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mVideoPlayer.setLooping(true);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        System.out.println("onSurfaceTextureAvailable");
        mVideoPlayer.setSurface(new Surface(surface));
        if (!mStarted) {
            mStarted = true;
            mVideoPlayer.setVideoUri(Uri.parse("https://static.nbchhf.com/apk/qxx_v11_21.mp4"));
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mVideoPlayer.pause();
        mVideoPlayer.setSurface(null);
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoPlayer.removeAllListeners();
        mVideoPlayer.stopPlayback();
        mVideoPlayer = null;
    }


    public void startDownLoad() {

        String path = "/storage/emulated/0/hahaha.apk";

        File file = new File(path);
        if (file.exists()) {
            try {
                String chmodCmd = "chmod 666 " + file.getAbsolutePath();
                Runtime.getRuntime().exec(chmodCmd);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }
}
