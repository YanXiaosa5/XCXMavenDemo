package com.fanhua.uiadapter.http;

import com.fanhua.uiadapter.L;
import com.fanhua.uiadapter.http.api.BaseHttpListener;
import com.fanhua.uiadapter.http.service.ApiService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 网络请求库
 * get post upload multiupload
 * <p>
 * Created by misl on 2017/3/24.
 */
public class HttpClient {

    /**
     * HTTP GET 方式请求数据.
     */
    public static final int GET = 0;

    /**
     * HTTP POST 方式请求数据.
     */
    public static final int POST = 1;

    /**
     * 主Url路径
     */
    private static String baseUrl;

    /**
     * 最开始需要设置BaseUrl
     *
     * @param url
     */
    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    /**
     * HTTP 请求头.
     */
    public static Map<String, String> publicHeader = new HashMap<>();

    /**
     * 添加公共请求Header.
     *
     * @param key
     * @param value
     */
    public static void addPublicHeader(String key, String value) {
        publicHeader.put(key, value);
    }

    /**
     * 设置公共请求Header.
     *
     * @param headers
     */
    public static void setPublicHeaders(Map<String, String> headers) {
        publicHeader = headers;
    }

    /**
     * 清空公共请求Header.
     */
    public static void clearPublicHeaders() {
        publicHeader.clear();
    }

    /**
     * 移除公共请求Header.
     *
     * @param key
     */
    public static void removePublicHeaders(String key) {
        publicHeader.remove(key);
    }

    /**
     * GET 方式请求数据.
     *
     * @param url              请求地址.
     * @param baseHttpListener 请求回调对象.
     */
    public static void get(String url, BaseHttpListener baseHttpListener, String reqTag) {
        get(url, null, null, baseHttpListener, reqTag);
    }

    /**
     * GET 方式请求数据.
     *
     * @param url              请求地址.
     * @param header           请求头.
     * @param params           请求参数.
     * @param baseHttpListener 请求回调对象.
     */
    public static void get(String url, Map header, Map params, BaseHttpListener baseHttpListener, String reqTag) {
        execute(url, GET, header, params, null, null, null, baseHttpListener, reqTag);
    }

