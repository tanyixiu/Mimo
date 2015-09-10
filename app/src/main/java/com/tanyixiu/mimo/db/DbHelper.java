package com.tanyixiu.mimo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/7/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mimos.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbStructure.CREATE_TABLE_ONE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    class DbStructure {
        public static final String CREATE_TABLE_ONE = "create table one(" +
                "id integer primary key," +
                "number text," +
                "imgurl text," +
                "title text," +
                "author text," +
                "day text," +
                "month text," +
                "year text," +
                "quote text" +
                ")";
    }
}
