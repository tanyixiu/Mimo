package com.tanyixiu.mimo.resolver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.tanyixiu.mimo.entities.OneItemEntity;

/**
 * Created by tanyixiu on 2015/7/19.
 */
public class OneResolver {

    public static OneItemEntity readOneItemByIdFromDb(Context context, int id) {

        Uri uri = Uri.parse("content://com.tanyixiu.mimo.provider/one/" + id);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (null == cursor) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            return null;
        }
        OneItemEntity entity = new OneItemEntity();
        entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
        entity.setNumber(cursor.getString(cursor.getColumnIndex("number")));
        entity.setImgurl(cursor.getString(cursor.getColumnIndex("imgurl")));
        entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        entity.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
        entity.setDay(cursor.getString(cursor.getColumnIndex("day")));
        entity.setMonth(cursor.getString(cursor.getColumnIndex("month")));
        entity.setYear(cursor.getString(cursor.getColumnIndex("year")));
        entity.setQuote(cursor.getString(cursor.getColumnIndex("quote")));
        cursor.close();
        return entity;
    }

    public static void saveOneItemToDb(Context context, OneItemEntity entity) {
        if (null == entity) {
            return;
        }
        Uri uri = Uri.parse("content://com.tanyixiu.mimo.provider/one/");
        ContentValues values = new ContentValues();
        values.put("id", entity.getId());
        values.put("number", entity.getNumber());
        values.put("imgurl", entity.getImgurl());
        values.put("title", entity.getTitle());
        values.put("author", entity.getAuthor());
        values.put("day", entity.getDay());
        values.put("month", entity.getMonth());
        values.put("year", entity.getYear());
        values.put("quote", entity.getQuote());
        context.getContentResolver().insert(uri, values);
    }
}
