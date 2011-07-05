package com.loopback.androidapps.saveapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

	public static SaveApp saveApp;
	
	/********************************************************************************************************/
	/*-----------------------------------TABLE CONSTANTS-----------------------------------------------------/
	/********************************************************************************************************/
	// General Constants
	private static final String DB_NAME = "/mnt/sdcard/saveapp/db";
	private static final int DB_VERSION = 1;
	public static final String KEY_ID = "_id";

	// Currency
	public static final char CURRENCY_TABLE_ID = 'C';
	public static final String CURRENCY_TABLE = "CURRENCY";
	public static final String CURRENCY_COLUMN_SYMBOL = "symbol";
	public static final String CURRENCY_COLUMN_DESC = "description";
	public static final String CURRENCY_TABLE_CREATE = "CREATE TABLE "
			+ CURRENCY_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CURRENCY_COLUMN_SYMBOL
			+ " VARCHAR, " + CURRENCY_COLUMN_DESC + " VARCHAR " + ");";

	// Account
	public static final char ACCOUNT_TABLE_ID = 'A'; // For switching between
														// tables
	public static final int ACCOUNT_TABLE_NUM_COLS = 3;
	public static final String ACCOUNT_TABLE = "ACCOUNT";
	public static final String ACCOUNT_COLUMN_DESC = "description";
	public static final String ACCOUNT_COLUMN_BUDGET = "budget";
	public static final String ACCOUNT_COLUMN_PERIOD = "period";
	public static final String ACCOUNT_COLUMN_START_DATE = "start_date";
	public static final String ACCOUNT_COLUMN_END_DATE = "end_date";
	public static final String ACCOUNT_COLUMN_CURRENCY = "currency_id";
	public static final String ACCOUNT_FK_CURRENCY = "currency_fk";

	public static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE "
			+ ACCOUNT_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_COLUMN_BUDGET
			+ " VARCHAR, " + ACCOUNT_COLUMN_PERIOD + " VARCHAR, "
			+ ACCOUNT_COLUMN_DESC + " VARCHAR, " + ACCOUNT_COLUMN_START_DATE
			+ " VARCHAR, " + ACCOUNT_COLUMN_END_DATE + " VARCHAR, "
			+ ACCOUNT_COLUMN_CURRENCY + " INTEGER, " + "FOREIGN KEY ("
			+ ACCOUNT_COLUMN_CURRENCY + ") REFERENCES " + CURRENCY_TABLE + " ("
			+ KEY_ID + ") " + ");";

	// Item
	public static final char ITEM_TABLE_ID = 'I';
	public static final String ITEM_TABLE = "ITEM";
	public static final String ITEM_COLUMN_TYPE = "type";
	public static final String ITEM_COLUMN_DESC = "description";
	public static final String ITEM_TABLE_CREATE = "CREATE TABLE " + ITEM_TABLE
			+ " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ITEM_COLUMN_TYPE + " VARCHAR, " + ITEM_COLUMN_DESC + " VARCHAR "
			+ ");";

	// Places
	public static final char PLACE_TABLE_ID = 'P';
	public static final String PLACE_TABLE = "PLACE";
	public static final String PLACE_COLUMN_TYPE = "type";
	public static final String PLACE_COLUMN_DESC = "description";
	public static final String PLACE_TABLE_CREATE = "CREATE TABLE "
			+ PLACE_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLACE_COLUMN_TYPE
			+ " VARCHAR, " + PLACE_COLUMN_DESC + " VARCHAR" + ");";

	// Address
	public static final char ADDRESS_TABLE_ID = 'D';
	public static final String ADDRESS_TABLE = "ADDRESS";
	public static final String ADDRESS_COLUMN_LATITUDE = "latitude";
	public static final String ADDRESS_COLUMN_LONGITUDE = "longitue";
	public static final String ADDRESS_COLUMN_DESC = "description";
	public static final String ADDRESS_COLUMN_PLACE = "place_id";
	public static final String ADDRESS_FK_PLACE = "place_fk";
	public static final String ADDRESS_TABLE_CREATE = "CREATE TABLE "
			+ ADDRESS_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ADDRESS_COLUMN_LATITUDE
			+ " VARCHAR, " + ADDRESS_COLUMN_LONGITUDE + " VARCHAR, "
			+ ADDRESS_COLUMN_DESC + " VARCHAR, " + ADDRESS_COLUMN_PLACE
			+ " INTEGER, " + "FOREIGN KEY (" + ADDRESS_COLUMN_PLACE
			+ ") REFERENCES " + PLACE_TABLE + " (" + KEY_ID + ") " + ");";

	// File
	public static final char FILE_TABLE_ID = 'H';
	public static final String FILE_TABLE = "FILE";
	public static final String FILE_COLUMN_TYPE = "type";
	public static final String FILE_COLUMN_PATH = "path";
	public static final String FILE_TABLE_CREATE = "CREATE TABLE " + FILE_TABLE
			+ " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FILE_COLUMN_TYPE + " VARCHAR, " + FILE_COLUMN_PATH + " VARCHAR "
			+ ");";

	// Album
	public static final char ALBUM_TABLE_ID = 'L';
	public static final String ALBUM_TABLE = "ALBUM";
	public static final String ALBUM_COLUMN_FILE = "file_id";
	public static final String ALBUM_FK_FILE = "file_fk";
	public static final String ALBUM_TABLE_CREATE = "CREATE TABLE "
			+ ALBUM_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ALBUM_COLUMN_FILE
			+ " INTEGER, " + "FOREIGN KEY (" + ALBUM_COLUMN_FILE
			+ ") REFERENCES " + FILE_TABLE + " (" + KEY_ID + ") " + ");";

	// Outlay
	public static final char OUTLAY_TABLE_ID = 'T';
	public static final String OUTLAY_TABLE = "OUTLAY";
	public static final String OUTLAY_COLUMN_CHARGE = "charge";
	public static final String OUTLAY_COLUMN_DATE = "date";
	public static final String OUTLAY_COLUMN_NOTES = "notes";

	public static final String OUTLAY_COLUMN_ACCOUNT = "account_id";
	public static final String OUTLAY_COLUMN_PLACE = "place_id";
	public static final String OUTLAY_COLUMN_ADDRESS = "address_id";
	public static final String OUTLAY_COLUMN_ITEM = "item_id";
	public static final String OUTLAY_COLUMN_ALBUM = "album_id";

	public static final String OUTLAY_FK_ACCOUNT = "account_fk";
	public static final String OUTLAY_FK_PLACE = "place_fk";
	public static final String OUTLAY_FK_ADDRESS = "address_fk";
	public static final String OUTLAY_FK_ITEM = "item_fk";
	public static final String OUTLAY_FK_ALBUM = "album_fk";

	public static final String OUTLAY_TABLE_CREATE = "CREATE TABLE "
			+ OUTLAY_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + OUTLAY_COLUMN_CHARGE
			+ " INTEGER, " + OUTLAY_COLUMN_DATE + " DATETIME, "
			+ OUTLAY_COLUMN_NOTES + " VARCHAR, "

			+ OUTLAY_COLUMN_ACCOUNT + " INTEGER, " + OUTLAY_COLUMN_PLACE
			+ " INTEGER, " + OUTLAY_COLUMN_ADDRESS + " INTEGER, "
			+ OUTLAY_COLUMN_ITEM + " INTEGER, " + OUTLAY_COLUMN_ALBUM
			+ " INTEGER, " + "FOREIGN KEY (" + OUTLAY_COLUMN_ACCOUNT
			+ ") REFERENCES " + ACCOUNT_TABLE + " (" + KEY_ID + "), "
			+ "FOREIGN KEY (" + OUTLAY_COLUMN_PLACE + ") REFERENCES "
			+ PLACE_TABLE + " (" + KEY_ID + "), " + "FOREIGN KEY ("
			+ OUTLAY_COLUMN_ADDRESS + ") REFERENCES " + ADDRESS_TABLE + " ("
			+ KEY_ID + "), " + "FOREIGN KEY (" + OUTLAY_COLUMN_ITEM
			+ ") REFERENCES " + ITEM_TABLE + " (" + KEY_ID + "), "
			+ "FOREIGN KEY (" + OUTLAY_COLUMN_ALBUM + ") REFERENCES "
			+ ALBUM_TABLE + " (" + KEY_ID + ") " + ");";

	public static final String OUTLAY_TABLE_TRIGGER_ACCOUNT = "CREATE TRIGGER "
			+ OUTLAY_FK_ACCOUNT + " BEFORE INSERT " + " ON " + OUTLAY_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ OUTLAY_COLUMN_ACCOUNT + " FROM " + ACCOUNT_TABLE + " WHERE "
			+ OUTLAY_COLUMN_ACCOUNT + "=new." + OUTLAY_COLUMN_ACCOUNT
			+ " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String OUTLAY_TABLE_TRIGGER_ITEM = "CREATE TRIGGER "
			+ OUTLAY_FK_ITEM + " BEFORE INSERT " + " ON " + OUTLAY_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ OUTLAY_COLUMN_ITEM + " FROM " + ITEM_TABLE + " WHERE "
			+ OUTLAY_COLUMN_ITEM + "=new." + OUTLAY_COLUMN_ITEM + " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String OUTLAY_TABLE_TRIGGER_PLACE = "CREATE TRIGGER "
			+ OUTLAY_FK_PLACE + " BEFORE INSERT " + " ON " + OUTLAY_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ OUTLAY_COLUMN_PLACE + " FROM " + PLACE_TABLE + " WHERE "
			+ OUTLAY_COLUMN_PLACE + "=new." + OUTLAY_COLUMN_PLACE
			+ " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String OUTLAY_TABLE_TRIGGER_ADDRESS = "CREATE TRIGGER "
			+ OUTLAY_FK_ADDRESS + " BEFORE INSERT " + " ON " + OUTLAY_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ OUTLAY_COLUMN_ADDRESS + " FROM " + ADDRESS_TABLE + " WHERE "
			+ OUTLAY_COLUMN_ADDRESS + "=new." + OUTLAY_COLUMN_ADDRESS
			+ " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String OUTLAY_TABLE_TRIGGER_ALBUM = "CREATE TRIGGER "
			+ OUTLAY_FK_ALBUM + " BEFORE INSERT " + " ON " + OUTLAY_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ OUTLAY_COLUMN_ALBUM + " FROM " + ALBUM_TABLE + " WHERE "
			+ OUTLAY_COLUMN_ALBUM + "=new." + OUTLAY_COLUMN_ALBUM
			+ " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String ACCOUNT_TABLE_TRIGGER_CURRENCY = "CREATE TRIGGER "
			+ ACCOUNT_FK_CURRENCY
			+ " BEFORE INSERT "
			+ " ON "
			+ ACCOUNT_TABLE
			+ " FOR EACH ROW BEGIN"
			+ " SELECT CASE WHEN ((SELECT "
			+ ACCOUNT_COLUMN_CURRENCY
			+ " FROM "
			+ CURRENCY_TABLE
			+ " WHERE "
			+ ACCOUNT_COLUMN_CURRENCY
			+ "=new."
			+ ACCOUNT_COLUMN_CURRENCY
			+ " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String ADDRESS_TABLE_TRIGGER_PLACE = "CREATE TRIGGER "
			+ ADDRESS_FK_PLACE + " BEFORE INSERT " + " ON " + PLACE_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ ADDRESS_COLUMN_PLACE + " FROM " + PLACE_TABLE + " WHERE "
			+ ADDRESS_COLUMN_PLACE + "=new." + ADDRESS_COLUMN_PLACE
			+ " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	public static final String ALBUM_TABLE_TRIGGER_FILE = "CREATE TRIGGER "
			+ ALBUM_FK_FILE + " BEFORE INSERT " + " ON " + ALBUM_TABLE
			+ " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
			+ ALBUM_COLUMN_FILE + " FROM " + FILE_TABLE + " WHERE "
			+ ALBUM_COLUMN_FILE + "=new." + ALBUM_COLUMN_FILE + " ) IS NULL)"
			+ " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;";

	/********************************************************************************************************/
	/*----------------------------------CONSTRUCTOR & OPEN & CLOSE-------------------------------------------/
	/********************************************************************************************************/

	// Variable to hold the database instance
	public SQLiteDatabase db;
	private DBManager dbManager;


	/********************************************************************************************************/
	/*---------------------------------------DDL-------------------------------------------------------------/
	/*----------------------------(Data Definition Language)-------------------------------------------------/
	/********************************************************************************************************/

	// CONSTRUCTOR
