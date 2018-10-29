package com.fanhua.uiadapter;

import android.util.Log;

/**
 * @desc
 *      Log统一管理类
 *
 * @author misl
 *
 * @email mishulin@tele-hot.com
 * <p>
 * Created by misl on 2017/3/14.
 */
public class L {

    /**
     * 判断是否是debug -》》》》扩展为级别 - 可在gradle 中编译时加入参数
     */
    public static boolean isDebug = true;

    /**
     * 获取项目名称
     */
    private static final String TAG = "XH_**";

    private L(){
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 下面四个是默认tag的函数
    public static void i(String msg)
    {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void w(String msg)
    {
        if (isDebug)
            Log.w(TAG, msg);
    }

    public static void e(Exception e, boolean isbroken) {

        if (isbroken){
            e.printStackTrace();
        }

        if (isDebug)
            Log.e(TAG, e.getMessage());
    }

//    public static void e(Exception e) {
//        if (isDebug)
//            Log.e(TAG, e.getMessage());
//    }

    public static void e(String msg)
    {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg)
    {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void Jout(String str){
        if (isDebug)
            System.out.println("System.out.print : "+str);
    }

}