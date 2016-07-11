package com.example.user.checkqrtickets.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.fragments.ListPresentationFragment;

public class ListPresentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_representation);
        FragmentManager fm = getSupportFragmentManager();

        ListPresentationFragment fragment = (ListPresentationFragment) fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new ListPresentationFragment();
            fm.beginTransaction()
                   .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context ctx) {
        Intent intent = new Intent(ctx, ListPresentationActivity.class);
        return intent;
    }

}
