package com.example.user.checkqrtickets.entities;

import android.util.Log;

import com.example.user.checkqrtickets.entities.Json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;

/*
*Sorry, limited access to data in connection with safety
*/

public class Client {
	public static String HTTP_SITE = "";
	public static String NOT_CONNECT_TO_INTERNET = "NOT_CONNECT";
	public static String NOT_FIND_TICKET = "NOT_FIND_TICKET";
	
	private static String _csrf = "";
	private static String name_csrf = "";
	
	private static String _cookie = "";
	
	private static  void getCSRF() {
	
	}

	private static void getCSRF(String cookie) {
	
	}

	public static boolean authorization(String name, String password) {
		return true;
	}
	
	public static String getListRepresentation() {
		return null;
	}
	
	public static String findTicket(String hashcode) {
		return null;
	}
	
	public static String checkTicket(String hashcode) {
		return null;
	}
}
