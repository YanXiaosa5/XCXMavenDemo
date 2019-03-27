package com.fanhua.tominiprogram.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

public class MyContentProviderTest {

    public MyContentProviderTest(){}

    private Context context =  InstrumentationRegistry.getInstrumentation().getContext();
//
    @Test
    public void onCreate() {
        //getTargetContext();
    }
//
    @Test
    public void insert() {
        insertTest();
    }
//
    @Test
    public void delete() {
        singleDelete();
    }
//
    @Test
    public void update() {

        updateSingleTest();
    }
//
    @Test
    public void query() {
        queryTest();
    }
//
    @Test
    public void getType() {
    }
//
    @Test
    public void call() {
        calltest();
    }
//
//
    public void calltest() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person");
        Bundle bundle = contentResolver.call(uri, "method", null, null);
        String returnCall = bundle.getString("returnCall");
        Log.i("main", "-------------->" + returnCall);
    }
//
//    测试方法：向数据库中添加记录。如果之前没有数据库，则会自动创建
    public void insertTest() {
//         使用内容解析者ContentResolver访问内容提供者ContentProvider
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "2");
        values.put("address", "2");

        // content://authorities/person
        // http://

        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person");
        contentResolver.insert(uri, values);
    }
//
//    测试方法：删除单条记录。如果要删除所有记录：content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person
    public void singleDelete() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person/2");//删除id为1的记录
        contentResolver.delete(uri, null, null);
    }
//
//    测试方法：根据条件删除记录。
    public void deletes() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person");
        String where = "address=?";
        String[] where_args = { "HK" };
        contentResolver.delete(uri, where, where_args);  //第二个参数表示查询的条件"address=?"，第三个参数表示占位符中的具体内容
    }
//
//    方法：根据id修改记录。注：很少有批量修改的情况。
    public void updateSingleTest() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person/2");
        ContentValues values = new ContentValues();
        values.put("name", "李四");
        values.put("address", "上海");
        contentResolver.update(uri, values, null, null);
    }
//
//    方法：根据条件来修改记录。
    public void updatesTest() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person/student");
        ContentValues values = new ContentValues();
        values.put("name", "王五");
        values.put("address", "深圳");
        String where = "address=?";
        String[] where_args = { "beijing" };
        contentResolver.update(uri, values, where, where_args);
    }
//
//    测试方法：查询所有记录。如果要查询单条记录：content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person/1
    public void queryTest() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person");
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            Log.i("MyTest",
                    "--->>"
                            + cursor.getString(cursor.getColumnIndex("name")));
        }
    }
//
//    测试方法：根据条件查询所有记录。
    public void querysTest() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri
                .parse("content://com.fanhua.tominiprogram.contentprovider.MyContentProvider/person");
        String where = "address=?";
        String[] where_args = { "深圳" };
        Cursor cursor = contentResolver.query(uri, null, where, where_args,
                null);
        while (cursor.moveToNext()) {
            Log.i("main",
                    "-------------->"
                            + cursor.getString(cursor.getColumnIndex("name")));
        }
    }
}