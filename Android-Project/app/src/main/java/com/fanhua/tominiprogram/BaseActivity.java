package com.fanhua.tominiprogram;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fanhua.uiadapter.LibApplication;
import com.fanhua.uiadapter.ResolutionAdapter;
import com.fanhua.uiadapter.enums.ViewScaleType;

public abstract class BaseActivity extends Activity {

    protected ResolutionAdapter resolutionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
