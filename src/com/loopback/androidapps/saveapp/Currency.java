/**
 * 
 */
package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Currency implements SaveAppTable {
	private DBManager dbManager;
	private int id;
	private String symbol;
	private String description;
	private Cursor cursor;
	public static SaveApp saveApp;

	public Currency() {
		dbManager = saveApp.getDbManager();
		this.id = 0;
		this.symbol =null;
		this.description = null;
	}
	public Currency(int _id) {
		dbManager = saveApp.getDbManager();
		this.id = _id;
		this.symbol =null;
		this.description = null;
		inflate(id);
	}

	public Currency(int id, String symbol, String description) {
		dbManager = saveApp.getDbManager();
		this.id = id;
		this.symbol = symbol;
		this.description = description;
	}

	
	public int insert() {
			ContentValues cv = new ContentValues();
			cv.put(DBManager.CURRENCY_COLUMN_SYMBOL, symbol);
			cv.put(DBManager.CURRENCY_COLUMN_DESC, description);
			return dbManager.insert(DBManager.CURRENCY_TABLE, cv);
	}

	public void update() {
		ContentValues cv = new ContentValues();
		cv.put(DBManager.CURRENCY_COLUMN_SYMBOL, symbol);
		cv.put(DBManager.CURRENCY_COLUMN_DESC, description);
		dbManager.update(id, DBManager.CURRENCY_TABLE, cv);
	}

	public void delete() {
		dbManager.delete(id, DBManager.CURRENCY_TABLE);
	}

	public void inflate(int _id) {
		id=_id;
		cursor = dbManager.select(id, DBManager.CURRENCY_TABLE_ID);
		int i = 0;
		Log.i("CU", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.CURRENCY_COLUMN_DESC));
				symbol = cursor
						.getString(cursor
								.getColumnIndexOrThrow(DBManager.CURRENCY_COLUMN_SYMBOL));
				i++;
			} while (cursor.moveToNext());
	}

	public boolean existSymbol(String value) {
		return dbManager.exist(DBManager.CURRENCY_TABLE,
				DBManager.CURRENCY_COLUMN_SYMBOL, value);
	}

	public boolean existDescription(String value) {
		return dbManager.exist(DBManager.CURRENCY_TABLE,
				DBManager.CURRENCY_COLUMN_DESC, value);
	}
	public ArrayList<String> selectCurrencies() {
		ArrayList<String> currencies = new ArrayList<String>();
		cursor = dbManager.selectFilter(DBManager.CURRENCY_TABLE,DBManager.CURRENCY_COLUMN_DESC,1,"1");
		int i = 0;
		Log.i("AC", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.CURRENCY_COLUMN_DESC));
				currencies.add(description);
				i++;
			} while (cursor.moveToNext());
		
		return currencies;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}


}
