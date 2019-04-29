package com.fanhua.tominiprogram.utils;

import android.content.Context;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * 连续点击五次的判断
 */
public class FiveClickUtils {

    //测试功能,一般情况下不会被发现
    private static long[] mHints = new long[5];

    //测试功能,一般情况下不会被发现
    public static void onDisplaySettingButton(TextView tv, Context context) {
        System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);//把从第二位至最后一位之间的数字复制到第一位至倒数第一位
        mHints[mHints.length - 1] = SystemClock.uptimeMillis();//从开机到现在的时间毫秒数
        if (SystemClock.uptimeMillis() - mHints[0] <= 1500) {//连续点击之间间隔小于一秒，有效

        }
    }

}
