package com.fanhua.uiadapter.http;

import com.fanhua.uiadapter.L;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用Gson 统一处理json字符串和对象之间的转化
 */
public class GsonUtils {

    public static <T> T json2Class(String json, Class<T> classOfT) {
        T t = null;
        try {
            t = getGson().fromJson(json, classOfT);
        } catch (Exception e) {
            L.e(e, false);
        }
        return t;
    }

    public static <T> List<T> json2List(String json, Class<T> classOfT) {

        if (null == json || json.length() <= 0) return null;

        JsonElement jsonElement = new JsonParser().parse(json);
        if (jsonElement.isJsonNull() || !jsonElement.isJsonArray()) return null;

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<T> dtos = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            T dto = getGson().fromJson(element, classOfT);
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 将一个对象转化成json字符串
     *
     * @param object 目标对象
     * @return 返回json
     */
    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    public static Gson getGson() {
        return getGson(false);
    }

    public static Gson getGson(boolean isFormart){

        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Boolean.class, int2booleanAdapter)
                .registerTypeAdapter(boolean.class, int2booleanAdapter)
                // 禁用html字符urlencode
                .disableHtmlEscaping();

        if(isFormart){
            gsonBuilder.setPrettyPrinting();
        }

        return gsonBuilder.create();
    }

    private static final TypeAdapter<Boolean> int2booleanAdapter = new TypeAdapter<Boolean>() {
        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case NULL:
                    in.nextNull();
                    return null;
                case BOOLEAN:
                    return in.nextBoolean();
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return Boolean.parseBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };
}
