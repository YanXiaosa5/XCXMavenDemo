package com.fanhua.uiadapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanhua.uiadapter.api.IResolutionAdapter;
import com.fanhua.uiadapter.enums.ViewScaleType;
import com.fanhua.uiadapter.utils.DensityUtils;
import com.fanhua.uiadapter.utils.Size;
import com.fanhua.uiadapter.utils.ViewUtils;


/**
 * Created by misl on 2017/3/22.
 */
public class ResolutionAdapter implements IResolutionAdapter {

    /**
     * 上下文环境
     */
    private Context context;

    /**
     * 设计稿的分辨率
     */
    private Resolution designResolution;

    /**
     * 当前视图分辨率
     */
    private Resolution curResolution;

    /**
     * 单例自己
     */
    private static ResolutionAdapter self;

    /**
     * 实例化
     *
     * @return
     */
    public static ResolutionAdapter getInstance() throws Exception {
        synchronized (ResolutionAdapter.class) {
            if (self == null) {
                self = new ResolutionAdapter();
            }
        }
        return self;
    }

    private ResolutionAdapter() {

    }

    public void setDesignResolution(Context context, Resolution designResolution) throws Exception {
        this.context = context;
        this.designResolution = designResolution;

        init();
    }

    private void init() throws Exception {
        if (null == context) {
            throw new Exception("上下文环境设置错误！");
        }

        if (null == designResolution) {
            throw new Exception("请先设置设计稿的屏幕分辨率！");
        }

        curResolution = Resolution.getResolution(context);

        if (null == designResolution) {
            throw new Exception("获取当前屏幕分辨率失败！");
        }
    }

    @Override
    public void setupAll(View view, ViewScaleType scaleType) {

        if (null == view || scaleType.getTag() == -1) {
            L.d("视图不存在");
            return;
        }

        // 设置视图适配
        setup(view, scaleType);

        // padding
        setPadding(view, scaleType);
        // margin
        setMargin(view, scaleType);

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            setTextSize(textView, textView.getTextSize());
        }

