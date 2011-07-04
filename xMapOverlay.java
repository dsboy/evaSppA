package com.loopback.androidapps.saveapp;
//package com.loopback.androidapps.saveapp;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import android.content.ContextWrapper;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.location.Address;
//import android.location.Geocoder;
//import android.view.MotionEvent;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.ItemizedOverlay;
//import com.google.android.maps.MapView;
//import com.google.android.maps.Overlay;
//import com.google.android.maps.OverlayItem;
//
//public class MapOverlay extends ItemizedOverlay {
//	
//	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
//	private Context mContext;
//	
//	public MapOverlay(Drawable defaultMarker, Context context) {
//		super(boundCenterBottom(defaultMarker));
//		mContext = context;
//	}
//	public void addOverlay(OverlayItem overlay) {
//	    mOverlays.add(overlay);
//	    populate();
//	}
//	protected OverlayItem createItem(int i) {
//		  return mOverlays.get(i);
//		}
//	public int size() {
//	  return mOverlays.size();
//	}
//	protected boolean onTap(int index) {
//		  OverlayItem item = mOverlays.get(index);
//		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//		  dialog.setTitle(item.getTitle());
//		  dialog.setMessage(item.getSnippet());
//		  dialog.show();
//		  return true;
//		}
//	 public boolean onTouchEvent(MotionEvent event, MapView mapView) 
//     {   
//         //---when user lifts his finger---
//         if (event.getAction() == 1) {                
//             GeoPoint p = mapView.getProjection().fromPixels(
//                 (int) event.getX(),
//                 (int) event.getY());
//
//             Geocoder geoCoder = new Geocoder(
//            		 mContext, Locale.getDefault());
//             try {
//                 List<Address> addresses = geoCoder.getFromLocation(
//                     p.getLatitudeE6()  / 1E6, 
//                     p.getLongitudeE6() / 1E6, 1);
//
//                 String add = "";
//                 if (addresses.size() > 0) 
//                 {
//                     for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
//                          i++)
//                        add += addresses.get(0).getAddressLine(i) + "\n";
//                 }
//                 
//                 Toast.makeText(mContext, add, Toast.LENGTH_SHORT).show();
//                 
//             }
//             catch (IOException e) {                
//                 e.printStackTrace();
//             }   
//             return true;
//         }
//         else                
//             return false;
//     }
//}
