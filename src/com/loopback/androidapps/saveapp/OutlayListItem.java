package com.loopback.androidapps.saveapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OutlayListItem extends LinearLayout {

	private LinearLayout rootContainer; // root container that is used to stack
	private TextView txtId, txtDate, txtTime, txtItem, txtPlace, txtCharge;
	private String date, charge, item, place, currencySymbol;

	public static SaveApp saveApp;
	
	public OutlayListItem(Context _context, Outlay outlay, int position, boolean landscape) {
		// Set Context
		super(_context);
		Log.i("OLI", "Init...");
		// Assign Values
		date = outlay.getDate();
		item = String.valueOf(outlay.getItemDesc());
		place = String.valueOf(outlay.getPlaceDesc());
		charge = String.valueOf(outlay.getCharge());
		currencySymbol = saveApp.getCurrencySymbol();

		// Inflate: Build the view from the XML.
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (landscape)
		rootContainer = (LinearLayout) inflater.inflate(
				R.layout.outlaylistitemland,
				(ViewGroup) findViewById(R.id.root_container));
		else
		rootContainer = (LinearLayout) inflater.inflate(
				R.layout.outlaylistitem,
				(ViewGroup) findViewById(R.id.root_container));

		// Get Views
		txtId = (TextView) rootContainer.findViewById(R.id.ide);
		txtDate = (TextView) rootContainer.findViewById(R.id.date);
		txtTime = (TextView) rootContainer.findViewById(R.id.time);
		txtItem = (TextView) rootContainer.findViewById(R.id.item);
		txtPlace = (TextView) rootContainer.findViewById(R.id.place);
		txtCharge = (TextView) rootContainer.findViewById(R.id.charge);

		// Set Text
		txtId.setTextColor(getResources().getColor(R.color.black));
		txtDate.setTextColor(getResources().getColor(R.color.black));
		txtTime.setTextColor(getResources().getColor(R.color.black));
		txtItem.setTextColor(getResources().getColor(R.color.black));
		txtPlace.setTextColor(getResources().getColor(R.color.black));
		txtCharge.setTextColor(getResources().getColor(R.color.black));
		if (Integer.valueOf(charge) > 0)
			txtCharge.setTextColor(getResources().getColor(R.color.green));
		//TODO Change color depending on the Charge.
		//else if ((Integer.valueOf(charge) < 0)
			//	&& (-1 * (Integer.valueOf(charge)) < (HomeActivity.ACCOUNT_BUDGET / 5)))
			//txtCharge.setTextColor(getResources().getColor(R.color.red));

		txtId.setText(String.valueOf(position));
		txtDate.setText((date == null) ? "No data" : Utilities.printDate(date).substring(0, 10));
		txtTime.setText((date == null) ? "No data" : date.substring(11, 16));
		txtItem.setText((item == null) ? "No data" : item);
		txtPlace.setText((place == null) ? "No data" : place);
		txtCharge.setText(charge + currencySymbol);

		// Add View
		this.addView(rootContainer);
		Log.i("OLI", "Initiated");

	}

}
