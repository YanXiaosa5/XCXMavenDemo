package com.fanhua.tominiprogram.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanhua.tominiprogram.R;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * https://www.cnblogs.com/zhaoyanjun/p/5175502.html   RxJava 和 RxAndroid 一 (基础)
 *
 * https://gank.io/post/560e15be2dca930e00da1083#toc_14   RxJava 详解
 *
 */
public class RxJavaActivity extends AppCompatActivity {

    /**
     * 图片集合
     */
    private LinearLayout ll_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        ll_images = findViewById(R.id.ll_images);
        method1();
    }

    private void method1() {
        File fold = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)+File.separator+"Screenshots");
        File[] files = fold.listFiles();
        Observable.from(files)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return listFiles(file);
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.getName().endsWith(".png");
                    }
                })
                .map(new Func1<File, Bitmap>() {
                    @Override
                    public Bitmap call(File file) {
                        return BitmapFactory.decodeFile(file.getPath());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        copyValue(bitmap);
                    }
                });
    }

    /**
     * 遍历文件夹,判断file是文件夹还是文件
     * @param file
     * @return
     */
    private Observable<File> listFiles(File file){
        if(file.isDirectory()){
            return Observable.from(file.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        }else {
            return Observable.just(file);
        }
    }

    /**
     * 生成ImageView添加到布局中
     * @param bitmap
     */
    public void copyValue(Bitmap bitmap){
        ImageView tempImage = new ImageView(this);
        tempImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tempImage.setImageBitmap(bitmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        params.setMargins(10,10,10,10);
        tempImage.setLayoutParams(params);
        ll_images.addView(tempImage);
    }
}
