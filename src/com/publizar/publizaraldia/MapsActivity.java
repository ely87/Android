package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.List;

import services.LocationServices;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import domain.LocationPromotion;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ZoomControls;

public class MapsActivity extends MapActivity implements LocationListener {

	/** Called when the activity is first created. */

	EditText txted = null;

	Button btnSimple = null;

	MapView gMapView = null;

	MapController mc = null;

	Drawable defaultMarker = null;

	GeoPoint p = null;

	double latitude = 10.478001, longitude = -66.924891;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_map);

		Intent intent = getIntent();
		String foursquare = intent.getStringExtra("Foursquare");
		LocationServices locationServices = new LocationServices();

		String url = "https://api.foursquare.com/v2/venues/search?ll="
				+ latitude
				+ ","
				+ longitude
				+ "&oauth_token=GXH3U5SYQ0RNQNVATDT1FM5T1VMBI12FBYOX3RPYBV04VVRF"
				+ "&v=20111212&query='" + foursquare + "'&radius=5000";
		ArrayList<LocationPromotion> allLocations = new ArrayList<LocationPromotion>();
		allLocations = locationServices.getLocationVenue(url);
		// Creating and initializing Map

		for (int i = 0; i < allLocations.size(); i++) {
			gMapView = (MapView) findViewById(R.id.mapview);
			p = new GeoPoint((int) (latitude * 1000000),
					(int) (longitude * 1000000));
			
			
		}
		gMapView.setSatellite(true);
		mc = gMapView.getController();
		mc.setCenter(p);
		mc.setZoom(14);
		// Add a location mark
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
		List<Overlay> list = gMapView.getOverlays();
		list.add(myLocationOverlay);

		// Adding zoom controls to Map
		@SuppressWarnings("deprecation")
		ZoomControls zoomControls = (ZoomControls) gMapView.getZoomControls();
		zoomControls.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		gMapView.addView(zoomControls);
		gMapView.displayZoomControls(true);

		// Getting locationManager and reflecting changes over map if distance
		// travel by
		// user is greater than 500m from current location.
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	/* This method is called when use position will get changed */
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			String currentLocation = "Lat: " + lat + " Lng: " + lng;
			txted.setText(currentLocation);
			p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			mc.animateTo(p);
		}
	}

	public void onProviderDisabled(String provider) {
		// required for interface, not used
	}

	public void onProviderEnabled(String provider) {
		// required for interface, not used
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// required for interface, not used
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* User can zoom in/out using keys provided on keypad */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			gMapView.getController().setZoom(gMapView.getZoomLevel() + 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			gMapView.getController().setZoom(gMapView.getZoomLevel() - 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			gMapView.setSatellite(true);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			gMapView.setTraffic(true);
			return true;
		}
		return false;
	}

	/* Class overload draw method which actually plot a marker,text etc. on Map */
	protected class MyLocationOverlay extends com.google.android.maps.Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			Paint paint = new Paint();

			super.draw(canvas, mapView, shadow);
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(p, myScreenCoords);

			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.gmarkerimage);

			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("I am here...", myScreenCoords.x, myScreenCoords.y,
					paint);
			return true;
		}
	}

}
