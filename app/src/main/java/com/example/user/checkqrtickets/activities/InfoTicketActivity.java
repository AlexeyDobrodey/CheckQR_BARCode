package com.example.user.checkqrtickets.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.fragments.InfoTicketFragment;
import com.example.user.checkqrtickets.entities.Ticket;

/**
 * Created by alexey on 05.07.16.
 */
public class InfoTicketActivity  extends AppCompatActivity{
    private static final String EXTRA_DATA_TICKET = InfoTicketFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_ticket_activity);

        Ticket ticket =  (Ticket)getIntent().getSerializableExtra(EXTRA_DATA_TICKET);
        Toast.makeText(this, ticket.getFIO(), Toast.LENGTH_LONG).show();

        FragmentManager fm = getSupportFragmentManager();

        InfoTicketFragment fragment = (InfoTicketFragment) fm.findFragmentById(R.id.ticket_fragment_container);
        if(fragment == null) {
            fragment = InfoTicketFragment.newInstance(ticket);
            fm.beginTransaction()
                    .add(R.id.ticket_fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context ctx, Ticket ticket) {
        Intent intent = new Intent(ctx, InfoTicketActivity.class);
        intent.putExtra(EXTRA_DATA_TICKET, ticket);
        return intent;
    }
}
