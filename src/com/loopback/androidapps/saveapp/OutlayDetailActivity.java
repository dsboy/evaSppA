package com.loopback.androidapps.saveapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OutlayDetailActivity extends Activity implements
		View.OnClickListener {

	private ImageButton btnPhoto, btnNotes, btnFile;
	TextView txtAccount, txtChargeValue, txtItemValue, txtPlaceValue,
			txtAddressValue, txtDateValue;
	private Outlay outlay;
	private String note;
	public boolean isEuropeanCalendar = true;
	private int outlayId;
	public SaveApp saveApp;
	/*------------------------------------------------------------------------------------------------
	 *--------------------------------------ONCREATE--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public void onCreate(Bundle savedInstanceState) {
		Log.i("OEA", "Init");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outlaydetail);

		loadActivity();
	}
	/*------------------------------------------------------------------------------------------------
	 *--------------------------------------LOAD--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public void loadActivity() {
		Log.i("ODA", "Loading...");
		saveApp = ((SaveApp) getApplicationContext());

		Bundle bundle = this.getIntent().getExtras();
		outlayId = bundle.getInt("Id");

		outlay = new Outlay(outlayId);

		note = null;
		btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
		btnNotes = (ImageButton) findViewById(R.id.btnNotes);
		btnFile = (ImageButton) findViewById(R.id.btnFile);
		txtChargeValue = (TextView) findViewById(R.id.txtChargeValue);
		txtItemValue = (TextView) findViewById(R.id.txtItemValue);
		txtPlaceValue = (TextView) findViewById(R.id.txtPlaceValue);
		txtAddressValue = (TextView) findViewById(R.id.txtAddress);
		txtDateValue = (TextView) findViewById(R.id.txtDate);
		txtAccount = (TextView) findViewById(R.id.txtAccount);

		btnNotes.setOnClickListener(this);
		btnFile.setOnClickListener(this);
		btnPhoto.setOnClickListener(this);

		Log.i("ODA", "Loading...");
		txtChargeValue.setText(String.valueOf((outlay.getCharge())
				+ saveApp.getCurrencySymbol()));
		txtItemValue.setText(outlay.getItemDesc());
		txtPlaceValue.setText(outlay.getPlaceDesc());
		txtAddressValue.setText((outlay.getAddressDesc() == null) ? "No data"
				: outlay.getAddressDesc());
		txtDateValue.setText(Utilities.printDate(outlay.getDate()));

		txtAccount.setText(getString(R.string.strAccount) + ": "
				+ saveApp.getAccountDesc());

		Log.i("ODA", "Loaded");
	}

	public void delete(){
		Outlay outlayToDelete = new Outlay(outlayId);
		outlayToDelete.delete();
		Intent intent = new Intent(this.getApplicationContext(),
				OutlaysListActivity.class);
		Log.i("ODA", "Starting Activity Outlay List");
		startActivity(intent);
	}
	public void onBackPressed() {
		Log.i("ODA", "Back Pressed...");
		Intent intent = new Intent(this.getApplicationContext(),
				OutlaysListActivity.class);
		Log.i("ODA", "Starting Activity Outlay List");
		startActivity(intent);
	}

	public void onClick(View v) {
		if (v == btnNotes) {
			if (outlay.getNote().equals("") || outlay.getNote() == null)
				Toast.makeText(this, "No notes for this movement",
						Toast.LENGTH_SHORT).show();
			else {
				Bundle bundle = new Bundle();
				bundle.putString("Note", (outlay.getNote()));
				bundle.putInt("Id", outlayId);
				Intent intent = new Intent(this.getApplicationContext(),
						NoteViewActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		} else if (v == btnFile) {
			Toast.makeText(this, getString(R.string.strNotSupported),
					Toast.LENGTH_SHORT).show();
		} else if (v == btnPhoto) {
			Toast.makeText(this, getString(R.string.strNotSupported),
					Toast.LENGTH_SHORT).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		note = data.getStringExtra("Note");
		if (note != null)
			Toast.makeText(this, getString(R.string.strNoteAdded),
					Toast.LENGTH_SHORT).show();

	}

	public void text() {
		Toast.makeText(this, "Deleting", Toast.LENGTH_SHORT).show();
	}

	/*------------------------------------------------------------------------------------------------
	 *--------------------------------------MENU------------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i("ODA", "Menu Inflating...");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		Log.i("ODA", "Menu Inflated");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("ODA", "Menu...");
		switch (item.getItemId()) {
		case R.id.mnuEdit:
			Log.i("ODA", "Menu Edit");
			saveApp.outlay.inflate(outlayId);
			Bundle bundle = new Bundle();
			bundle.putBoolean("HasChanges", false);
			bundle.putInt("Id", outlayId);
			Intent intentEdit = new Intent(this.getApplicationContext(),
					OutlayEditActivity.class);
			intentEdit.putExtras(bundle);
			Log.i("ODA", "Start Activity For Editint");
			startActivity(intentEdit);
			return true;
		case R.id.mnuDelete:
			Log.i("ODA", "Menu Delete");
			deleteOutlay();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*------------------------------------------------------------------------------------------------
	 *--------------------------------------ALERT DIALOG------------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public void deleteOutlay() {
		Log.i("ODA", "Delete Alert");
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(getString(R.string.strDialogDelete))
				.setCancelable(true)
				.setPositiveButton(getString(R.string.strYes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								delete();
							}
						})
				.setNegativeButton(getString(R.string.strNo),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'NO' Button
								dialog.cancel();
							}
						});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getString(R.string.strAlert));
		alert.setIcon(R.drawable.alert);
		alert.show();
	}
}
