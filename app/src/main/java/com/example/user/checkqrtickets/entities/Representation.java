package com.example.user.checkqrtickets.entities;

import java.util.Calendar;
import java.util.Date;


public class Representation {
	private String name;
	private int id;
	private Calendar date;
	private int odeum;
	private boolean active;
	
	public Representation(String name, int id, Calendar date, int odeum, boolean active) {
		this.name = name;
		this.id = id;
		this.date = date;
		this.odeum = odeum;
		this.active = active;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public int getOdeum() {
		return odeum;
	}
	
	public boolean isActive() {
		return active;
	}
}
