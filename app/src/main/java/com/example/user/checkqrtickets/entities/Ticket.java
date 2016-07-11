package com.example.user.checkqrtickets.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class Ticket implements  Serializable {
	
	private static final String LOG_TAG = "TICKET";
	
	private String FIO;
	private String nameRepresentation = null;
	private String hashcode = null; 
	
	private int number;
	private int row;
	private int id;
	
	public Ticket(int id, String fio, String nameRepresentation, int number, int row, String hashcode) {
		this.id = id;
		this.FIO = fio;
		this.nameRepresentation = nameRepresentation;
		this.number = number;
		this.row = row;
		this.hashcode = hashcode;
	}
	
	public String getHashCode() {
		return hashcode;
	}
	
	public Ticket(Parcel parcel) {
		id = parcel.readInt();
		FIO = parcel.readString();
		nameRepresentation = parcel.readString();
		number = parcel.readInt();
		row = parcel.readInt();
		hashcode = parcel.readString();
	}
	
	public String getFIO() {
		return FIO;
	}
	
	public String getNameRepresentation() {
		return nameRepresentation;
	}
	
	public int getID() {
		return id;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getRow() {
		return row;
	}
}
