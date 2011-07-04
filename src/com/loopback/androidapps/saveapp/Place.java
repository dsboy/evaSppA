package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Place implements SaveAppTable {
	public static DBManager dbManager;
	private int id;
	private String type;
	private String description;
	private Cursor cursor;
	public static SaveApp saveApp;
	
	public Place() {
		type = null;
		description = null;
		dbManager = saveApp.getDbManager();
	}

	public Place(int _id, String _type, String _description) {
		dbManager = saveApp.getDbManager();
		this.id = _id;
		this.type = _type;
		this.description = _description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setSymbol(String symbol) {
		this.type = symbol;
	}

	public String getSymbol() {
		return type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int insert(String _address, double _longitude, double _latitude) {

		// Address
		Log.i("PL", "Inserting Addres...");

		ContentValues cv = new ContentValues();
		cv.put(DBManager.PLACE_COLUMN_TYPE, type);
		cv.put(DBManager.PLACE_COLUMN_DESC, description);
		return dbManager.insert(DBManager.PLACE_TABLE, cv);
	}

	public void update() {
		ContentValues cv = new ContentValues();
		cv.put(DBManager.PLACE_COLUMN_TYPE, type);
		cv.put(DBManager.PLACE_COLUMN_DESC, description);
		dbManager.update(id, DBManager.PLACE_TABLE, cv);
	}

	public void delete() {
		dbManager.delete(id, DBManager.PLACE_TABLE);
	}

	public void inflate(int _id) {
		id = _id;
		cursor = dbManager.select(id, DBManager.PLACE_TABLE_ID);
		int i = 0;
		Log.i("PL", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.PLACE_COLUMN_DESC));
				type = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.PLACE_COLUMN_TYPE));
				i++;
			} while (cursor.moveToNext());
	}

	public boolean existType(String value) {
		return dbManager.exist(DBManager.PLACE_TABLE,
				DBManager.PLACE_COLUMN_TYPE, value);
	}

	public boolean existDescription(String value) {
		return dbManager.exist(DBManager.PLACE_TABLE,
				DBManager.PLACE_COLUMN_DESC, value);
	}

	public int insertOrGetId(String value) {
		if (existDescription(value))
			return dbManager.getId(DBManager.PLACE_TABLE,
					DBManager.PLACE_COLUMN_DESC, value);
		else{
			description=value;
			return insert();
		}
	}

	public int insert() {
		ContentValues cv = new ContentValues();
		cv.put(DBManager.PLACE_COLUMN_TYPE, type);
		cv.put(DBManager.PLACE_COLUMN_DESC, description);
		return dbManager.insert(DBManager.PLACE_TABLE, cv);
	}
	
	public String[] selectPlaces() {
		ArrayList<String> places = new ArrayList<String>();
		cursor = dbManager.selectFilter(DBManager.PLACE_TABLE,DBManager.PLACE_COLUMN_DESC,1,"1");
		int i = 0;
		Log.i("AC", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.PLACE_COLUMN_DESC));
				places.add(description);
				i++;
			} while (cursor.moveToNext());
		String[] placeStrings = new String[places.size()];
		for (int j = 0; j< places.size();j++) {
			placeStrings[j]=places.get(j);
		}
			
		return placeStrings;
	}
}
