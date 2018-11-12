package com.fanhua.uiadapter.file;

import android.content.Context;
import android.util.Log;

import com.fanhua.uiadapter.logger.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * IOUtils的使用案例
 */
public class Demo {
    private Context mContext;

    public Demo(Context context) {
        mContext = context;
    }

    public void startDemo() {
        openFileOutput();
    }

    public void copyToPrivateSubDirDemo() {
        // 需要拷贝到文件
        File tempFile = IOUtils.mkFileInCache(mContext, "yunayi.txt");
        IOUtils.safeOnceWrite(IOUtils.openFileWriter(tempFile, false), true, "this is a test file");
        LoggerFactory.getDefault().d(tempFile == null ? "Cache file is null" : tempFile.getAbsolutePath());
        if (tempFile != null) {
            // 在应用程序私有文件目录下创建子目录pic
            File picDir = IOUtils.mkSubDirsInFiles(mContext, "pic");
            if (picDir != null) {
                // 将tempFile拷贝到pic目录下，拷贝后文件名是newFile.txt
                File file = IOUtils.copyFileToDir(tempFile, picDir, "newFile.txt", false);
                LoggerFactory.getDefault().d("Dest File " +
                        (file == null
                                ? "null"
                                : ("Path: " + file.getAbsolutePath() + ", Exist: " + file.exists() + ", Length: " + file.length())));
            }
        }
    }

    public void showFileParentDemo(Context context) {
        File file = new File(context.getFilesDir(), "com/fanhua/tominiprogram/test.txt");
        LoggerFactory.getDefault().d("File Parent: " + file.getParent() + ", File Name: " + file.getName());
    }

    public void createFile() {
        File file = IOUtils.mkFile(mContext.getFilesDir(), "next/next/test.file");
//        File file = IOUtils.mkFile(new File(mContext.getFilesDir(), "next/next/test.file"), true);
        LoggerFactory.getDefault().d("File: {}", (file == null) ? "null" : (file.getAbsolutePath() + ", exist: " + file.exists()));
    }

    public void openFileOutput() {
        Writer writer = IOUtils.openFileWriter(mContext.getCacheDir(), "temp.cache", false);
        LoggerFactory.getDefault().d("Writer: {}", writer);
        if (writer != null) {
            try {
                writer.write("this is a test file");
            } catch (IOException e) {
                LoggerFactory.getDefault().e(e);
            } finally {
                IOUtils.safeClose(writer);
            }
        }
    }
}
