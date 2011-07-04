package com.loopback.androidapps.saveapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

	private String location;
	
	public MyLocationListener (){
		location=null;
	}
	
	public void  onLocationChanged(Location loc) {

		loc.getLatitude();
		loc.getLongitude();
		String text = "My current location is: " + "Latitud = "
				+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
		setLocation(text);
	}

	public void onProviderDisabled(String provider){
	}

	public void onProviderEnabled(String provider){
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

}/* End of Class MyLocationListener */
