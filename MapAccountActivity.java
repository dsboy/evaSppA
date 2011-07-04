package com.loopback.androidapps.saveapp;

/**
 * @author Hugo A. Matilla G—mez 
 *Activity que muestra el mapa en pantalla
 */

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapAccountActivity extends MapActivity {

	private MapView mapView;
	private MapController mapController;
	private MapOverlay itemizedoverlay;
	private GeoPoint localization;
	private DBManager dbManager;
	private List<Overlay> mapOverlays;
	private ArrayList<Outlay> outlays = new ArrayList<Outlay>();

	public SaveApp saveApp;

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------ON CREATE & ON ROTATION--------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int orientation = display.getOrientation();
		if (orientation == 1 || orientation == 3)
			setContentView(R.layout.mapaccount);
		else
			setContentView(R.layout.mapaccount);

		loadActivity();
	}

	public void onCreate(Bundle savedInstanceState) {
		Log.i("MAP", "Create...");
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int orientation = display.getOrientation();
		if (orientation == 1 || orientation == 3)
			setContentView(R.layout.mapaccount);
		else
			setContentView(R.layout.mapaccount);

		loadActivity();
	}

	public void onResume(Bundle savedInstanceState) {
		Log.i("MAP", "Create...");
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int orientation = display.getOrientation();
		if (orientation == 1 || orientation == 3)
			setContentView(R.layout.mapaccount);
		else
			setContentView(R.layout.mapaccount);

		loadActivity();
	}

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- LOAD ACTIVITY --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void loadActivity() {
		saveApp = ((SaveApp) getApplicationContext());
		// Map
		// Init------------------------------------------------------------------
		Log.i("MAP", "Drawing Map...");
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		dbManager = saveApp.getDbManager();

		// Get Intennt
		// Values------------------------------------------------------------------

		// Reading Outlays from the DB
		Log.i("OLA", "Reading...");
		Cursor cursor = dbManager.select(-1, DBManager.OUTLAY_TABLE_ID);
		DBReader dbReader = new DBReader();
		dbReader.readOutlay(cursor);
		outlays = dbReader.outlayList;

		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.changeaccount);
		itemizedoverlay = new MapOverlay(drawable, this);
		Outlay outlay = new Outlay();
		AddressX address = new AddressX();
		for (int i = 0; i < outlays.size(); i++) {
			outlay = outlays.get(i);
			address.inflate(outlay.getAddressId());
			localization = new GeoPoint((int) (address.getLatitude() * 1E6),
					(int) (address.getLongitude() * 1E6));
			OverlayItem overlayitem = new OverlayItem(localization,
					i +". "+ outlay.getItemDesc() + ": " + String.valueOf(outlay.getCharge()) + saveApp.getCurrencySymbol(), 
					getString(R.string.strDateAndTimeList) +  ": " + outlay.getDate() + "\n"
							+ getString(R.string.strWhere) +  ": " + outlay.getPlaceDesc());
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		}

		// Get
		// Preferences------------------------------------------------------------------
		Log.i("MAP", "Preferences...");
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		Integer mapStyle = Integer.valueOf(sp.getString("mapStyle", "-1"));

		// Map
		// Edit------------------------------------------------------------------
		Log.i("MAP", "Editionn...");
		mapView.setSatellite(false);
		if (mapStyle == 2)
			mapView.setSatellite(true);
		mapController = mapView.getController();
		mapController.setZoom(12);
		mapController.animateTo(localization);

		// Re-Draw
		// ------------------------------------------------------------------
		mapView.invalidate();
		Log.i("MAP", "Draw");
	}

	/*--******************************************************************************************-------------
	 *-------------------------------------- MapOverlay CLASS --------------------------------------------------
	 *--******************************************************************************************-------------*/

	@SuppressWarnings("rawtypes")
	public class MapOverlay extends ItemizedOverlay {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;

		public MapOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		public void clearOverlay() {
			mOverlays.clear();
		}

		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		public int size() {
			return mOverlays.size();
		}

		protected boolean onTap(int index) {
			Log.i("MAP", "Tap...");
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							return;

						}
					});
			dialog.show();
			return true;
		}

	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}