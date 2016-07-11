package com.example.user.checkqrtickets.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.fragments.SearchQRCodeFragment;

public class SearchQRCode extends AppCompatActivity {
    private static final String EXTRA_DATA = SearchQRCode.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qrcode);

        int id_rep = getIntent().getExtras().getInt(EXTRA_DATA);
        FragmentManager fm = getSupportFragmentManager();

        SearchQRCodeFragment fragment = (SearchQRCodeFragment)  fm.findFragmentById(R.id.fragment_container_search_qr_code);
        if(fragment == null) {
            fragment = SearchQRCodeFragment.newInstance(id_rep);
            fm.beginTransaction()
                    .add(R.id.fragment_container_search_qr_code, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context ctx, int id) {
        Intent intent = new Intent(ctx, SearchQRCode.class);
        intent.putExtra(EXTRA_DATA, id);
        return intent;
    }


}
