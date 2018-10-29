package com.fanhua.uiadapter.api;

import android.view.View;
import android.widget.TextView;

import com.fanhua.uiadapter.enums.ViewScaleType;


/**
 * Created by misl on 2017/3/23.
 */

public interface IResolutionAdapter {

    void setupAll(View view, ViewScaleType scaleType);

    void setup(View view, ViewScaleType scaleType);

    void setup(View view, float w, float h, ViewScaleType scaleType);

    void setup(View view, float x, float y, float w, float h, ViewScaleType scaleType);

    void setup(View view, float x, float y, float w, float h, float wordSize, ViewScaleType scaleType);

    void setWidth(View view, ViewScaleType scaleType);

    void setWidth(View view, float w, ViewScaleType scaleType);

    void setHeight(View view, ViewScaleType scaleType);

    void setHeight(View view, float h, ViewScaleType scaleType);

    void setPadding(View view, ViewScaleType scaleType);

    void setMargin(View view, ViewScaleType scaleType);

    void setMargin(View view, float l, float t, float r, float b, ViewScaleType scaleType);

    void setPadding(View view, float l, float t, float r, float b, ViewScaleType scaleType);

    void setTextSize(TextView view, float size);

    int getTxtSizePx(float size);

    int getTxtSizeSp(float size);

}
