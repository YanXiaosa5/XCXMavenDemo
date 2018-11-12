package com.fanhua.uiadapter.logger;

/**
 * Created by yxs on 2017/7/27.
 */
public abstract class AbstractNamedLogger implements ILogger {
    protected String mName;

    public AbstractNamedLogger(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }
}
