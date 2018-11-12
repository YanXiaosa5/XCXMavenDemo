package com.fanhua.uiadapter.http;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yxs on 2017/3/29.
 */
public final class BaseInterceptor implements Interceptor {

    /**
     * 请求头
     */
    private Map<String, String> reqHeaders;

    /**
     * 返回res
     */
    private Response response;

    /**
     * 请求req
     */
    private Request request;

    public BaseInterceptor(Map<String, String> headers) {
        this.reqHeaders = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        request = chain.request();

        Request.Builder builder = request.newBuilder();

        if (reqHeaders != null && reqHeaders.size() > 0) {
            for (String key : reqHeaders.keySet()) {
                builder.header(key, reqHeaders.get(key));
            }
//            Headers tmpHeaders = Headers.of(reqHeaders);
//            builder.headers(tmpHeaders);
        }
        response = chain.proceed(builder.build());
        return response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
