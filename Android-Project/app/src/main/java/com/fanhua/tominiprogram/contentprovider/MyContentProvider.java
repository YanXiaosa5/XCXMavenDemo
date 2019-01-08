package com.fanhua.tominiprogram.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fanhua.tominiprogram.contentprovider.db.PersonDao;

public class MyContentProvider extends ContentProvider {

    private final String TAG = "PersonContentProvider";
    private PersonDao personDao = null;
    private static final UriMatcher URI_MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);// 默认的规则是不匹配的
    private static final int PERSON = 1; // 操作单行记录
    private static final int PERSONS = 2; // 操作多行记录
    // 往UriMatcher中添加匹配规则。注意，这里面的url不要写错了，我就是因为写错了，半天没调试出来。哎···
    //这三个参数分别代表：权限、路径、和一个自定义代码。一般第一个参数是uri（包名.内容提供者的类名），第二个参数一般是数据库的表名。
    static {
        // 添加两个URI筛选
        URI_MATCHER.addURI("com.fanhua.tominiprogram.contentprovider.MyContentProvider",
                "person", PERSONS);
        // 使用通配符#，匹配任意数字
        //匹配规则的解释：*表示匹配任意字符，#表示匹配任意数字。注：如果内部的匹配规则越多，越容易访问。
        URI_MATCHER.addURI("com.fanhua.tominiprogram.contentprovider.MyContentProvider",
                "person/#", PERSON);
    }

    public MyContentProvider() {

    }

    @Override
    public boolean onCreate() {
        // 初始化一个数据持久层
        personDao = new PersonDao(getContext());
        Log.i(TAG, "--->>onCreate()被调用");
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        // 解析Uri，返回Code
        int flag = URI_MATCHER.match(uri);
        switch (flag) {
            case PERSONS:
                //调用数据库的访问方法
                long id = personDao.insertPerson(values); //执行插入操作的方法，返回插入当前行的行号
                resultUri = ContentUris.withAppendedId(uri, id);
                Log.i(TAG,"--->>插入成功, id=" + id);
                Log.i(TAG,"--->>插入成功, resultUri=" + resultUri.toString());
                System.out.println("insert success");
                break;
        }
        return resultUri;
    }

    //方法：删除记录。注：参数：selection和selectionArgs是查询的条件，是由外部（另一个应用程序）传进来的
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = -1; //影响数据库的行数
        try {
            int flag = URI_MATCHER.match(uri);
            switch (flag) {
                case PERSON:
                    // delete from student where id=?
                    // 单条数据，使用ContentUris工具类解析出结尾的Id
                    long id = ContentUris.parseId(uri);
                    String where_value = "id = ?";
                    String[] args = { String.valueOf(id) };
                    count = personDao.deletePerson(where_value, args);
                    break;
                case PERSONS:
                    count = personDao.deletePerson(selection, selectionArgs);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "--->>删除成功,count=" + count);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = -1;
        try {
            int flag = URI_MATCHER.match(uri);
            switch (flag) {
                case PERSON:
                    long id = ContentUris.parseId(uri);
                    String where_value = " id = ?";
                    String[] args = { String.valueOf(id) };
                    count = personDao.updatePerson(values, where_value, args);
                    break;
                case PERSONS:
                    count = personDao
                            .updatePerson(values, selection, selectionArgs);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "--->>更新成功，count=" + count);
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        try {
            int flag = URI_MATCHER.match(uri);
            switch (flag) {
                case PERSON:
                    long id = ContentUris.parseId(uri);
                    String where_value = " id = ?";
                    String[] args = { String.valueOf(id) };
                    cursor = personDao.queryPersons(where_value, args);
                    break;
                case PERSONS:
                    cursor = personDao.queryPersons(selection, selectionArgs);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "--->>查询成功，Count=" + cursor.getCount());
        return cursor;
    }

    //getType(Uri uri)方法：所有的内容提供者都必须提供的一个方法。用于获取uri对象所对应的MIME类型
    @Override
    public String getType(Uri uri) {
        int flag = URI_MATCHER.match(uri);
        switch (flag) {
            case PERSON:
                return "vnd.android.cursor.item/person"; // 如果是单条记录，则为vnd.android.cursor.item/[path]

            case PERSONS:
                return "vnd.android.cursor.dir/persons"; // 如果是多条记录，则为vnd.android.cursor.dir/[path]
            // + path
        }
        return null;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Log.i(TAG, "--->>" + method);
        Bundle bundle = new Bundle();
        bundle.putString("returnCall", "call被执行了");
        return bundle;
    }
}
