package com.fanhua.uiadapter.http.converter;


import com.fanhua.uiadapter.http.GsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by misl on 2017/3/31.
 */
public class GsonConverter<T> extends BaseConverter<T> {

    protected Class<T> cls;

    public GsonConverter(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T converter(String body) {
        Type t = GsonConverter.class.getGenericSuperclass();
        Type[] params = ((ParameterizedType) t).getActualTypeArguments();
        Class<T> cls = (Class<T>) params[0];
        System.out.println(cls.getName());

        T t1 = null;
        t1 = (T) GsonUtils.json2Class(body, t.getClass());
        return t1;
    }
}
