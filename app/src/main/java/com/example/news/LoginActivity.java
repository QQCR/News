package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.database.UserDao;
import com.example.news.models.User;
import com.example.news.ui.profile.ProfileFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    EditText username, password;
    TextView registerLink;
    UserDao userDao;
    private AppContext appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appContext = (AppContext) getApplication();

        bLogin = (Button) findViewById(R.id.bLogin);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        registerLink = (TextView) findViewById(R.id.registerLink);
        userDao = new UserDao(this);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bLogin:
                String name = username.getText().toString();
                String pass = password.getText().toString();

                if(userDao.isLoginRight(name,pass)){
                    User user = userDao.getUserByName(name);
                    appContext.setUser(user);
                    Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.registerLink:
                startActivity(new Intent(this, RegisterActivity.class));
                break;



        }
    }
}
