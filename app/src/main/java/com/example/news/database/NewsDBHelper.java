package com.example.news.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NewsDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "news.db";
    private String create_save_sql, create_user_sql,drop_save_sql,drop_user_sql;


    public NewsDBHelper(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        create_save_sql = "CREATE TABLE `save` (\n" +
                "  `id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  `userId` int(11) DEFAULT NULL,\n" +
                "  `title` varchar(100) DEFAULT NULL,\n" +
                "  `source` varchar(100) DEFAULT NULL,\n" +
                "  `urlToImage` varchar(100) DEFAULT NULL,\n" +
                "  `url` varchar(100) DEFAULT NULL" +
                ")";

        create_user_sql = "CREATE TABLE `user` (\n" +
                "  `userId` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  `username` varchar(100) DEFAULT NULL,\n" +
                "  `password` varchar(100) DEFAULT NULL,\n" +
                "  `email` varchar(100) DEFAULT NULL" +
                ")";

        sqLiteDatabase.execSQL(create_save_sql);
        sqLiteDatabase.execSQL(create_user_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        drop_save_sql = "DROP TABLE IF EXISTS save";
        drop_user_sql = "DROP TABLE IF EXISTS user";

        sqLiteDatabase.execSQL(drop_save_sql);
        sqLiteDatabase.execSQL(drop_user_sql);

        onCreate(sqLiteDatabase);

    }
}
