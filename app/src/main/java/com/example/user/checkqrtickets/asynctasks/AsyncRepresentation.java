package com.example.user.checkqrtickets.asynctasks;

import android.os.AsyncTask;

import com.example.user.checkqrtickets.entities.Client;


public class AsyncRepresentation extends AsyncTask<Void, Void, String>{
	@Override
	protected String doInBackground(Void... params) {
		return Client.getListRepresentation();
	}
}
