package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Account implements SaveAppTable {

	private static DBManager dbManager;
	private int id;
	private String description;
	private String startDate;
	private String endDate;
	private int budget;
	private String period;
	private int currencyId;
	private Cursor cursor;
	public static SaveApp saveApp;
	
	public Account() {
		dbManager = saveApp.getDbManager();
		description = null;
		id = 0;
		startDate = null;
		endDate = null;
		budget = 0;
		period = null;
		currencyId = 0;
	}
	public Account(int _id) {
		dbManager = saveApp.getDbManager();
		description = null;
		id = _id;
		startDate = null;
		endDate = null;
		budget = 0;
		period = null;
		currencyId = 0;
		inflate(id);
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

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public int getBudget() {
		return budget;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPeriod() {
		return period;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public int insert() {

		ContentValues cv = new ContentValues();
		cv.put(DBManager.ACCOUNT_COLUMN_BUDGET, budget);
		cv.put(DBManager.ACCOUNT_COLUMN_PERIOD, period);
		cv.put(DBManager.ACCOUNT_COLUMN_DESC, description);
		cv.put(DBManager.ACCOUNT_COLUMN_START_DATE, startDate);
		cv.put(DBManager.ACCOUNT_COLUMN_END_DATE, endDate);
		cv.put(DBManager.ACCOUNT_COLUMN_DESC, currencyId);
		return dbManager.insert(DBManager.ACCOUNT_TABLE, cv);
	}

	public void update() {
		ContentValues cv = new ContentValues();
		cv.put(DBManager.ACCOUNT_COLUMN_BUDGET, budget);
		cv.put(DBManager.ACCOUNT_COLUMN_PERIOD, period);
		cv.put(DBManager.ACCOUNT_COLUMN_DESC, description);
		cv.put(DBManager.ACCOUNT_COLUMN_START_DATE, startDate);
		cv.put(DBManager.ACCOUNT_COLUMN_END_DATE, endDate);
		cv.put(DBManager.ACCOUNT_COLUMN_CURRENCY, currencyId);
		dbManager.update(id, DBManager.ACCOUNT_TABLE, cv);
	}

	public void delete() {
		dbManager.delete(id, DBManager.ACCOUNT_TABLE);
	}

	public void inflate(int _id) {
		id=_id;
		cursor = dbManager.select(id, DBManager.ACCOUNT_TABLE_ID);
		int i = 0;
		Log.i("AC", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_DESC));
				startDate = cursor
						.getString(cursor
								.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_START_DATE));
				endDate = cursor
						.getString(cursor
								.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_END_DATE));
				budget = Utilities.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_BUDGET)));
				period = cursor
						.getString(cursor
								.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_PERIOD));
				currencyId = Utilities.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_CURRENCY)));
				i++;
			} while (cursor.moveToNext());
	}

	public boolean existId(String value) {
		return dbManager.exist(DBManager.ACCOUNT_TABLE,
				DBManager.KEY_ID, value);
	}

	public boolean existDescription(String value) {
		return dbManager.exist(DBManager.ACCOUNT_TABLE,
				DBManager.ACCOUNT_COLUMN_DESC, value);
	}

	public CharSequence[] selectAccounts() {
		ArrayList<String> accounts = new ArrayList<String>();
		cursor = dbManager.selectFilter(DBManager.ACCOUNT_TABLE,DBManager.ACCOUNT_COLUMN_DESC,1,"1");
		int i = 0;
		Log.i("AC", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				description = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.ACCOUNT_COLUMN_DESC));
				accounts.add(description);
				i++;
			} while (cursor.moveToNext());
		CharSequence[] accountsChars = new CharSequence[accounts.size()];
		for (int j = 0; j< accounts.size();j++) {
			accountsChars[j]=accounts.get(j);
		}
			
		return accountsChars;
	}

}
