package com.loopback.androidapps.saveapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Outlay implements SaveAppTable {

	private int id;
	private int charge;
	private String note;
	private String date;

	private int accountId;
	private int itemId;
	private int placeId;
	private int addressId;
	private int albumId;

	private String accountDesc;
	private String itemDesc;
	private String placeDesc;
	private String addressDesc;
	private String albumDesc;

	private Item item;
	private Place place;
	private AddressX address;
	private Album album;

	private DBManager dbManager;
	private Cursor cursor;
	public static SaveApp saveApp;

	public Outlay() {
		dbManager = saveApp.getDbManager();
		id = 0;
		charge = 0;
		date = "";
		note = "";

		accountId = 0;
		itemId = 0;
		placeId = 0;
		addressId = 0;
		albumId = 0;

		itemDesc = "";
		placeDesc = "";
		addressDesc = "";
		setAccountDesc("");

		item = new Item();
		place = new Place();
		address = new AddressX();
		album = new Album();
	}

	public Outlay(int _id, int _charge, int _accountId) {
		dbManager = saveApp.getDbManager();
		id = _id;
		charge = _charge;
		accountId = _accountId;

		date = "";
		note = "";

		itemId = 0;
		placeId = 0;
		addressId = 0;
		albumId = 0;

		itemDesc = "";
		placeDesc = "";
		addressDesc = "";
		setAccountDesc("");

		item = new Item();
		place = new Place();
		address = new AddressX();
		album = new Album();
	}

	public Outlay(int _id) {
		dbManager = saveApp.getDbManager();
		id = _id;
		charge = 0;
		date = "";
		note = "";

		accountId = 0;
		itemId = 0;
		placeId = 0;
		addressId = 0;
		albumId = 0;

		itemDesc = "";
		placeDesc = "";
		addressDesc = "";
		setAccountDesc("");

		item = new Item();
		place = new Place();
		address = new AddressX();
		album = new Album();
		inflate(id);
	}

	/***
	 * For each description, if it exist it is infalted the object from the id.
	 * Otherwise it is created a new entrance in the corresponding Table.
	 * */
	public void insert(int _accountId, int _charge, String _date, String _note,
			String _itemDesc, String _placeDesc, String _albumDesc,
			String _addressDesc, double _longitude, double _latitude) {
		dbManager = saveApp.getDbManager();
		charge = _charge;
		date = _date;
		note = _note;

		Log.i("OU", "Inserting Ids...");

		itemId = item.insertOrGetId(_itemDesc);
		albumId = album.insertOrGetId(_albumDesc);
		placeId = place.insertOrGetId(_placeDesc);
		addressId = address.insertOrGetId(_addressDesc);
		address.inflate(addressId);
		if (address.getLatitude() == 0) {
			address.setLatitude(Double.valueOf(_latitude));
			address.setLongitude(Double.valueOf(_longitude));
			address.update();
		}

		Log.i("OU", "Inserting Outlay...");
		ContentValues cv = new ContentValues();
		cv.put(DBManager.OUTLAY_COLUMN_CHARGE, charge);
		cv.put(DBManager.OUTLAY_COLUMN_DATE, date);
		cv.put(DBManager.OUTLAY_COLUMN_NOTES, note);
		cv.put(DBManager.OUTLAY_COLUMN_ITEM, itemId);
		cv.put(DBManager.OUTLAY_COLUMN_PLACE, placeId);
		cv.put(DBManager.OUTLAY_COLUMN_ADDRESS, addressId);
		cv.put(DBManager.OUTLAY_COLUMN_ALBUM, albumId);
		cv.put(DBManager.OUTLAY_COLUMN_ACCOUNT, saveApp.getAccountId());
		dbManager.insert(DBManager.OUTLAY_TABLE, cv);
		Log.i("OU", "Inserted");
	}

	public void update() {
		dbManager = saveApp.getDbManager();
		Log.i("OU", "Updatinging Ids...");
		itemId = item.insertOrGetId(itemDesc);
		if ((addressDesc == null) || (addressDesc.compareTo("") == 0))
			addressId = 1;
		else
			addressId = address.insertOrGetId(addressDesc);
		placeId = place.insertOrGetId(placeDesc);
		albumId = album.insertOrGetId(albumDesc);

		Log.i("OU", "Updating Outlay...");

		ContentValues cv = new ContentValues();
		cv.put(DBManager.OUTLAY_COLUMN_CHARGE, charge);
		cv.put(DBManager.OUTLAY_COLUMN_DATE, date);
		cv.put(DBManager.OUTLAY_COLUMN_NOTES, note);
		cv.put(DBManager.OUTLAY_COLUMN_ITEM, itemId);
		cv.put(DBManager.OUTLAY_COLUMN_PLACE, placeId);
		cv.put(DBManager.OUTLAY_COLUMN_ADDRESS, addressId);
		cv.put(DBManager.OUTLAY_COLUMN_ALBUM, albumId);
		cv.put(DBManager.OUTLAY_COLUMN_ACCOUNT, saveApp.getAccountId());
		dbManager.update(id, DBManager.OUTLAY_TABLE, cv);

		Log.i("OU", "Updated");
	}

	public void delete() {
		dbManager.delete(id, DBManager.OUTLAY_TABLE);
	}

	public void inflate(int _id) {
		dbManager = saveApp.getDbManager();
		id = _id;
		cursor = dbManager.select(id, DBManager.OUTLAY_TABLE_ID);
		int i = 0;
		Log.i("OU", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				charge = Utilities
						.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_CHARGE)));

				note = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_NOTES));

				date = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_DATE));

				accountId = Utilities
						.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_ACCOUNT)));

				itemId = Utilities.stringToInt(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_ITEM)));

				placeId = Utilities.stringToInt(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_PLACE)));

				albumId = Utilities.stringToInt(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_ALBUM)));

				addressId = Utilities
						.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_ADDRESS)));

				setAccountDesc(saveApp.getAccountDesc());

				if (itemId != 0) {
					item.inflate(itemId);
					itemDesc = item.getDescription();
				}
				if (placeId != 0) {
					place.inflate(placeId);
					placeDesc = place.getDescription();
				}
				if (albumId != 0) {
					// album.inflate(albumId);
					albumDesc = "";// album.getDescription();
				}
				if (addressId != 0) {
					address.inflate(addressId);
					addressDesc = address.getDescription();
				}

				i++;
			} while (cursor.moveToNext());
	}

	public int insert() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int sum(int accountId) {
		Log.i("IT", "Sum...");
		int sum = dbManager.sum(DBManager.OUTLAY_TABLE,
				DBManager.OUTLAY_COLUMN_CHARGE, accountId,
				DBManager.OUTLAY_COLUMN_ACCOUNT);
		return sum;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public int getCharge() {
		return charge;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setAccount(int accountId) {
		this.accountId = accountId;
	}

	public int getAccount() {
		return accountId;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}

	public String getAccountDesc() {
		return accountDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setPlaceDesc(String placeDesc) {
		this.placeDesc = placeDesc;
	}

	public String getPlaceDesc() {
		return placeDesc;
	}

	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}

	public String getAddressDesc() {
		return addressDesc;
	}

}
