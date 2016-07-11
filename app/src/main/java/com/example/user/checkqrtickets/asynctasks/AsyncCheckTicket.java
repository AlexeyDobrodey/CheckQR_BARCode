package com.example.user.checkqrtickets.asynctasks;

import android.os.AsyncTask;

import com.example.user.checkqrtickets.entities.Client;


public class AsyncCheckTicket extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... params) {
		String result = Client.checkTicket(params[0]);
		return result;
	}
}
