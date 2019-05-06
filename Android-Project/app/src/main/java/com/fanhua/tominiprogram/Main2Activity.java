package com.fanhua.tominiprogram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.fanhua.tominiprogram.presenter.LeftImpl;
import com.fanhua.tominiprogram.presenter.LeftPresenter;
import com.fanhua.tominiprogram.presenter.OnBaseHttpPresenter;
import com.fanhua.uiadapter.http.BaseInterceptor;

/**
 * 网络请求demo(加密请忽略),请确保在application中配置BaseUrl:  HttpClient.setBaseUrl("");
 */
public class Main2Activity extends BaseActivity implements OnBaseHttpPresenter{

    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LeftPresenter leftPresenter = new LeftImpl(this,this);
        leftPresenter.getDataList(10,1,"list");
        findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }else{
                    //版本小于23，不用动态请求权限
                    System.out.println("Main2Activity.onClick 不用请求");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        //如果用户没有读取存储的权限,则提示
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE);
        }else{
            //已经获取了权限
            System.out.println("Main2Activity.requestPermission already have permissions");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            switch (permissions[0]){
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    //判断权限提示框是否会显示,如果不显示
                    if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        //如果不显示，则自定义对话框，跳转对应的设置页面
                        toSetting();
                    }else{
                        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            //允许
                        }else{
                            //拒绝
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 跳转到对应的设置页面
     */
    public void toSetting(){
        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected boolean isAutoAdapter() {
        return false;
    }

    @Override
    public void onSucceed(String data, BaseInterceptor baseInterceptor, String tag) {
        System.out.println("Main2Activity.onSucceed"+data);
    }

    @Override
    public void onCompleted(String tag) {
        System.out.println("Main2Activity.onCompleted 请求完成!");
    }

    @Override
    public void onFailed(Throwable throwable, String tag) {
        System.out.println("Main2Activity.onFailed 请求失败!");
    }
}
