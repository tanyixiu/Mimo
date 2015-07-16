package com.tanyixiu.mimo.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tanyixiu.mimo.db.DbHelper;
import com.tanyixiu.mimo.utils.OneItemParser;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/16.
 */
public class OneItemLoader {

    private Context mContext;

    public OneItemLoader(Context context) {
        mContext = context;
    }

    public interface onOneItemLoadedListener {
        void loaded(OneItemEntity entity);

        void loadFail(Exception ex);
    }

    public OneItemEntity readMaxOneItemFromDb() {

        SQLiteDatabase db = (new DbHelper(mContext)).getReadableDatabase();
        final String SQL = "select id,number,imgurl,title,author,day,month,year,quote,onedate from one order by id desc limit 1";
        Cursor cursor = db.rawQuery(SQL, null);
        if (null == cursor) {
            return null;
        }
        OneItemEntity entity = new OneItemEntity();
        while (cursor.moveToNext()) {
            entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
            entity.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            entity.setImgurl(cursor.getString(cursor.getColumnIndex("imgurl")));
            entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            entity.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            entity.setDay(cursor.getString(cursor.getColumnIndex("day")));
            entity.setMonth(cursor.getString(cursor.getColumnIndex("month")));
            entity.setYear(cursor.getString(cursor.getColumnIndex("year")));
            entity.setQuote(cursor.getString(cursor.getColumnIndex("quote")));
        }
        cursor.close();
        db.close();
        return entity;
    }

    public void requestMaxOneItemFromWeb(RequestQueue requestQueue,
                                         final onOneItemLoadedListener loadedListener) {
        int oneId = OneItemEntity.getIdByDate(new Date());
        String oneUrl = OneItemEntity.getOneUrlById(oneId);
        StringRequest request = new StringRequest(oneUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        OneItemEntity entity;
                        try {
                            entity = OneItemParser.parser(s);
                            loadedListener.loaded(entity);
                        } catch (Exception e) {
                            e.printStackTrace();
                            loadedListener.loadFail(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadedListener.loadFail(volleyError);
                    }
                });
        requestQueue.add(request);
    }

    private void saveOneItemToDb(OneItemEntity entity) {
        if (null == entity) {
            return;
        }
        try {
            SQLiteDatabase db = (new DbHelper(mContext)).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", entity.getId());
            values.put("number", entity.getNumber());
            values.put("imgurl", entity.getImgurl());
            values.put("title", entity.getTitle());
            values.put("author", entity.getAuthor());
            values.put("day", entity.getDay());
            values.put("month", entity.getMonth());
            values.put("year", entity.getYear());
            db.insert("one", null, values);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
