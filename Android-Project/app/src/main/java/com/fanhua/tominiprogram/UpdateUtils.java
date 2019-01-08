package com.fanhua.tominiprogram;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 更新
 */
public class UpdateUtils {

    public static void intoDownloadManager(final Context mContext, String path){

        final DownloadManager dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(path);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        System.out.println("sd卡"+Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
        // 设置下载路径和文件名
        boolean download = isFolderExist("Download");
        if(download) {
            request.setDestinationInExternalPublicDir("Download", "quxiuxian.apk");
        }

        request.setDescription("新版本下载");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setMimeType("application/vnd.android.package-archive");

        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();

        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);

        final long refernece = dManager.enqueue(request);

        // 注册广播接收器，当下载完成时自动安装
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {

            public void onReceive(Context context, final Intent intent) {
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("版本更新")
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                if (refernece == myDwonloadID) {

                                    Intent install = new Intent(Intent.ACTION_VIEW);
                                    Uri downloadFileUri = dManager.getUriForDownloadedFile(refernece);
                                    install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(install);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        };
        mContext.registerReceiver(receiver, filter);

    }

    private static boolean isFolderExist(String dir) {
        File folder = Environment.getExternalStoragePublicDirectory(dir);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }


}
