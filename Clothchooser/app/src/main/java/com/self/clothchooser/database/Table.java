package com.self.clothchooser.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by nishant on 14-05-2017.
 */

public class Table {
    public static final String ROW_ID = "id";
    public static final String PANT_IMAGE = "pantimage";
    public static final String SHIRT_IMAGE = "shirtimage";

    public static final String DB_NAME = "a_DB";
    public static final String TB_NAME = "a_TB";
    public static final int DB_VERSION = '1';

    public static final String CREATE_TB = "create table "
            + TB_NAME
            + "("
            + ROW_ID + " integer primary key autoincrement, "
            + PANT_IMAGE + " blob not null,"
            + SHIRT_IMAGE
            + " blob not null"
            + ");";


}
