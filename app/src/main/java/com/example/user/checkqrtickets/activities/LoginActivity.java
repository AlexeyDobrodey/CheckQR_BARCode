package com.example.user.checkqrtickets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.fragments.LoginFragment;

/**
 * Created by User on 10.05.2016.
 */
public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        FragmentManager fm = getSupportFragmentManager();

        LoginFragment loginFragment = (LoginFragment) fm.findFragmentById(R.id.login_fragment_container);
        if(loginFragment == null) {
            loginFragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.login_fragment_container, loginFragment)
                    .commit();
        }
    }
}
