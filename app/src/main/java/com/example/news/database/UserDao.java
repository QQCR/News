package com.example.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.news.models.User;

public class UserDao {
    private Context context;
    private NewsDBHelper newsDBHelper;private static final String TAG = "UserDao";

    private static final String TABLE_NAME = "user";

    private final String[] USER_COLUMNS = new String[] { "userId","username","password","email"};

    public UserDao(Context context){
        this.context = context;
        newsDBHelper = new NewsDBHelper(context);
    }

    /**
     * add a user
     */
    public boolean insertUser(String username, String password, String email){
        SQLiteDatabase db = null;

        if(isUserExist(username)){
            Toast.makeText(context, "existed name", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                db = newsDBHelper.getWritableDatabase();
                db.beginTransaction();

                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("password", password);
                contentValues.put("email", email);
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

        }

        return false;
    }

    /**
     * check username existed
     */
    public boolean isUserExist(String name){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = newsDBHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME,
                    USER_COLUMNS,
                    "username = ?",
                    new String[] {name},
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
     * check username and password
     */
    public boolean isLoginRight(String name,String pass){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = newsDBHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME,
                    USER_COLUMNS,
                    "username = ?",
                    new String[] {name},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String password = cursor.getString(cursor.getColumnIndex("password"));
                if(password.equals(pass)){
                    return true;
                }
                else{
                    Toast.makeText(context, "wrong password ", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
            else {
                Toast.makeText(context, "unregistered user ", Toast.LENGTH_SHORT).show();
                return false;
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
     * update password
     */
    public boolean updatePaaword(String name, String pass){
        SQLiteDatabase db = null;
        try {
            db = newsDBHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put("password", pass);
            db.update(TABLE_NAME,
                    cv,
                    "username = ?",
                    new String[]{name});
            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * update Email
     */
    public boolean updateEmail(String name, String email){
        SQLiteDatabase db = null;
        try {
            db = newsDBHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put("email", email);
            db.update(TABLE_NAME,
                    cv,
                    "username = ?",
                    new String[]{name});
            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    public int getIdbyName(String name){

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = newsDBHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME,
                    USER_COLUMNS,
                    "username = ?",
                    new String[] {name},
                    null, null, null);

            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("userId"));
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
        return 0;
    }

    public User getUserByName(String name){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        User user = null;

        try {
            db = newsDBHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME,
                    USER_COLUMNS,
                    "username = ?",
                    new String[] {name},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                user = new User(username,password,email);
                return user;
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
        return user;
    }











}
