package com.fanhua.uiadapter;

import android.app.Application;
import android.content.pm.ActivityInfo;

import com.fanhua.uiadapter.api.IResolutionListener;
import com.fanhua.uiadapter.utils.ACache;


/**
 * Created by Misl on 2016/2/22.
 */
public abstract class LibApplication extends Application implements IResolutionListener {

    protected static LibApplication self;

    protected ResolutionAdapter resolutionAdapter;

    /**
     * 缓存对象
     */
    protected ACache aCache;

    public static LibApplication getInstance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initResolutionAdapter();
        self = this;
    }

    public ACache getCache() {
        if (null == aCache){
            aCache = ACache.get(this);
        }
        return aCache;
    }

    public void setCache(ACache cache) {
        this.aCache = cache;
    }

    /**
     * 分辨率适配类！
     *
     * @return
     */
    public ResolutionAdapter getResolutionAdapter() {
        return resolutionAdapter;
    }

    /**
     * 多分辨率配置器
     */
    private void initResolutionAdapter() {
        try {
            if (null == resolutionAdapter) {
                resolutionAdapter = ResolutionAdapter.getInstance();
                Resolution resolution = designResolutionForApp();
                if (null == resolution) {
                    resolution = new Resolution(750, 1334, 1.0f, 160, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                resolutionAdapter.setDesignResolution(this, resolution);
            }
        } catch (Exception e) {
            L.e(e, false);
        }
    }

}
