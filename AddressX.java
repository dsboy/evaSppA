package com.loopback.androidapps.saveapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class AddressX implements SaveAppTable {

	private DBManager dbManager;
	private int id;
	private double latitude;
	private double longitude;
	private String description;
	private int placeId;
	private Cursor cursor;
	public static SaveApp saveApp;

	public AddressX() {
		dbManager = saveApp.getDbManager();
		id = 0;
		latitude = 0;
		longitude = 0;
		description = null;
		placeId = 0;
	}

	public AddressX(int _id) {
		dbManager = saveApp.getDbManager();
		id = _id;
		latitude = 0;
		longitude = 0;
		description = null;
		placeId = 0;
		inflate(id);
	}

	public AddressX(int _id, double _longitude, double _latitude,
			String _description, int _placeId) {
		dbManager = saveApp.getDbManager();
		id = _id;
		longitude = _longitude;
		latitude = _latitude;
		description = _description;
		placeId = _placeId;
	}

	public AddressX(double _longitude, double _latitude, String _description,
			int _placeId) {
		dbManager = saveApp.getDbManager();
		longitude = _longitude;
		latitude = _latitude;
		description = _description;
		placeId = _placeId;
		insert();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public int getPlaceId() {
		return placeId;
	}

	public int insert() {
		dbManager = saveApp.getDbManager();

		ContentValues cv = new ContentValues();
		cv.put(DBManager.ADDRESS_COLUMN_LATITUDE, latitude);
		cv.put(DBManager.ADDRESS_COLUMN_LONGITUDE, longitude);
		cv.put(DBManager.ADDRESS_COLUMN_DESC, description);
		cv.put(DBManager.ADDRESS_COLUMN_PLACE, placeId);
		return dbManager.insert(DBManager.ADDRESS_TABLE, cv);
	}

	public void update() {
		dbManager = saveApp.getDbManager();
		ContentValues cv = new ContentValues();
		cv.put(DBManager.ADDRESS_COLUMN_LATITUDE, latitude);
		cv.put(DBManager.ADDRESS_COLUMN_LONGITUDE, longitude);
		cv.put(DBManager.ADDRESS_COLUMN_DESC, description);
		cv.put(DBManager.ADDRESS_COLUMN_PLACE, placeId);
		dbManager.update(id, DBManager.ADDRESS_TABLE, cv);
	}

	public void delete() {
		dbManager = saveApp.getDbManager();
		dbManager.delete(id, DBManager.ADDRESS_TABLE);
	}

	public void inflate(int _id) {
		dbManager = saveApp.getDbManager();
		id = _id;
		cursor = dbManager.select(id, DBManager.ADDRESS_TABLE_ID);
		int i = 0;
		Log.i("AD", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ADDRESS_COLUMN_DESC));
				longitude = Utilities
						.stringToDouble(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.ADDRESS_COLUMN_LONGITUDE)));
				latitude = Utilities
						.stringToDouble(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.ADDRESS_COLUMN_LATITUDE)));
				placeId = Utilities
						.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.ADDRESS_COLUMN_PLACE)));
				i++;
			} while (cursor.moveToNext());
	}

	public boolean existLongitud(String value) {
		return dbManager.exist(DBManager.ADDRESS_TABLE,
				DBManager.ADDRESS_COLUMN_LONGITUDE, value);
	}

	public boolean existLatitud(String value) {
		return dbManager.exist(DBManager.ADDRESS_TABLE,
				DBManager.ADDRESS_COLUMN_LATITUDE, value);
	}

	public boolean existDescription(String value) {
		if (value == null)
			value = "";
		return dbManager.exist(DBManager.ADDRESS_TABLE,
				DBManager.ADDRESS_COLUMN_DESC, value);
	}

	public int insertOrGetId(String value) {
		if (existDescription(value))
			return dbManager.getId(DBManager.ADDRESS_TABLE,
					DBManager.ADDRESS_COLUMN_DESC, value);
		else {
			description = value;
			return insert();
		}
	}
}
