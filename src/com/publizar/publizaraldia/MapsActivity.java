package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.List;
import services.LocationServices;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import domain.LocationPromotion;
import adapters.ActionBar;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MapsActivity extends MapActivity {

	private MapView map = null; // Milliseconds
	private Double latitude;
	private Double longitude;
	private String foursquare;
	private SitesOverlay sites = null;
	private ArrayList<LocationPromotion> allLocations;
	private ActionBar actionBar;

	protected LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_map);

		map = (MapView) findViewById(R.id.mapview);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Mapas");
		actionBar
				.setHomeAction(new IntentAction(this,
						PreferencesTimelineActivity.createIntent(this),
						R.drawable.home));
		actionBar.setDisplayHomeAsUpEnabled(true);

		final Action promos = new IntentAction(this, new Intent(this,
				AllPromosActivity.class), R.drawable.list);
		actionBar.addAction(promos);

		final Action calendar = new IntentAction(this, new Intent(this,
				CalendarListActivity.class), R.drawable.calendar);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				SettingsActivity.class), R.drawable.settings);
		actionBar.addAction(settings);
		Location location = showCurrentLocation();

		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		} else {
			latitude = 10.48;
			longitude = -66.85;
		}
		map.getController().setCenter(getPoint(latitude, longitude));
		map.getController().setZoom(14);
		map.setBuiltInZoomControls(true);
		map.setSatellite(false);

		Intent intent = getIntent();
		foursquare = intent.getStringExtra("Foursquare");
		sites = new SitesOverlay(String.valueOf(latitude),
				String.valueOf(longitude));
		map.getOverlays().add(sites);

	}

	protected Location showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
		}
		return location;
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private class SitesOverlay extends ItemizedOverlay<CustomItem> {
		private Drawable heart = null;
		private List<CustomItem> items = new ArrayList<CustomItem>();

		public SitesOverlay(String latitude, String longitude) {
			super(null);

			CustomItem custom = new CustomItem(getPoint(
					Double.valueOf(latitude), Double.valueOf(longitude)),
					longitude, latitude, getMarker(R.drawable.google_pin),
					heart);
			LocationServices locationServices = new LocationServices();

			items.add(custom);
			if (foursquare.contains(" ")) {
				foursquare = foursquare.replace(" ", "%20");
			}

			String url = "https://api.foursquare.com/v2/venues/search?ll="
					+ latitude
					+ ","
					+ longitude
					+ "&oauth_token=GXH3U5SYQ0RNQNVATDT1FM5T1VMBI12FBYOX3RPYBV04VVRF"
					+ "&v=20111212&query='" + foursquare + "'&radius=100000";
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
		public int size() {
			return (items.size());
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