    /**
     * GET 方式请求数据.
     *
     * @param url              请求地址.
     * @param params           请求参数.
     * @param baseHttpListener 请求回调对象.
     */
    public static void get(String url, Map params, BaseHttpListener baseHttpListener, String reqTag) {
        get(url, null, params, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传单个字符串body
     * @see #string(String, Map, String, BaseHttpListener, String)
     */
    public static void string(String url, String singleBody, BaseHttpListener baseHttpListener, String reqTag) {
        string(url, null, singleBody, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传单个字符串body
     * @see #string(String, Map, Map, String, BaseHttpListener, String)
     */
    public static void string(String url, Map params, String singleBody, BaseHttpListener baseHttpListener, String reqTag) {
        string(url, null, params, singleBody, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传单个字符串body
     * @see #string(String, Map, Map, String, BaseHttpListener, String)
     */
    public static void string(String url, Map params, BaseHttpListener baseHttpListener, String reqTag) {
        string(url, params, "", baseHttpListener, reqTag);
    }


    /**
     * @desc 上传字符串
     * @see #execute(String, int, Map, Map, String, Map, Map, BaseHttpListener, String)
     */
    public static void string(String url, Map header, Map params, String singleBody, BaseHttpListener baseHttpListener, String reqTag) {
        execute(url, POST, header, params, singleBody, null, null, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传单文件
     * @see #file(String, Map, String, String, BaseHttpListener, String)
     */
    public static void file(String url, String key, String file, BaseHttpListener baseHttpListener, String reqTag) {
        file(url, null, key, file, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传单文件
     * @see #files(String, Map, Map, BaseHttpListener, String)
     */
    public static void file(String url, Map params, String key, String file, BaseHttpListener baseHttpListener, String reqTag) {

        Map<String, List<String>> multpartFiles = new HashMap<>();
        List<String> files = new ArrayList<>();
        files.add(file);
        multpartFiles.put(key, files);
        files(url, params, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文件
     * @see #file(String, Map, String, String, BaseHttpListener, String)
     */
    public static void files(String url, Map<String, List<String>> multpartFiles, BaseHttpListener baseHttpListener, String reqTag) {
        files(url, null, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文件
     * @see #upload(String, Map, Map, Map, Map, BaseHttpListener, String)
     */
    public static void files(String url, Map params, Map<String, List<String>> multpartFiles, BaseHttpListener baseHttpListener, String reqTag) {
        upload(url, null, params, null, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本
     * @see #strings(String, Map, Map, BaseHttpListener, String)
     */
    public static void strings(String url, Map<String, Object> multpartBody, BaseHttpListener baseHttpListener, String reqTag) {
        strings(url, null, multpartBody, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本
     * @see #upload(String, Map, Map, Map, Map, BaseHttpListener, String)
     */
    public static void strings(String url, Map params, Map<String, Object> multpartBody, BaseHttpListener baseHttpListener, String reqTag) {
        upload(url, null, params, multpartBody, null, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本和文件
     * @see #upload(String, Map, Map, Map, BaseHttpListener, String)
     */
    public static void upload(String url, String bodyKey, String body, String fileKey, String file, BaseHttpListener baseHttpListener, String reqTag) {

        List<String> bodys = new ArrayList<>();
        bodys.add(body);

        List<String> files = new ArrayList<>();
        files.add(file);

        upload(url, bodyKey, bodys, fileKey, files, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本和文件
     * @see #upload(String, Map, Map, Map, BaseHttpListener, String)
     */
    public static void upload(String url, String bodyKey, List<String> bodys, String fileKey, List<String> files, BaseHttpListener baseHttpListener, String reqTag) {

        Map<String, Object> multpartBody = new HashMap<>();
        multpartBody.put(bodyKey, bodys);

        Map<String, List<String>> multpartFiles = new HashMap<>();
        multpartFiles.put(fileKey, files);

        upload(url, null, multpartBody, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本和文件
     * @see #upload(String, Map, Map, Map, BaseHttpListener, String)
     */
    public static void upload(String url, Map<String, Object> multpartBody, Map<String, List<String>> multpartFiles, BaseHttpListener baseHttpListener, String reqTag) {
        upload(url, null, multpartBody, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本和文件
     * @see #upload(String, Map, Map, Map, Map, BaseHttpListener, String)
     */
    public static void upload(String url, Map params, Map<String, Object> multpartBody, Map<String, List<String>> multpartFiles, BaseHttpListener baseHttpListener, String reqTag) {
        upload(url, null, params, multpartBody, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @desc 上传多文本和文件
     * @see #execute(String, int, Map, Map, String, Map, Map, BaseHttpListener, String)
     */
    public static void upload(String url, Map header, Map params, Map<String, Object> multpartBody, Map<String, List<String>> multpartFiles, BaseHttpListener baseHttpListener, String reqTag) {
        execute(url, POST, header, params, null, multpartBody, multpartFiles, baseHttpListener, reqTag);
    }

    /**
     * @param url              请求地址.
     * @param method           请求方法.
     * @param header           请求头.
     * @param params           请求参数.
     * @param singleBody       请求的body内容.
     * @param multpartBody     多部分内容上传.
     * @param multpartFiles    文件所在的Map 由key 和 Map对应.
     * @param baseHttpListener 请求返回的回调对象.
     * @param reqTag           请求tag
     * @desc 开始执行网络请求.
     */
    private static void execute(String url, int method, Map header, Map params, String singleBody, Map<String, Object> multpartBody, Map<String, List<String>> multpartFiles, BaseHttpListener baseHttpListener, String reqTag) {
        new Builder()
                .url(url)
                .method(method)
                .headers(header)
                .params(params)
                .singleBody(singleBody)
                .multpartBody(multpartBody)
                .multpartFiles(multpartFiles)
                .baseHttpListener(baseHttpListener)
                .requestTag(reqTag)
                .build();
    }

    /**
     * 构建网络Builder 对象用于构建请求.
     */
    private static class Builder {

        /**
         * 请求的地址.
         */
        private String url;

        /**
         * 请求方法.
         */
        private int method;

        /**
         * 请求的tag，便于监听由谁发起的请求.
         */
        private String requestTag;

        /**
         * 请求服务器封装.
         */
        private ApiService apiService;

        /**
         * 请求Headers.
         */
        private Map headers;

        /**
         * 请求参数.
         */
        private Map params;

        /**
         * 请求body.
         */
        private String singleBody;

        private Map<String, Object> multpartBody;

        /**
         * 上传的文件Map.
         */
        private Map<String, List<String>> multpartFiles;

        /**
         * 网络监听对象.
         */
        private BaseHttpListener baseHttpListener;

        /**
         * 消息订阅对象.
         */
        private Subscriber subscriber;

        /**
         * okHttpClient 对象.
         */
        private OkHttpClient httpClient;

        /**
         * Builder 构造函数.
         *
         * @param url 请求的url
         * @return
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 请求方法.
         *
         * @param method
         * @return
         */
        public Builder method(int method) {
            this.method = method;
            return this;
        }

        /**
         * 请求Tag.
         *
         * @param requestTag
         * @return
         */
        public Builder requestTag(String requestTag) {
            this.requestTag = requestTag;
            return this;
        }

        /**
         * 请求头设置.
         *
         * @param headers
         * @return
         */
        public Builder headers(Map headers) {
            if (null != headers) {
                publicHeader.putAll(headers);
            }
            this.headers = publicHeader;
            return this;
        }

        /**
         * multipart文件对象上传结构，支持多模块和多文件上传.
         *
         * @param multpartFiles 多模块多文件
         * @return
         */
        public Builder multpartFiles(Map<String, List<String>> multpartFiles) {
            this.multpartFiles = multpartFiles;
            return this;
        }

        /**
         * multipart对象上传结构，支持多模块上传.
         *
         * @param multpartBody 多模块
         * @return
         */
        public Builder multpartBody(Map<String, Object> multpartBody) {
            this.multpartBody = multpartBody;
            return this;
        }

        /**
         * 上传的body对象.
         *
         * @param singleBody
         * @return
         */
        public Builder singleBody(String singleBody) {
            this.singleBody = singleBody;
            return this;
        }

        /**
         * 请求参数对象.
         *
         * @param params
         * @return
         */
        public Builder params(Map params) {
            this.params = params;
            return this;
        }

        /**
         * 请求监听者.
         *
         * @param baseHttpListener
         * @return
         */
        public Builder baseHttpListener(BaseHttpListener baseHttpListener) {
            this.baseHttpListener = baseHttpListener;
            return this;
        }

        /**
         * 请求监听者.
         *
         * @param subscriber
         * @return
         */
        public Builder subscriber(Subscriber subscriber) {
            this.subscriber = subscriber;
            return this;
        }

        /**
         * 构建网络请求事件.
         */
        public void build() {

            if (null == baseUrl) {
                L.e("base url cant null.");
                return;
            }

            RequestBody requestBody = null;
            Response response = null;
            final BaseInterceptor baseInterceptor = new BaseInterceptor(headers);

            // 声明通过okHttp发起网络请求，先添加拦截器设置请求头.
            httpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(baseInterceptor)
                    .build();

            // 声明Retrofit 对象用于真正的网络请求管理模块.
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();

            // 通过Service 的方式创建网络请求对象.它拥有发起网络请求的能力.
            apiService = retrofit.create(ApiService.class);

            // 开始构建监听返回的对象.
            Observable<ResponseBody> responseBodyObservable = null;
            switch (method) {
                case GET:
                    // GET 方式发起HTTP请求. 需要判断参数是否存在.用户可以直接拼接在url上 也可以直接通过params设置参数，后台自动拼接.
                    if (null == params) {
                        // 参数不存在.
                        responseBodyObservable = apiService.doGet(url);
                    } else {
                        // 参数存在.
                        responseBodyObservable = apiService.doGet(url, params);
                    }
                    break;
                case POST:
                    // POST 方式发起HTTP请求.这里通过文件来区分是文件上传，还是数据提交.
                    if (null != multpartFiles || null != multpartBody) {
                        responseBodyObservable = multpartUpload();
                    } else {
                        if (null != singleBody) {
                            // 设置body格式.
                            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), singleBody);
                        }
                        if (null == params) {
                            responseBodyObservable = apiService.doPost(url, requestBody);
                        } else {
                            responseBodyObservable = apiService.doPost(url, params, requestBody);
                        }
                    }
                    break;
            }

            if (null != responseBodyObservable) {
                responseBodyObservable.compose(schedulersTransformer())
                        .subscribe(parseSubscriber(baseInterceptor));
            }
        }

        /**
         * 定义转换器，将订阅者和消费者绑定在一起.
         *
         * @return
         */
        Observable.Transformer schedulersTransformer() {
            return new Observable.Transformer() {
                @Override
                public Object call(Object observable) {
                    return ((Observable) observable).subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            };
        }

        /**
         * multipart 上传的方法，需要构建Multpart
         *
         * @return
         */
        private Observable<ResponseBody> multpartUpload() {

            if (null == multpartFiles && multpartBody == null) return null;

            RequestBody requestBody = null;

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MediaType.parse("multipart/form-data"));
            // 组合文件模块
            if (null != multpartFiles) {

                Iterator<String> iterator = multpartFiles.keySet().iterator();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    List<String> filesName = multpartFiles.get(key);

                    if (null != filesName) {
                        for (String name : filesName) {
                            File file = new File(name);
                            if (null == file) {
                                continue;
                            }
                            requestBody = RequestBody.create(MediaType.parse(MIMETypeUtils.getFileType(file)), file);
                            builder.addFormDataPart(key, file.getName(), requestBody);
                        }
                    }
                }
            }
            // 组合数据模块
            if (null != multpartBody) {

                Iterator<String> iterator = multpartBody.keySet().iterator();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = multpartBody.get(key);
                    if (value instanceof String) {
                        builder.addFormDataPart(key, value.toString());
                    } else if (value instanceof List) {
                        List<Object> values = (List<Object>) value;
                        if (((List) value).size() > 0) {
                            for (Object object : values) {
                                builder.addFormDataPart(key, object.toString());
                            }
                        }
                    }
                }

            }

            if (null == params) {
                return apiService.doPost(url, builder.build());
            }
            return apiService.doPost(url, params, builder.build());
        }

        private Subscriber<ResponseBody> parseSubscriber(final BaseInterceptor baseInterceptor) {

            return new Subscriber<ResponseBody>() {

                @Override
                public void onCompleted() {
                    if (null != baseHttpListener) {
                        baseHttpListener.onCompleted(requestTag);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (null != baseHttpListener) {
                        baseHttpListener.onFailed(e, requestTag);
                    }
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        String bodyContent = responseBody.string();
                        if (null != baseHttpListener) {
                            baseHttpListener.onSucceed(bodyContent, baseInterceptor, requestTag);
                        }
                    } catch (IOException e) {
                        L.e(e, true);
                    }
                }
            };
        }
    }
}
