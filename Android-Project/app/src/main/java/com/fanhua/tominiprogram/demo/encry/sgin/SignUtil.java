package com.fanhua.tominiprogram.demo.encry.sgin;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 请求参数签名工具类
 * @author yaogang
 * @creat 2018-08-23 14:05
 * @contact betteryaogang@gmail.com
 */
public class SignUtil {

    public static String createSign(SortedMap<String, String> packageParams, String appSecret) {
        String characterEncoding = "UTF-8";
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = entry.getKey().toString();
            String v = entry.getValue().toString();
            if (isNotBlank(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("secret=" + appSecret);
        String sign = MD5Util.encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }


    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
}
