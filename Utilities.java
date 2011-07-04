package com.loopback.androidapps.saveapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.Display;

public class Utilities {
	public static SaveApp saveApp;

	// String to Integer
	public static int stringToInt(String string) {
		Log.i("UT", "String = " + string + ".");
		if (string == null)
			return 0;
		else if (string.compareTo("") == 0)
			return 0;
		else
			return Integer.valueOf(string);
	}

	// String to Double
	public static double stringToDouble(String string) {
		if (string == null)
			return 0;
		else if (string.compareTo("") == 0)
			return 0;
		else
			return Double.valueOf(string);
	}

	public ArrayList<Outlay> readOutlay(Cursor cursor) {
		ArrayList<Outlay> outlayList = new ArrayList<Outlay>();
		int i = 0;
		Log.i("UT", "Read Cursor");
		if (cursor.moveToFirst())
			do {
				Outlay outlay = new Outlay();
				outlay.setId(cursor.getInt(cursor
						.getColumnIndexOrThrow(DBManager.KEY_ID)));
				outlay.setCharge(cursor.getInt(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_CHARGE)));
				outlay.setDate(cursor.getString(cursor
						.getColumnIndexOrThrow(DBManager.OUTLAY_COLUMN_DATE)));
				outlayList.add(outlay);
				i++;
			} while (cursor.moveToNext());
		return outlayList;
	}

	/*------------------------------------------------------------------------------------------------
	 *--------------------------------------DATE--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public static String dateGetter() {
		String pattern = "MM/dd/yyyy HH:mm";
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static Date datePutter(String _date) {
		String pattern = "MM/dd/yyyy HH:mm";
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = new Date();
		try {
			date = dateFormat.parse(_date);
		} catch (ParseException e) {
			Log.i("UT", "Date Format Error");
			e.printStackTrace();
		}

		return date;

	}

	// Select Layout Depending on the rotation
	public static int getLayout(Display display, int orientationPortrait,
			int orientationLandScape) {
		int orientation = display.getOrientation();
		if (orientation == 1 || orientation == 3)
			return orientationLandScape;
		else
			return orientationPortrait;
	}

	public static int getDateUnit(Date date, char unit) {
		String pattern = null;
		switch (unit) {
		case 'y':
			pattern = "yyyy";
			break;
		case 'M':
			pattern = "MM";
			break;
		case 'd':
			pattern = "dd";
			break;
		case 'H':
			pattern = "HH";
			break;
		case 'm':
			pattern = "mm";
			break;
		}
		SimpleDateFormat simpleDateformat = new SimpleDateFormat(pattern);
		return Integer.valueOf(simpleDateformat.format(date));
	}

	public static String getDateUnit(String date, char unit) {
		String result = null;
		switch (unit) {
		case 'y':
			result = date.substring(6, 10);
			break;
		case 'M':
			result = date.substring(0, 2);
			break;
		case 'd':
			result = date.substring(3, 5);
			break;
		case 'H':
			result = date.substring(11, 13);
			break;
		case 'm':
			result = date.substring(13, 15);
			break;
		} // 06/06/1999 34:34
		return result;
	}

	public static String printDate(String date) {
		if (date.substring(0, 1).equals("N") || date.substring(0, 1).equals(""))
			return date;
		else if (date.length() == 0)
			return date;
		else if (saveApp.isEuropean()) {
			return date.substring(3, 5) + "/" + date.substring(0, 2)
					+ date.substring(6, 10) + "/ " + date.substring(11, 13)
					+ ":" + date.substring(13, 15);
		} else
			return date;
	}

	public static String formatDate(int _year, int _month, int _day, int _hour,
			int _minute) {
		String year = String.valueOf(_year);
		String month = String.format("%02d", _month + 1);
		String day = String.format("%02d", _day);
		String hour = String.format("%02d", _hour);
		String minute = String.format("%02d", _minute);

		return month + "/" + day + "/" + year + " " + hour + ":" + minute;
	}

	public static int calculateCurrentBudget() {
		Outlay outlay = new Outlay();
		return saveApp.getBudget() + outlay.sum(saveApp.getAccountId());
	}

	public static int daysUntilToday(String date) {
		Calendar todayCal = Calendar.getInstance();
		Calendar dateCal = Calendar.getInstance();
		dateCal.set(Integer.valueOf(getDateUnit(date, 'y')),
				Integer.valueOf(Utilities.getDateUnit(date, 'M')) - 1,
				Integer.valueOf(Utilities.getDateUnit(date, 'd')));
		long milis1 = todayCal.getTimeInMillis();
		long milis2 = dateCal.getTimeInMillis();
		long diff = milis1 - milis2;
		return (int) (diff / (24 * 60 * 60 * 1000));
	}

	public static double dateToMiliseconds(String date) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.set(Integer.valueOf(getDateUnit(date, 'y')),
				Integer.valueOf(Utilities.getDateUnit(date, 'M')) - 1,
				Integer.valueOf(Utilities.getDateUnit(date, 'd')));
		return dateCal.getTimeInMillis();
	}

	public static void showAccounts(AlertDialog.Builder builder) {
		final CharSequence[] accounts;
		Account account = new Account();
		accounts = account.selectAccounts();
		builder.setTitle("Chooese Account");
		builder.setItems(accounts, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int _item) {
				saveApp.setAccountId(_item + 1);

			}
		});
	}

	public static int numberOfDaysPerMonth(int month, int year) {
		int returnValue = 30;
		switch (month) {
		case 1:
			returnValue = 31;
			break;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
				returnValue = 29;
			else
				returnValue = 28;
			break;
		case 3:
			returnValue = 30;
			break;
		case 4:
			returnValue = 31;
			break;
		case 5:
			returnValue = 30;
			break;
		case 6:
			returnValue = 31;
			break;
		case 7:
			returnValue = 30;
			break;
		case 8:
			returnValue = 31;
			break;
		case 9:
			returnValue = 30;
			break;
		case 10:
			returnValue = 31;
			break;
		case 11:
			returnValue = 30;
			break;
		case 12:
			returnValue = 31;
			break;

		}
		return returnValue;

	}
}
