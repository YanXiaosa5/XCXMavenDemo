package com.fanhua.tominiprogram.async;

/**
 * Created by YuanZezhong.
 * Date: 2018/10/26
 * Time: 15:59
 */
public abstract class TagTask<R> implements Task<R> {
    private String tag;

    public TagTask(String tag) {
        this.tag = tag;
    }

    @Override
    public String tag() {
        return tag;
    }
}
