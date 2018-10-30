package com.fanhua.uiadapter.http.service;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by misl on 2017/3/23.
 */
public interface ApiService {

    @GET("{url}")
    Observable<ResponseBody> doGet(
            @Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<ResponseBody> doGet(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps);

    @POST("{url}")
    Observable<ResponseBody> doPost(
            @Path(value = "url", encoded = true) String url,
            @Body RequestBody requestBody);

    @POST("{url}")
    Observable<ResponseBody> doPost(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps,
            @Body RequestBody requestBody);

    @POST("{url}")
    Observable<ResponseBody> doPost(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps);

    @POST("{url}")
    Observable<ResponseBody> doPost(
            @Path(value = "url", encoded = true) String url,
            @Body MultipartBody body);

    @POST("{url}")
    Observable<ResponseBody> doPost(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps,
            @Body MultipartBody body);

    @POST("{url}")
    Observable<ResponseBody> multpart(
            @Path(value = "url", encoded = true) String url,
            @QueryMap Map<String, String> maps,
            @Body List<MultipartBody> body);
}
