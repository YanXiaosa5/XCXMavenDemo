package com.fanhua.tominiprogram.contentprovider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonDao {

    private DBHelper helper = null;

    public PersonDao(Context context) {
        helper = new DBHelper(context);
    }

    //方法：插入操作，返回的long类型为：插入当前行的行号
    public long insertPerson(ContentValues values) {
        long id = -1;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            id = database.insert("person", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return id;
    }

    public int deletePerson(String whereClause, String[] whereArgs) {
        int count = -1;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            count = database.delete("person", whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return count;
    }

    public int updatePerson(ContentValues values, String whereClause,
                            String[] whereArgs) {
        SQLiteDatabase database = null;
        int count = -1;
        try {
            database = helper.getWritableDatabase();
            count = database.update("person", values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
        return count;
    }

    public Cursor queryPersons(String selection, String[] selectionArgs) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(true, "person", null, selection,
                    selectionArgs, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                // database.close();
            }
        }
        return cursor;
    }

}
