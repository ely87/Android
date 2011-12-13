package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.List;

import services.LocationServices;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import domain.LocationPromotion;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MapsActivity extends MapActivity implements LocationListener {

	private MapView map = null;
	private MyLocationOverlay me = null;
	private SitesOverlay sites = null;
	private ArrayList<LocationPromotion> allLocations;
	private String foursquare;
	private String latitude = "10.478001";
	private String longitude = "-66.924891";
	private Button buttonSatellite;
	private Button buttonStreet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.location_map);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);

		map = (MapView) findViewById(R.id.mapview);

		map.getController().setCenter(
				getPoint(Double.valueOf(latitude), Double.valueOf(longitude)));
		map.getController().setZoom(14);
		map.setBuiltInZoomControls(true);

		Intent intent = getIntent();
		foursquare = intent.getStringExtra("Foursquare");
		sites = new SitesOverlay(latitude, longitude);
		map.getOverlays().add(sites);

		me = new MyLocationOverlay(this, map);
		map.getOverlays().add(me);

		buttonSatellite = (Button) findViewById(R.id.satelite_button);
		buttonStreet = (Button) findViewById(R.id.street_button);

		Button.OnClickListener setSatelliteViewOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				map.setSatellite(true);
				map.setStreetView(false);
			}
		};

		Button.OnClickListener setStreetViewOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				map.setSatellite(false);
				map.setStreetView(true);
			}
		};

		buttonSatellite.setOnClickListener(setSatelliteViewOnClickListener);
		buttonStreet.setOnClickListener(setStreetViewOnClickListener);
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			sites = new SitesOverlay(String.valueOf(lat), String.valueOf(lng));
			map.getOverlays().add(sites);

			me = new MyLocationOverlay(this, map);
			map.getOverlays().add(me);
		}
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			map.setSatellite(!map.isSatellite());
			return (true);
		} else if (keyCode == KeyEvent.KEYCODE_Z) {
			map.displayZoomControls(true);
			return (true);
		} else if (keyCode == KeyEvent.KEYCODE_H) {
			sites.toggleHeart();

			return (true);
		}

		return (super.onKeyDown(keyCode, event));
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	private class SitesOverlay extends ItemizedOverlay<CustomItem> {
		private Drawable heart = null;
		private List<CustomItem> items = new ArrayList<CustomItem>();

		public SitesOverlay(String latitude, String longitude) {
			super(null);

			LocationServices locationServices = new LocationServices();

			if (foursquare.contains(" ")) {
				foursquare = foursquare.replace(" ", "%20");
			}

			String url = "https://api.foursquare.com/v2/venues/search?ll="
					+ latitude
					+ ","
					+ longitude
					+ "&oauth_token=GXH3U5SYQ0RNQNVATDT1FM5T1VMBI12FBYOX3RPYBV04VVRF"
					+ "&v=20111212&query='" + foursquare + "'&radius=5000";
			allLocations = new ArrayList<LocationPromotion>();
			allLocations = locationServices.getLocationVenue(url);

			heart = getMarker(R.drawable.gmarkerimage);

			for (int i = 0; i < allLocations.size(); i++) {
				LocationPromotion location = new LocationPromotion();
				location = allLocations.get(i);
				items.add(new CustomItem(getPoint(
						Double.valueOf(location.getLatitude()),
						Double.valueOf(location.getLongitude())), location
						.getName(), location.getAddress(),
						getMarker(R.drawable.gmarkerimage), heart));
			}
			populate();
		}

		@Override
		protected CustomItem createItem(int i) {
			return (items.get(i));
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

		}

		@Override
		protected boolean onTap(int i) {
			// OverlayItem item = getItem(i);
			// GeoPoint geo = item.getPoint();
			// Point pt = map.getProjection().toPixels(geo, null);

			return (true);
		}

		@Override
		public int size() {
			return (items.size());
		}

		void toggleHeart() {
			CustomItem focus = getFocus();

			if (focus != null) {
				focus.toggleHeart();
			}

			map.invalidate();
		}

		private Drawable getMarker(int resource) {
			Drawable marker = getResources().getDrawable(resource);

			marker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight());
			boundCenter(marker);

			return (marker);
		}
	}

	class PopupPanel {
		View popup;
		boolean isVisible = false;

		PopupPanel(int layout) {
			ViewGroup parent = (ViewGroup) map.getParent();

			popup = getLayoutInflater().inflate(layout, parent, false);

			popup.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					hide();
				}
			});
		}

		View getView() {
			return (popup);
		}

		void show(boolean alignTop) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			if (alignTop) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				lp.setMargins(0, 20, 0, 0);
			} else {
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				lp.setMargins(0, 0, 0, 60);
			}

			hide();

			((ViewGroup) map.getParent()).addView(popup, lp);
			isVisible = true;
		}

		void hide() {
			if (isVisible) {
				isVisible = false;
				((ViewGroup) popup.getParent()).removeView(popup);
			}
		}
	}

	class CustomItem extends OverlayItem {
		Drawable marker = null;
		boolean isHeart = false;
		Drawable heart = null;

		CustomItem(GeoPoint pt, String name, String snippet, Drawable marker,
				Drawable heart) {
			super(pt, name, snippet);

			this.marker = marker;
			this.heart = heart;
		}

		@Override
		public Drawable getMarker(int stateBitset) {
			Drawable result = (isHeart ? heart : marker);

			setState(result, stateBitset);

			return (result);
		}

		void toggleHeart() {
			isHeart = !isHeart;
		}
	}
}