        if (view instanceof ViewGroup) {

            int count = ((ViewGroup) view).getChildCount();

            if (count > 0) {
                for (int i = 0; i < count; ++i) {
                    setupAll(((ViewGroup) view).getChildAt(i), scaleType);
                }
            }
        }
    }

    public float getScaleW() {
        return (float) curResolution.getWidth() / (float) designResolution.getWidth();
    }

    public float getScaleH() {
        return (float) curResolution.getHeight() / (float) designResolution.getHeight();
    }

    @Override
    public void setup(View view, ViewScaleType scaleType) {
        setup(view, view.getLayoutParams().width, view.getLayoutParams().height, scaleType);
    }

    @Override
    public void setup(View view, float w, float h, ViewScaleType scaleType) {

        // 先判断并切换屏幕方向 - 看是否有必要
        int curOrientation = ScreenUtils.getOrientation(view.getContext());
        if (designResolution.getScreenOrientation() != curOrientation) {
            // 切换一下方向
            designResolution.switchOrientation(curOrientation);
        }

        Size size = getScaledSize(w, h, scaleType);

        ViewUtils.setViewSize(view, size.w, size.h);

        view.requestLayout();
    }

    public Size getScaledSize(float w, float h, ViewScaleType scaleType) {
        Size temp = new Size(0, 0);
        float scaleW = getScaleW();
        float scaleH = getScaleH();
        float minScale = scaleW > scaleH ? scaleH : scaleW;
        float maxScale = scaleW < scaleH ? scaleH : scaleW;

        switch (scaleType) {
            case NO_SCALE:
                break;
            case AS_MAX_EDGES_SCALE:
                temp.w = w * maxScale;
                temp.h = h * maxScale;
                break;
            case AS_MIN_EDGES_SCALE:
                temp.w = w * minScale;
                temp.h = h * minScale;
                break;
            case AS_TWO_EDGES_SCALE:
                temp.w = w * scaleW;
                temp.h = h * scaleH;
                break;
            case AS_WIDTH_EDGES_SCALE:
                temp.w = w * scaleW;
                temp.h = h * scaleW;
                break;
            case AS_HEIGHT_EDGES_SCALE:
                temp.w = w * scaleH;
                temp.h = h * scaleH;
                break;
//            case FIX_WIDTH:
//
//                break;
//            case FIX_HEIGHT:
//
//                break;
//            case FIX_WIDTH_HEIGHT:
//
//                break;
//            case FIX_LEFT_SPACE:
//
//                break;
        }

        float onePx = DensityUtils.px2dp(context, 1.0f);

        if (w > 0 && w <= 1) {
            temp.w = DensityUtils.dp2px(context, onePx);
        }
        if (h > 0 && h <= 1) {
            temp.h = DensityUtils.dp2px(context, onePx);
        }
        return temp;
    }

    public float getScaledW(float w, ViewScaleType scaleType) {
        Size size = getScaledSize(w, -1, scaleType);
        return size.w;
    }

    public float getScaledH(float h, ViewScaleType scaleType) {
        Size size = getScaledSize(-1, h, scaleType);
        return size.h;
    }

    @Override
    public void setup(View view, float x, float y, float w, float h, ViewScaleType scaleType) {
        setup(view, x, y, w, h, -1, scaleType);
    }

    @Override
    public void setup(final View view, final float x, final float y, final float w, final float h, final float wordSize, ViewScaleType scaleType) {

        if (ViewScaleType.NO_SCALE == scaleType) return;

        setup(view, w, h, scaleType);

        if (wordSize >= 0
                && view instanceof TextView) {
            setTextSize((TextView) view, wordSize);
        }

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (x >= 0) {
                    view.setX(x);
                }
                if (y >= 0) {
                    view.setY(y);
                }
            }
        }, 20);

    }

    @Override
    public void setWidth(View view, ViewScaleType scaleType) {
        setWidth(view, view.getLayoutParams().width, scaleType);
    }

    @Override
    public void setWidth(View view, float w, ViewScaleType scaleType) {
        setup(view, w, -1, scaleType);
    }

    @Override
    public void setHeight(View view, ViewScaleType scaleType) {
        setHeight(view, view.getLayoutParams().height, scaleType);
    }

    @Override
    public void setHeight(View view, float h, ViewScaleType scaleType) {
        setup(view, -1, h, scaleType);
    }

    @Override
    public void setPadding(View view, ViewScaleType scaleType) {
        setPadding(view, view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom(), scaleType);
    }

    @Override
    public void setPadding(View view, float l, float t, float r, float b, ViewScaleType scaleType) {
        Size xy = getScaledSize(l, t, scaleType);
        Size wh = getScaledSize(r, b, scaleType);
        view.setPadding((int) xy.w, (int) xy.h, (int) wh.w, (int) wh.h);
    }

    @Override
    public void setMargin(View view, ViewScaleType scaleType) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) layoutParams;
            setMargin(view, p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin, scaleType);
        }
    }

    @Override
    public void setMargin(View view, float l, float t, float r, float b, ViewScaleType scaleType) {
        Size xy = getScaledSize(l, t, scaleType);
        Size wh = getScaledSize(r, b, scaleType);
        ViewUtils.setViewMargin(view, (int) xy.w, (int) xy.h, (int) wh.w, (int) wh.h);
    }

    @Override
    public void setTextSize(TextView view, float size) {
        size = size / 2;
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.round(size * curResolution.getDensity()));
    }

    @Override
    public int getTxtSizePx(float size) {
        return Math.round(size * curResolution.getDensity());
    }


    @Override
    public int getTxtSizeSp(float size) {
        return (int) DensityUtils.px2sp(context, getTxtSizePx(size));
    }

}
