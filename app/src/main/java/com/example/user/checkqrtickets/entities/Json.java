package com.example.user.checkqrtickets.entities;

import com.example.user.checkqrtickets.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
	
	public static String[] getSCRF(String jsonObject) {
		String [] result = new String[2];
		try {
			JSONObject obj = new JSONObject(jsonObject);

			result[0] = obj.getString("name");
			result[1] = obj.getString("value");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
	public static List<Representation> getListRepresentation(String jsonObject) {
		List<Representation> listRepresentation = null;
		//System.out.println("JSON:_____________\n" + jsonObject);
		try {
			
			JSONArray jArray = new JSONArray(jsonObject);
			
			listRepresentation = new ArrayList<Representation>();
			for(int i= 0; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				//System.out.println(obj.getString("show"));
				JSONObject objShow = new JSONObject(obj.getString("show"));
				
				//System.out.println(obj1);
				
				
				String name = objShow.getString("name");
				//System.out.println(obj1.getString("name"));
				int id = obj.getInt("id");
				
				long millis = obj.getLong("date");
				Calendar date = null;
				try {
					date = Utils.getDate(millis);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				boolean active = obj.getBoolean("active");

				listRepresentation.add(new Representation(name, id, date, 0, active));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listRepresentation;
	}
	
	public static Ticket getTicket(String jsonObject) {
		Ticket ticket = null;
		try {
			JSONObject jObject = new JSONObject(jsonObject);
			JSONObject account = (JSONObject) jObject.get("account");
			String email = account.getString("email");
			
			JSONObject place = jObject.getJSONObject("place");
			JSONObject odeum = place.getJSONObject("odeum");
			JSONObject event = (JSONObject) odeum.get("event");
			JSONObject show  = (JSONObject) event.get("show");
			
			String nameRepresent = show.getString("name");
			
			int id = event.getInt("id");
			int row = place.getInt("row");
			int number = place.getInt("num");
			
			String hashcode = jObject.getString("id");
			ticket = new Ticket(id, email, nameRepresent, number, row, hashcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return ticket;
	}
}
