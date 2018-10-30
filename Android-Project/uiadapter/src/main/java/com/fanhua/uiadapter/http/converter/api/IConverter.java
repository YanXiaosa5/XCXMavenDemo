package com.fanhua.uiadapter.http.converter.api;

/**
 * 转换器接口
 * Created by misl on 2017/3/31.
 */

public interface IConverter<T> {

    T converter(String body);
}
