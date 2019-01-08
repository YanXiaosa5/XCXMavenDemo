package com.fanhua.tominiprogram.signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

/**
 * @author zmm on 2018/12/27
 */
public class SignaUtils {

    public static String getSignature(PackageInfo info) {
        try {
            //PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
            //    PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("MD5");//SHA1
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            int length = publicKey.length;
            for (int i = 0; i < length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                    .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                if (i != length - 1) {
                    hexString.append(":");
                }

            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取程序的签名
     *
     * @param context
     * @param packname
     * @return
     */
    public static String getAppSignature(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            Log.e("zmm", packname + "info--->" + packinfo);
            //获取当前应用签名
            return getSignature(packinfo);
        } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        return packname;
    }

    public static Signature[] getSignature(Context context,String pName){
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(pName, PackageManager.GET_SIGNATURES);
            Log.e("zmm", pName + "info--->" + packinfo);
            //获取当前应用签名
            return packinfo.signatures;
        } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        return null;
    }
}
