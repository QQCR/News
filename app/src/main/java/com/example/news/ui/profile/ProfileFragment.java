package com.example.news.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.news.AppContext;
import com.example.news.LoginActivity;
import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.database.UserDao;
import com.example.news.models.User;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    Button bLogout,bLogin;
    TextView username, email;
    private AppContext appContext;
    UserDao userDao;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        appContext = (AppContext) getActivity().getApplication();
        userDao = new UserDao(getContext());
        View view;

        if(appContext.getUser().getUsername().equals("admin")){
            view = inflater.inflate(R.layout.require_login, container, false);
            bLogin = view.findViewById(R.id.btnLogin);
            bLogin.setOnClickListener(this);
        }
        else{
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            username = view.findViewById(R.id.username);
            email = view.findViewById(R.id.email);
            bLogout = view.findViewById(R.id.bLogout);
            bLogout.setOnClickListener(this);

            setProfile();
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogout:
                User user = userDao.getUserByName("admin");
                appContext.setUser(user);
                Toast.makeText(getContext(), "logout successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.btnLogin:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }

    }

    public void setProfile(){
        User user = appContext.getUser();
        username.setText("username: " + user.getUsername());
        email.setText("email: "+ user.getEmail());
    }
}
