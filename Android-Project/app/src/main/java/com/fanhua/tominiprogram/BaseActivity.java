package com.fanhua.tominiprogram;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fanhua.uiadapter.LibApplication;
import com.fanhua.uiadapter.ResolutionAdapter;
import com.fanhua.uiadapter.enums.ViewScaleType;

/**
 * 对适配工具的初始化,请先确保继承LibApplication,且在Application中配置基于哪一个尺寸适配的信息
 */
public abstract class BaseActivity extends Activity {

    /**
     * 适配工具,
     */
    protected ResolutionAdapter resolutionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获取适配工具
        resolutionAdapter = ((LibApplication) getApplication()).getResolutionAdapter();
        if(isAutoAdapter()){
            resolutionAdapter.setupAll(this.getContentView(), ViewScaleType.AS_TWO_EDGES_SCALE);
        }
    }

    protected boolean isAutoAdapter(){
        return false;
    }

    public View getContentView(){
        return this.findViewById(android.R.id.content);
    }
}
