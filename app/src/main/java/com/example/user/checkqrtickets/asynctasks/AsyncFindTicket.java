package com.example.user.checkqrtickets.asynctasks;

import android.os.AsyncTask;

import com.example.user.checkqrtickets.entities.Client;


public class AsyncFindTicket extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... params) {
		return Client.findTicket(params[0]);
	}
}
