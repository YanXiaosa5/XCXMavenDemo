package com.fanhua.uiadapter.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/*
 怎样对图片进行压缩呢？
 通过设置BitmapFactory.Options中inSampleSize的值就可以实现。
 比如我们有一张2048*1536像素的图片，将inSampleSize的值设置为4，
 就可以把这张图片压缩成512*384像素。
 原本加载这张图片需要占用13M的内存，
 压缩后就只需要占用0.75M了(假设图片是ARGB_8888类型，即每个像素点占用4个字节)。
 下面的方法可以根据传入的宽和高，计算出合适的inSampleSize值：
 */

/**
 * 图片相关
 */
public class ImageUtils {

    /**
     * 计算图片大小
     * @param options 配置
     * @param reqWidth 需要的宽度
     * @param reqHeight 需要的高度
     * @return  返回要缩放的比例值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 使用这个方法，首先你要将BitmapFactory.Options的inJustDecodeBounds属性设置为true，
     * 解析一次图片。然后将 BitmapFactory.Options 连同期望的宽度和高度一起传递到到 calculateInSampleSize 方法中，
     * 就可以得到合适的 inSampleSize 值了。之后再解析一次图片，使用新获取到的inSampleSize值，
     * 并把inJustDecodeBounds设置为false，就可以得到压缩后的图片了。
     * @param res  原始资源路径
     * @param resId  原始资源id
     * @param reqWidth  设置宽度
     * @param reqHeight  设置高度
     * @return  返回处理后的对象
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
