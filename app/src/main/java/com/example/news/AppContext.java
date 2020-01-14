package com.example.news;

import android.app.Application;

import com.example.news.database.UserDao;
import com.example.news.models.User;


public class AppContext extends Application {
    private User user;
    private UserDao userDao;

    public void onCreate() {
        //初始化字符串
        super.onCreate();
        user = new User("admin","123456","admin@example.com");
        userDao = new UserDao(this);
        if(!userDao.isUserExist("admin")){
            userDao.insertUser("admin","123456","admin@example.com");
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }




}
