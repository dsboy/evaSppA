package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OutlaysListActivity extends ListActivity implements
		OnClickListener {

	private DBManager dbManager;
	private ArrayList<Outlay> outlays = new ArrayList<Outlay>();
	private ListAdapter listAdapter; // we will use this "custom adapter" to
										// bind this data to the listView
	private TextView txtAccount, footerDays, footerBudget;
	private boolean landscape = false;

	public SaveApp saveApp;

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- ON CREATE --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int orientation = display.getOrientation();
		if (orientation == 1 || orientation == 3) {
			setContentView(R.layout.outlayslistland);
			landscape = true;
		} else
			setContentView(R.layout.outlayslist);

		loadActivity();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int orientation = display.getOrientation();
		if (orientation == 1 || orientation == 3) {
			setContentView(R.layout.outlayslistland);
			landscape = true;
		} else
			setContentView(R.layout.outlayslist);

		loadActivity();
	}

	public void loadActivity() {
		Log.i("OLA", "Loading...");
		saveApp = ((SaveApp) getApplicationContext());
		dbManager = saveApp.getDbManager();
		// Header
		txtAccount = (TextView) findViewById(R.id.txtAccount);
		txtAccount.setText(getString(R.string.strAccount) + ": "
				+ saveApp.getAccountDesc());

		// Reading Outlays from the DB
		Log.i("OLA", "Reading...");
		Cursor cursor = dbManager.select(-1, DBManager.OUTLAY_TABLE_ID);
		DBReader dbReader = new DBReader();
		dbReader.readOutlay(cursor);
		outlays = dbReader.outlayList;

		// Inflating and bind this adapter to the ListActivity
		Log.i("OLA", "Inflating...");
		listAdapter = new OutlayAdapter(getApplicationContext(), outlays,
				landscape);
		this.setListAdapter(listAdapter);

		// Set Footer
		Log.i("OLA", "Setting Footer...");
		footerBudget = (TextView) findViewById(R.id.txtFooterBudget);
		footerDays = (TextView) findViewById(R.id.txtFooterDays);
		footerDays.setText(String.valueOf(saveApp.getCurrentDays()));
		footerBudget.setText(saveApp.getCurrentBudget()
				+ saveApp.getCurrencySymbol());
		sendTextToWidget(this);
		Log.i("OLA", "Set.");
	}

	/*------------------------------------------------------------------------------------------------
	 *--------------------------------------BUTTONS--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	/* BACK---------------------------------------------------- */
	public void onBackPressed() {
		Log.i("OLA", "Back Pressed...");
		Intent intent = new Intent(this.getApplicationContext(),
				HomeActivity.class);
		Log.i("OLA", "Starting Activity Home");
		startActivity(intent);
	}

	/* REST---------------------------------------------------- */
	public void onListItemClick(ListView parent, View v, int _position, long _id) {
		Log.i("OLA", "ListItemClick"); // debug statement
		int id = outlays.get(_position).getId();

		Bundle bundle = new Bundle();
		bundle.putInt("Id", id);

		Log.i("OLA", "ListItemClick"); // debug statement

		Intent intent = new Intent(this.getApplicationContext(),
				OutlayDetailActivity.class);
		intent.putExtras(bundle);
		Log.i("OLA", "Starting Activity Outlay Detail"); // debug statement
		startActivity(intent);

	}

	/* NOT USED---------------------------------------------------- */
	public void onClick(View v) {
		Log.d("Selections:", "Click"); // debug statement
	}

	public void onClick(DialogInterface dialog, int which) {
		Log.d("Selections:", "Click"); // debug statement

	}

	private void sendTextToWidget(Context context) {
		Intent uiIntent = new Intent();
		uiIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT,footerBudget.getText());
		context.sendBroadcast(uiIntent);
	}

}
