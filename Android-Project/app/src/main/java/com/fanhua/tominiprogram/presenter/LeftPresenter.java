package com.fanhua.tominiprogram.presenter;

public interface LeftPresenter {

    /**
     * 获取推荐列表
     * @param size 每页长度
     * @param page 页码
     * @param requestTag 请求标记
     */
    void getDataList(int size,int page,String requestTag);

}
