package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Item implements SaveAppTable {
	public static DBManager dbManager;
	private int id;
	private String type;
	private String description;
	private Cursor cursor;
	public static SaveApp saveApp;

	public Item() {
		type = null;
		description = null;
		dbManager = saveApp.getDbManager();
	}

	public Item(int id, String type, String description) {
		dbManager = saveApp.getDbManager();
		this.id = id;
		this.type = type;
		this.description = description;
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

	public int insert() {
		Log.i("IT", "Inserting...");
		ContentValues cv = new ContentValues();
		cv.put(DBManager.ITEM_COLUMN_TYPE, type);
		cv.put(DBManager.ITEM_COLUMN_DESC, description);
		return dbManager.insert(DBManager.ITEM_TABLE, cv);
	}

	public void update() {
		Log.i("IT", "Updating...");
		ContentValues cv = new ContentValues();
		cv.put(DBManager.ITEM_COLUMN_TYPE, type);
		cv.put(DBManager.ITEM_COLUMN_DESC, description);
		dbManager.update(id, DBManager.ITEM_TABLE, cv);
	}

	public void delete() {
		Log.i("IT", "Deleting...");
		dbManager.delete(id, DBManager.ITEM_TABLE);
	}

	public void inflate(int _id) {
		id=_id;
		Log.i("IT", "Selecting...");
		cursor = dbManager.select(id, DBManager.ITEM_TABLE_ID);
		int i = 0;
		Log.i("IT", "Reading Cursor...");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ITEM_COLUMN_DESC));
				type = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ITEM_COLUMN_TYPE));
				i++;
			} while (cursor.moveToNext());
	}

	public boolean existType(String value) {
		Log.i("IT", "Exist?...");
		return dbManager.exist(DBManager.ITEM_TABLE,
				DBManager.ITEM_COLUMN_TYPE, value);
	}

	public boolean existDescription(String value) {
		Log.i("IT", "Exist?...");
		return dbManager.exist(DBManager.ITEM_TABLE,
				DBManager.ITEM_COLUMN_DESC, value);
	}

	public int insertOrGetId(String value) {
		Log.i("IT", "Inserting or Getting Id...");
		if (existDescription(value)) {
			Log.i("IT", "Getting Id...");
			return dbManager.getId(DBManager.ITEM_TABLE,
					DBManager.ITEM_COLUMN_DESC, value);
		} else{
			description=value;
			return insert();
		}
	}
	public String[] selectItems() {
		ArrayList<String> items = new ArrayList<String>();
		cursor = dbManager.selectFilter(DBManager.ITEM_TABLE,DBManager.ITEM_COLUMN_DESC,1,"1");
		int i = 0;
		Log.i("AC", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ITEM_COLUMN_DESC));
				items.add(description);
				i++;
			} while (cursor.moveToNext());
		String[] itemStrings = new String[items.size()];
		for (int j = 0; j< items.size();j++) {
			itemStrings[j]=items.get(j);
		}
		
		return itemStrings;
	}
}

