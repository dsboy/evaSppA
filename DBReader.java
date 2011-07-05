package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

public class DBReader {

	public static SaveApp saveApp;
	private String dateAux;
	public ArrayList<Outlay> outlayList = new ArrayList<Outlay>();
	public ArrayList<Integer> chargeList = new ArrayList<Integer>();
	public ArrayList<Integer> dateList = new ArrayList<Integer>();
	public ArrayList<Integer> chargeListAux = new ArrayList<Integer>();
	public ArrayList<Integer> dateListAux = new ArrayList<Integer>();
	private int itemId, placeId, addressId;
	private Item item;
	private Place place;
	private AddressX address;

	public DBReader() {
	}

	public void readOutlay(Cursor cursor) {

		item = new Item();
		place = new Place();
		address = new AddressX();
		Log.i("HA", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				Outlay outlay = new Outlay();
				outlay.setId(cursor.getInt(cursor
						.getColumnIndexOrThrow(DBManager.KEY_ID)));
				outlay.setCharge(cursor.getInt(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_CHARGE)));
				outlay.setDate(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_DATE)));
				itemId = Utilities.stringToInt(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_ITEM)));
				placeId = Utilities.stringToInt(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_PLACE)));
				addressId = Utilities
						.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_ADDRESS)));

				item.inflate(itemId);
				outlay.setItemId(itemId);
				outlay.setItemDesc(item.getDescription());

				place.inflate(placeId);
				outlay.setPlaceId(placeId);
				outlay.setPlaceDesc(place.getDescription());

				address.inflate(addressId);
				outlay.setAddressId(addressId);
				outlay.setAddressDesc(address.getDescription());

				outlayList.add(outlay);

			} while (cursor.moveToNext());
	}

	public void plotList(Cursor cursor) {
		int i = 0;
		Log.i("HA", "Read Cursor for Plot");
		if (cursor.moveToFirst())
			do {
				Integer charge;
				String date;

				charge = Utilities
						.stringToInt(cursor.getString(cursor
								.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_CHARGE)));
				date = cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_DATE));
				chargeListAux.add(charge);
				dateListAux.add(Integer.valueOf(Utilities
						.getDateUnit(date, 'd')));
				dateAux = date;
				// dateList.add(Utilities.dateToMiliseconds(date));
				i++;

			} while (cursor.moveToNext());

		// Charge 0 until the fisrt date is reached
		if (dateListAux.size()>0) {
			Integer firstDate = dateListAux.get(0);
			// The bufget is add to the first outlay
			dateList.add(1);
			chargeList.add(saveApp.getBudget());
			for (int j = 2; j < firstDate; j++) {
				dateList.add(j);
				chargeList.add(0);
			}
			Integer lastDate = Utilities.numberOfDaysPerMonth(
					Integer.valueOf(Utilities.getDateUnit(dateAux, 'M')),
					Integer.valueOf(Utilities.getDateUnit(dateAux, 'y')));
			Integer y = 0;
			Integer size = dateListAux.size();
			// Charge 0 in the positions where there isn«t outlays until the end
			// of the month
			for (int k = firstDate; k <= lastDate; k++) {
				if ((y < size) && k == dateListAux.get(y)) {
					dateList.add(k);
					chargeList.add(chargeListAux.get(y));
					y++;
				} else {
					dateList.add(k);
					chargeList.add(0);
				}

			}
		}else 
			dateList=null;
			

	}
}
