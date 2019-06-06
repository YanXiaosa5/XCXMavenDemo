package com.fanhua.tominiprogram.utils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件名称为：NetPingManager.java
 * 文件功能简述： 描述一个URL地址是否有效
 */
public class NetPingManager {

    /**
     * 是否能连接到指定地址,需要在子线程中执行
     * @param host  地址
     * @return 是否能连接
     */
    public static boolean isConnByHttp(String host) {
        boolean isConn = false;
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(host);//你的服务器IP
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 5);
            if (conn.getResponseCode() == 200) {
                isConn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        System.out.println(isConn + "NetPingManager.isConnByHttp" + host);
        return isConn;
    }
}