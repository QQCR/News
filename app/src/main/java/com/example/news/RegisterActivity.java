package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news.database.UserDao;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText newUsername, newEmail, newPassword;
    Button bRegister;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newUsername = (EditText) findViewById(R.id.newUsername);
        newPassword=(EditText) findViewById(R.id.newPassword);
        newEmail = (EditText) findViewById(R.id.newEmail);

        bRegister = (Button) findViewById(R.id.btn_register);
        bRegister.setOnClickListener(this);

        userDao = new UserDao(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                String username = newUsername.getText().toString();
                String password = newPassword.getText().toString();
                String email = newEmail.getText().toString();

                if(userDao.insertUser(username,password,email)){
                    Toast.makeText(this, "register successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }

    }
}
