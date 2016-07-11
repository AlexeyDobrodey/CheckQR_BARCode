package com.example.user.checkqrtickets.asynctasks;

import android.os.AsyncTask;

import com.example.user.checkqrtickets.entities.Client;


public class AsyncLogin extends AsyncTask<String, Void, Boolean>{
	@Override
	protected Boolean doInBackground(String... params) {
		String username = params[0];
		String password = params[1];
		
		if(Client.authorization(username, password)) {
			return true;
		}
		else {
			return false;
		}
	}
}
