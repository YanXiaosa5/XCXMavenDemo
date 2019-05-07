package com.fanhua.tominiprogram.utils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件名称为：NetPingManager.java
 * 文件功能简述： 描述一个URL地址是否有效
 */
public class NetPingManager {

    public static boolean isConnByHttp(String host){
        boolean isConn = false;
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(host);//你的服务器IP
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(1000*5);
            if(conn.getResponseCode()==200){
                isConn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NetPingManager.isConnByHttp 失败");
        }finally{
            if(conn != null)
            conn.disconnect();
        }
        System.out.println("NetPingManager.isConnByHttp"+isConn);
        return isConn;
    }

}