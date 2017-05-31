package com.self.clothchooser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by nishant on 14-05-2017.
 */

public class DBAdapter {
    private Context context;
    private DBHelper helper;
    private SQLiteDatabase database;

    public DBAdapter(Context context) {
        this.context = context;
        helper = new DBHelper(context);
    }

    public DBAdapter open() {
        try {
            database = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() {
        try {
            helper.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public long insert(Bitmap bitmap_shirt, Bitmap bitmap_pant) {
        byte[] blob_shirt = Utility.getBytes(bitmap_shirt);
        byte[] blob_pant = Utility.getBytes(bitmap_pant);
        Log.d("TEST", "" + blob_pant + "\n" + blob_shirt);
        ContentValues values = new ContentValues();
        values.put(Table.SHIRT_IMAGE, blob_shirt);
        values.put(Table.PANT_IMAGE, blob_pant);
        return database.insert(Table.TB_NAME, Table.ROW_ID, values);
    }

    public Cursor getAllpairs() {
        String Column[] = {Table.ROW_ID, Table.SHIRT_IMAGE, Table.PANT_IMAGE};
        return database.query(Table.TB_NAME, Column, null, null, null, null, null);
    }

    public Cursor getsinglecolumn(int id) {
        String query = "SELECT * FROM a_TB WHERE id =" + id;
        return database.rawQuery(query, null);
    }

}
