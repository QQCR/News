package com.example.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.news.models.Save;

import java.util.ArrayList;
import java.util.List;

public class SaveDao {
    private Context context;
    private NewsDBHelper newsDBHelper;
    private UserDao userDao;

    private static final String TAG = "SaveDao";

    private static final String TABLE_NAME = "save";

    private final String[] SAVE_COLUMNS = new String[] {"id", "userId","title","source","urlToImage","url"};

    public SaveDao(Context context){
        this.context = context;
        newsDBHelper = new NewsDBHelper(context);
    }

    /**
     *
     */
    public boolean isDataExist(int id,String title){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = newsDBHelper.getReadableDatabase();

            cursor = db.query(TABLE_NAME,
                    SAVE_COLUMNS,
                    "userId = ? and title = ?",
                    new String[] {String.valueOf(id),title},
                    null, null, null);
            if (cursor.moveToFirst()) {
                return true;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    /**
     * select all
     */
    public List<Save> getAll(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = newsDBHelper.getReadableDatabase();
            // select * from Save
            cursor = db.query(TABLE_NAME, SAVE_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<Save> orderList = new ArrayList<Save>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseSave(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * add
     */
    public boolean insertSave(int userId,String title,String source,String imgurl,String url){
        SQLiteDatabase db = null;

        try {
            db = newsDBHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("userId", userId);
            contentValues.put("title", title);
            contentValues.put("source", source);
            contentValues.put("urlToImage", imgurl);
            contentValues.put("url", url);
            db.insertOrThrow(TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "repeat", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * delete
     */
    public boolean deleteSave(int id) {
        SQLiteDatabase db = null;

        try {
            db = newsDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }


    /**
     * get by userid
     */
    public List<Save> getUserSave(int id){
        SQLiteDatabase db = null;
        Cursor cursor = null;


        try {
            db = newsDBHelper.getReadableDatabase();

            cursor = db.query(TABLE_NAME,
                   SAVE_COLUMNS,
                    "userId = ?",
                    new String[] {String.valueOf(id)},
                    null, null, null);

            if (cursor.getCount() > 0) {
                List<Save> saveList = new ArrayList<Save>(cursor.getCount());
                while (cursor.moveToNext()) {
                    Save order = parseSave(cursor);
                    saveList.add(order);
                }
                return saveList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * transfer to save class
     */
    private Save parseSave(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String source = cursor.getString(cursor.getColumnIndex("source"));
        String imgurl = cursor.getString(cursor.getColumnIndex("urlToImage"));
        String url = cursor.getString(cursor.getColumnIndex("url"));
        Save save = new Save(id,title,source,imgurl,url);
        return save;
    }
}
