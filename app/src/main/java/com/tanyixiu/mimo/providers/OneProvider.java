package com.tanyixiu.mimo.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tanyixiu.mimo.db.DbHelper;

/**
 * Created by tanyixiu on 2015/7/15.
 */
public class OneProvider extends ContentProvider {

    private static final int ONE_DIR = 0;
    private static final int ONE_ITEM = 1;
    private static final String AUTHORITY = "com.tanyixiu.mimo.provider";
    private static final String TB_ONE = "one";

    private static UriMatcher sUriMatcher;
    private DbHelper mDbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "one", ONE_DIR);
        sUriMatcher.addURI(AUTHORITY, "one/#", ONE_ITEM);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case ONE_DIR:
                cursor = db.query(TB_ONE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case ONE_ITEM:
                String uniqueId = uri.getPathSegments().get(1);
                cursor = db.query(TB_ONE, projection, "uniqueid = ?", new String[]{uniqueId},
                        null, null, sortOrder);
                break;

            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri uriReturn;
        switch (sUriMatcher.match(uri)) {
            case ONE_DIR:
            case ONE_ITEM:
                long uniqueId = db.insert(TB_ONE, null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/" + TB_ONE + "/" + uniqueId);
                break;

            default:
                uriReturn = null;
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case ONE_DIR:
                updatedRows = db.update(TB_ONE, values, selection, selectionArgs);
                break;

            case ONE_ITEM:
                String uniqueId = uri.getPathSegments().get(1);
                updatedRows = db.update(TB_ONE, values, "uniqueid = ?", new String[]{uniqueId});
                break;

            default:
                updatedRows = 0;
                break;
        }
        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deletedRows;
        switch (sUriMatcher.match(uri)) {
            case ONE_DIR:
                deletedRows = db.delete(TB_ONE, selection, selectionArgs);
                break;

            case ONE_ITEM:
                String uniqueId = uri.getPathSegments().get(1);
                deletedRows = db.delete(TB_ONE, "uniqueid = ?", new String[]{uniqueId});
                break;

            default:
                deletedRows = 0;
                break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ONE_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TB_ONE;

            case ONE_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TB_ONE;
        }
        return null;
    }
}
