package com.loopback.androidapps.saveapp;

/**
 * @author Hugo A. Matilla G—mez 
 *Activity que muestra el mapa en pantalla
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapAddressSelectionActivity extends MapActivity implements
		View.OnClickListener {

	private MapView mapView;
	private MapController mapController;
	private int id;
	public TextView txtAddressClicked;
	public List<Overlay> mapOverlays;
	public MapOverlay itemizedoverlay;
	private GeoPoint locationClicked, localization;
	private Button btnSaveAddress, btnDiscardAddress;
	private AddressX address;
	private Outlay outlay;
	private String addressDescSelected;
	private Double latitudeSelected, longitudeSelected;

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
			setContentView(R.layout.mapaddress);
		else
			setContentView(R.layout.mapaddress);

		settings();
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
			setContentView(R.layout.mapaddress);
		else
			setContentView(R.layout.mapaddress);

		loadActivity();
	}

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- LOAD ACTIVITY --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void loadActivity() {
		// Map
		// Init------------------------------------------------------------------
		Log.i("MAP", "Drawing Map...");
		txtAddressClicked = (TextView) findViewById(R.id.txtAddressClicked);
		btnSaveAddress = (Button) findViewById(R.id.btnSaveAddress);
		btnDiscardAddress = (Button) findViewById(R.id.btnDiscardAddress);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		//btnSaveAddress.setOnClickListener(this);
		btnDiscardAddress.setOnClickListener(this);
		settings();

	}

	public void settings() {
		saveApp = ((SaveApp) getApplicationContext());
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getInt("Id");

		// Get Intennt
		// Values------------------------------------------------------------------
		Log.i("MAP", "Set Localization...");
		if (id > 0) {
			outlay = new Outlay(id);
			Integer addressId = outlay.getAddressId();
			address = new AddressX(addressId);
			localization = new GeoPoint((int) ((address.getLongitude()) * 1E6),
					(int) ((address.getLongitude()) * 1E6));
			txtAddressClicked.setText(address.getDescription());
		} else {
			address = new AddressX();
			localization = new GeoPoint((int) ((saveApp.getLatitude()) * 1E6),
					(int) ((saveApp.getLongitude()) * 1E6));
			txtAddressClicked.setText(saveApp.getAddressDesc());
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
		mapController.setZoom(17);
		mapController.animateTo(localization);

		// OverLay
		// Init-----------------------------------------------------------------
		Log.i("MAP", "Overlay...");
		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.changeaccount);
		itemizedoverlay = new MapOverlay(drawable, this);

		// Set
		// Overlay------------------------------------------------------------------
		OverlayItem overlayitem = new OverlayItem(localization,
				"Set the movemnt here?", "Click Save!");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);

		// Re-Draw
		// ------------------------------------------------------------------
		mapView.invalidate();
		Log.i("MAP", "Draw");
	}

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- BUTTONS --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */
	public void onBackPressed() {
		Log.i("MAP", "Back Pressed");
		Bundle bundle = new Bundle();
		bundle.putInt("Id", id);
		Intent intent = new Intent(this.getApplicationContext(),
				ChangeAutogeneratedActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void onClick(View v) {
		// Save Address
		// ------------------------------------------------------------------
		if (v == btnSaveAddress) {
			Bundle bundle = new Bundle();

			bundle.putBoolean("HasBeenChanged", true);
			bundle.putInt("Id", id);
			bundle.putString("AddressDesc", addressDescSelected);
			bundle.putDouble("Latitude", latitudeSelected);
			bundle.putDouble("Longitude", longitudeSelected);

			Intent intent = new Intent(this.getApplicationContext(),
					ChangeAutogeneratedActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);

		}

		// Discard Address
		// ------------------------------------------------------------------
		if (v == btnDiscardAddress) {
			Bundle bundle = new Bundle();

			bundle.putBoolean("HasBeenChanged", false);
			bundle.putInt("Id", id);

			Intent intent = new Intent(this.getApplicationContext(),
					ChangeAutogeneratedActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
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
			dialog.show();
			return true;
		}

		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// ---when user lifts his finger---
			if (event.getAction() == 1) {
				locationClicked = mapView.getProjection().fromPixels(
						(int) event.getX(), (int) event.getY());

				Geocoder geoCoder = new Geocoder(mContext, Locale.getDefault());
				try {
					latitudeSelected = locationClicked.getLatitudeE6() / 1E6;
					longitudeSelected = (locationClicked.getLongitudeE6() / 1E6);
					List<Address> addresses = geoCoder.getFromLocation(
							locationClicked.getLatitudeE6() / 1E6,
							locationClicked.getLongitudeE6() / 1E6, 1);

					String add = "";
					if (addresses.size() > 0) {
						for (int i = 0; i < addresses.get(0)
								.getMaxAddressLineIndex(); i++)
							add += addresses.get(0).getAddressLine(i) + "\n";
					}

					mapOverlays.clear();
					itemizedoverlay.clearOverlay();
					OverlayItem overlayitem = new OverlayItem(locationClicked,
							"Hola", "Estamso aqui");
					itemizedoverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedoverlay);
					addressDescSelected = add;
					txtAddressClicked.setText(addressDescSelected);

				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else
				return false;
		}

	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
