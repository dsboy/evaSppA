package com.loopback.androidapps.saveapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Album implements SaveAppTable {
	private DBManager dbManager;
	private int id;
	private String description;
	Cursor cursor;
	public static SaveApp saveApp;
	
	public Album() {
		description = null;
		dbManager = saveApp.getDbManager();
	}

	public Album(int _id, String _description) {
		description = _description;
		dbManager = saveApp.getDbManager();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int insert() {
		ContentValues cv = new ContentValues();
		cv.put(DBManager.ALBUM_COLUMN_FILE, description);
		return dbManager.insert(DBManager.ALBUM_TABLE, cv);
	}

	public void update() {
		ContentValues cv = new ContentValues();
		cv.put(DBManager.ALBUM_COLUMN_FILE, description);
		dbManager.update(id, DBManager.ALBUM_TABLE, cv);
	}

	public void delete() {
		dbManager.delete(id, DBManager.ALBUM_TABLE);
	}

	public void inflate(int _id) {
		id = _id;
		cursor = dbManager.select(id, DBManager.ALBUM_TABLE_ID);
		int i = 0;
		Log.i("AC" + "AL", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ALBUM_COLUMN_FILE));
				i++;
			} while (cursor.moveToNext());
	}

	public boolean existDescription(String value) {
		return dbManager.exist(DBManager.ALBUM_TABLE,
				DBManager.ALBUM_COLUMN_FILE, value);
	}

	public int insertOrGetId(String value) {
		if (existDescription(value))
			return dbManager.getId(DBManager.ALBUM_TABLE,
					DBManager.ALBUM_COLUMN_FILE, value);
		else {
			description = value;
			return insert();
		}
	}
}
