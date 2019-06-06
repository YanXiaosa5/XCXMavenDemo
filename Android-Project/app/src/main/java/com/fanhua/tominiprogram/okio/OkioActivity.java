package com.fanhua.tominiprogram.okio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fanhua.tominiprogram.R;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okio.Okio;

public class OkioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okio);
    }

    /**
     * 向file中写入数据 用OutputStream实现
     * @param file
     */
    public static void writeTestStream(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStream os = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF("write string by utf-8.\n");
            dos.writeInt(1234);
            dos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向file中写入数据,用okio实现
     * @param file
     */
    public void writeTestOkio(File file) {
        try {
            Okio.buffer(Okio.sink(file))
                    .writeUtf8("write string by utf-8.\n")
                    .writeInt(1234).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