//	// Context of the application using the database.
//	private Context context;
//	public DBManager(Context context, String name, CursorFactory factory,
//			int version) {
//		super(context, DB_NAME, factory, DB_VERSION);
//		this.context = context;
//		Log.i("DB", "Contructor 1");
//	}

	public DBManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Log.i("DB", "Constructor 2");

	}

	// CREATE
	public void onCreate(SQLiteDatabase _db) {
		Log.i("DB", "Create Tables...");

		// CURRENCY
		_db.execSQL(ACCOUNT_TABLE_CREATE);
		Log.i("DB", "Created ACCOUNT.");
		_db.execSQL(ITEM_TABLE_CREATE);
		Log.i("DB", "Created OBJECT.");
		_db.execSQL(CURRENCY_TABLE_CREATE);
		Log.i("DB", "Created CURRENCY.");
		_db.execSQL(ADDRESS_TABLE_CREATE);
		Log.i("DB", "Created ADDRESS.");
		_db.execSQL(PLACE_TABLE_CREATE);
		Log.i("DB", "Created PLACE.");
		_db.execSQL(FILE_TABLE_CREATE);
		Log.i("DB", "Created PHOTP.");
		_db.execSQL(ALBUM_TABLE_CREATE);
		Log.i("DB", "Created ALBUM.");
		_db.execSQL(OUTLAY_TABLE_CREATE);
		Log.i("DB", "Created OUTLAY.");

		// _db.execSQL(OUTLAY_TABLE_TRIGGER_ITEM);
		// Log.i("DB", "Created OBJECT TRIGGER.");
		// _db.execSQL(OUTLAY_TABLE_TRIGGER_ACCOUNT);
		// Log.i("DB", "Created CURRENCY TRIGGER.");
		// _db.execSQL(OUTLAY_TABLE_TRIGGER_PLACE);
		// Log.i("DB", "Created PLACE TRIGGER.");
		// _db.execSQL(OUTLAY_TABLE_TRIGGER_ADDRESS);
		// Log.i("DB", "Created PLACE TRIGGER.");
		// _db.execSQL(OUTLAY_TABLE_TRIGGER_ALBUM);
		// Log.i("DB", "Created ALBUM TRIGGER.");
		// _db.execSQL(ADDRESS_TABLE_TRIGGER_PLACE);
		// Log.i("DB", "Created ADDRESS TRIGGER.");
		// _db.execSQL(ALBUM_TABLE_TRIGGER_FILE);
		// Log.i("DB", "Created FILE TRIGGER.");
		// Log.i("DB", "Tables created.");
		//
		insertInitialCurrencies(_db);
		insertInitialAccount(_db);
		insertInitialItem(_db);
		insertInitialAddress(_db);
		insertInitialPlace(_db);
		insertInitialFile(_db);
		insertInitialAlbum(_db);

	}

	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		Log.i("DB", "Upgrade");
		_db.execSQL("DROP TABLE IF EXISTS " + OUTLAY_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + CURRENCY_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + ADDRESS_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + FILE_TABLE);
		_db.execSQL("DROP TABLE IF EXISTS " + ALBUM_TABLE);
		_db.execSQL("DROP TRIGGER IF EXISTS " + OUTLAY_TABLE_TRIGGER_ACCOUNT);
		_db.execSQL("DROP TRIGGER IF EXISTS " + OUTLAY_TABLE_TRIGGER_ITEM);
		_db.execSQL("DROP TRIGGER IF EXISTS " + OUTLAY_TABLE_TRIGGER_PLACE);
		_db.execSQL("DROP TRIGGER IF EXISTS " + OUTLAY_TABLE_TRIGGER_ADDRESS);
		_db.execSQL("DROP TRIGGER IF EXISTS " + OUTLAY_TABLE_TRIGGER_ALBUM);
		_db.execSQL("DROP TRIGGER IF EXISTS " + ACCOUNT_TABLE_TRIGGER_CURRENCY);
		_db.execSQL("DROP TRIGGER IF EXISTS " + ALBUM_TABLE_TRIGGER_FILE);
		_db.execSQL("DROP TRIGGER IF EXISTS " + ADDRESS_TABLE_TRIGGER_PLACE);
		onCreate(_db);
		// Log the version upgrade.
		/*
		 * Log.w('TaskDBAdapter', 'Upgrading from version ' + _oldVersion + 'to'
		 * + _newVersion + ', which will destroy all old data');
		 */
	}

	// OPEN DB
	public void open() throws SQLException {
		Log.i("DB", "Open");
		try {
			db = dbManager.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbManager.getReadableDatabase();
		}
	}

	// CLOSE
	public void close() {
		Log.i("DB", "Closing");
		db.close();
		Log.i("DB", "Closed");
	}

	/********************************************************************************************************/
	/*---------------------------------------(DML) ----------------------------------------------------------/
	/*-----------------------------Data Manipulation Language -----------------------------------------------/
	/********************************************************************************************************/

	// Insert
	public int insert(String table, ContentValues cv) {
		Log.i("DB", "Inserting in " + table + "...");
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(table, KEY_ID, cv);
		Cursor cursor = db.rawQuery("SELECT COUNT(1)  FROM " + table, null);
		cursor.moveToFirst();
		Log.i("DB", "Inserted.");
		return Integer.valueOf(cursor.getString(0));
	}

	// Update
	public void update(int id, String table, ContentValues cv) {
		Log.i("DB", "Updating " + table + "...");
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(table, cv, KEY_ID + "=?", new String[] { String.valueOf(id) });
		Log.i("DB", "Updated.");
	}

	// Delete
	public void delete(int id, String table) {
		Log.i("DB", "Deleting " + table + "...");
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table, KEY_ID + "=?", new String[] { String.valueOf(id) });
		Log.i("DB", "Deleted.");
	}

	// Select
	public Cursor select(int id, char tableId) {
		Log.i("DB", "Selecting data...");

		String table = null;
		Cursor cursor;
		SQLiteDatabase db = this.getWritableDatabase();

		switch (tableId) {
		case ACCOUNT_TABLE_ID:
			table = ACCOUNT_TABLE;
			break;
		case ITEM_TABLE_ID:
			table = ITEM_TABLE;
			break;
		case CURRENCY_TABLE_ID:
			table = CURRENCY_TABLE;
			break;
		case PLACE_TABLE_ID:
			table = PLACE_TABLE;
			break;
		case ADDRESS_TABLE_ID:
			table = ADDRESS_TABLE;
			break;
		case ALBUM_TABLE_ID:
			table = ALBUM_TABLE;
			break;
		case FILE_TABLE_ID:
			table = FILE_TABLE;
			break;
		case OUTLAY_TABLE_ID:
			// Select only the outlays of the account selected. and order it
			// TODO
			table = OUTLAY_TABLE;
			if (id == -1)
				table = OUTLAY_TABLE + " WHERE " + OUTLAY_COLUMN_ACCOUNT
						+ " = '" + String.valueOf(saveApp.getAccountId())
						+ "' ORDER BY " + OUTLAY_COLUMN_DATE;
			break;
		}
		if (id == -1)
			cursor = db.rawQuery("SELECT * FROM " + table, null);
		else
			cursor = db.rawQuery("SELECT * FROM " + table + " WHERE " + KEY_ID
					+ " = '" + String.valueOf(id) + "'", null);
		Log.i("DB", "Selected.");
		return cursor;
	}

	public Cursor selectPlotList() {
		Log.i("DB", "Select...");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT SUM(" + OUTLAY_COLUMN_CHARGE
				+ ") as "+OUTLAY_COLUMN_CHARGE + ", SUBSTR(" + OUTLAY_COLUMN_DATE + ",0,11) as "+ OUTLAY_COLUMN_DATE +" FROM "
				+ OUTLAY_TABLE + " WHERE " + OUTLAY_COLUMN_ACCOUNT + " = '"
				+ String.valueOf(saveApp.getAccountId()) + "' GROUP  BY "
				+ "SUBSTR(" + OUTLAY_COLUMN_DATE + ",0,11) " + " ORDER BY "
				+ "SUBSTR(" + OUTLAY_COLUMN_DATE + ",0,11)", null);
		cursor.moveToFirst();
		return cursor;
		// SELECT sum(charge),substr(date,0,11) FROM outlay group by
		// substr(date,0,11) order by substr(date,0,11)
	}

	public int getId(String table, String column, String value) {
		Log.i("DB", "Selecting data...");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + table
				+ " WHERE " + column + "= '" + value + "'", null);
		cursor.moveToFirst();
		Log.i("DB", "Selected.");
		return Integer.valueOf(cursor.getString(0));

	}

	// Exist
	public boolean exist(String table, String column, String value) {
		Log.i("DB", "Checking Values Existence...");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(" + column + ") FROM "
				+ table + " WHERE " + column + "= '" + value + "'", null);
		cursor.moveToFirst();
		String count = cursor.getString(0);
		Log.i("DB", "Selected.");
		return (Integer.valueOf(count) > 0);
	}

	// Select Filter
	public Cursor selectFilter(String table, String column, int filter,
			String columnFilter) {
		Log.i("DB", "Select...");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery("SELECT " + column + " FROM " + table, null);
		cursor.moveToFirst();
		return cursor;
	}

	// Ssum
	public int sum(String table, String column, int filter, String columnFilter) {
		Log.i("DB", "Sum...");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT SUM(" + column + ") FROM " + table
				+ " WHERE " + columnFilter + "= '" + filter + "'", null);
		cursor.moveToFirst();
		String sum = cursor.getString(0);
		if (sum == null)
			return 0;
		Log.i("DB", "Selected.");
		return Integer.valueOf(sum);
	}

	/*------------------------------------------------
	--------------------CURRENCY----------------------
	--------------------------------------------------*/

	public static void insertInitialCurrencies(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial Currencies...");
		ContentValues cv = new ContentValues();
		cv.put(CURRENCY_COLUMN_SYMBOL, "$");
		cv.put(CURRENCY_COLUMN_DESC, "Dollar");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "Û");
		cv.put(CURRENCY_COLUMN_DESC, "Euro");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "£");
		cv.put(CURRENCY_COLUMN_DESC, "Pound");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "´");
		cv.put(CURRENCY_COLUMN_DESC, "Yen");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "´");
		cv.put(CURRENCY_COLUMN_DESC, "Yuan");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "W");
		cv.put(CURRENCY_COLUMN_DESC, "Won");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "$");
		cv.put(CURRENCY_COLUMN_DESC, "Peso");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		cv.put(CURRENCY_COLUMN_SYMBOL, "");
		cv.put(CURRENCY_COLUMN_DESC, "None");
		_db.insert(CURRENCY_TABLE, KEY_ID, cv);
		Log.i("DB", "Currencies Inserted");
	}

	/*------------------------------------------------
	--------------------ACCOUNT----------------------
	--------------------------------------------------*/
	public static void insertInitialAccount(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial Account...");
		ContentValues cv = new ContentValues();
		cv.put(ACCOUNT_COLUMN_DESC, "MAIN");
		cv.put(ACCOUNT_COLUMN_BUDGET, "0");
		cv.put(ACCOUNT_COLUMN_PERIOD, "Month");
		cv.put(ACCOUNT_COLUMN_START_DATE, Utilities.dateGetter());
		cv.put(ACCOUNT_COLUMN_END_DATE, "None      ");
		cv.put(ACCOUNT_COLUMN_CURRENCY, "1");
		_db.insert(ACCOUNT_TABLE, KEY_ID, cv);
		Log.i("DB", "Inserted");
	}

	public static void insertInitialItem(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial Item...");
		ContentValues cv = new ContentValues();
		cv.put(ITEM_COLUMN_TYPE, "");
		cv.put(ITEM_COLUMN_DESC, "");
		_db.insert(ITEM_TABLE, KEY_ID, cv);
		Log.i("DB", "Inserted");
	}

	public static void insertInitialPlace(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial C...");
		ContentValues cv = new ContentValues();
		cv.put(PLACE_COLUMN_TYPE, "");
		cv.put(PLACE_COLUMN_DESC, "");
		_db.insert(PLACE_TABLE, KEY_ID, cv);
		Log.i("DB", "Inserted");
	}

	public static void insertInitialAddress(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial Address...");
		ContentValues cv = new ContentValues();
		cv.put(ADDRESS_COLUMN_DESC, "");
		cv.put(ADDRESS_COLUMN_PLACE, "");
		cv.put(ADDRESS_COLUMN_LATITUDE, "0");
		cv.put(ADDRESS_COLUMN_LONGITUDE, "0");
		_db.insert(ADDRESS_TABLE, KEY_ID, cv);
		Log.i("DB", "Inserted");
	}

	public static void insertInitialFile(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial File...");
		ContentValues cv = new ContentValues();
		cv.put(FILE_COLUMN_TYPE, "");
		cv.put(FILE_COLUMN_PATH, "");
		_db.insert(FILE_TABLE, KEY_ID, cv);
		Log.i("DB", "Inserted");
	}

	public static void insertInitialAlbum(SQLiteDatabase _db) {
		Log.i("DB", "Inserting Initial Album...");
		ContentValues cv = new ContentValues();
		cv.put(ALBUM_COLUMN_FILE, "");
		_db.insert(ALBUM_TABLE, KEY_ID, cv);
		Log.i("DB", "Inserted");
	}
}
