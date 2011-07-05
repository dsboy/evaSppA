package com.loopback.androidapps.saveapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class SaveApp extends Application {

	// Application Variables -------------------------------
	private boolean initiated;
	private DBManager dbManager;
	
	// Account Global Variables -------------------------------
	private int accountId, currencyId, currentBudget, budget, currentDays;
	private String accountDesc, currencySymbol, startDate;
	private boolean isEuropean;
	
	// Address Variables -------------------------------
	private String addressDesc;
	private double latitude, longitude;

	// Date Variables -------------------------------
	private String date;

	// Outlay Variables -------------------------------
	private boolean isAdding;
	public Outlay outlay;

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------OnCREATE--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void onCreate() {
		Log.i("APP", "On Create...");
		// Preferences
		SharedPreferences settings = getSharedPreferences(
				getString(R.string.prefs_name), 0);
		int prefAccount = settings.getInt("Account", 1);
		this.setAccountId(prefAccount);
		boolean isEuropean = settings.getBoolean("IsEuropean", false);
		this.setEuropean(isEuropean);
		
		
		
		Account.saveApp = this;
		AddressX.saveApp = this;
		Album.saveApp = this;
		Currency.saveApp = this;
		DBManager.saveApp = this;
		Item.saveApp = this;
		Outlay.saveApp = this;
		OutlayListItem.saveApp = this;
		Place.saveApp = this;
		Utilities.saveApp = this;
		DBReader.saveApp = this;
		
		
		outlay=new Outlay();
		
		Log.i("APP", "On Created");
		
	}

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------Setters and Getters--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setInitiated(boolean initiated) {
		this.initiated = initiated;
	}

	public boolean isInitiated() {
		return initiated;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrentBudget(int currentBudget) {
		this.currentBudget = currentBudget;
	}

	public int getCurrentBudget() {
		return currentBudget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public int getBudget() {
		return budget;
	}

	public void setCurrentDays(int currentDays) {
		this.currentDays = currentDays;
	}

	public int getCurrentDays() {
		return currentDays;
	}

	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}

	public String getAccountDesc() {
		return accountDesc;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEuropean(boolean isEuropean) {
		this.isEuropean = isEuropean;
	}

	public boolean isEuropean() {
		return isEuropean;
	}

	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}

	public String getAddressDesc() {
		return addressDesc;
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

	public void setDbManager(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	public DBManager getDbManager() {
		return dbManager;
	}

	public void setAdding(boolean isAdding) {
		this.isAdding = isAdding;
	}

	public boolean isAdding() {
		return isAdding;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
}
