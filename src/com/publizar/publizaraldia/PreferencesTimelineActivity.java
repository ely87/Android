package com.publizar.publizaraldia;

import java.util.ArrayList;

import domain.Preference;
import persistence.UserHelper;
import services.PreferenceService;
import adapters.GalleryAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Gallery;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PreferencesTimelineActivity extends Activity {
	private UserHelper userHelper = new UserHelper(this);
	private String email;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.preferences_timeline);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		setTablePreferences();
	}

	public TableLayout setTablePreferences() {

		ArrayList<Preference> preferences = new ArrayList<Preference>();
		PreferenceService preferenceService = new PreferenceService();
		userHelper.open();
		Cursor c = userHelper.fetchUser(1);
		email = c.getString(1);
		userHelper.close();
		preferences = preferenceService.getUserPreferences(email);
		TableLayout table = null;

		int count = 0;
		for (int i = 0; i < preferences.size(); i++) {
			Preference pref = new Preference();
			pref = preferences.get(i);
			if (pref.getSelection() == 1) {
				count++;
			}
		}

		for (int i = 0; i < count; i++) {
			table = (TableLayout) findViewById(R.id.tableGallery);
			// create a new TableRow
			TableRow row = new TableRow(this);
			Preference pref = new Preference();
			pref = preferences.get(i);
			TextView t = new TextView(this);
			if (pref.getSelection() == 1) {
				// set the text to "text xx"
				t.setText(pref.getName());
				t.setTextColor(Color.WHITE);
				t.setTextSize(14);
				t.setBackgroundColor(Color.GRAY);
			}

			PreferenceService prefService = new PreferenceService();
			//prefService.getPromosByPreference(preferences);
			row.addView(t);
			TableRow rowGallery = new TableRow(this);


			String urls[] = new String[5];
			urls[0] = "http://www.publizar.com.ve/wp-content/uploads/2011/12/spathumbnail.jpg";
			urls[1] = "http://www.publizar.com.ve/wp-content/uploads/2011/12/paellathumbnail.jpg";
			urls[2] = "http://www.publizar.com.ve/wp-content/uploads/2011/12/spathumbnail.jpg";
			urls[3] = "http://www.publizar.com.ve/wp-content/uploads/2011/12/paellathumbnail.jpg";
			urls[4] = "http://www.publizar.com.ve/wp-content/uploads/2011/12/spathumbnail.jpg";
			Gallery gallery = new Gallery(this);
			gallery.setAdapter(new GalleryAdapter(this, urls));
			gallery.setBackgroundColor(Color.DKGRAY);

			gallery.onFling(null, null, 1000, 0);
			rowGallery.addView(gallery);
			table.addView(row, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			table.addView(rowGallery, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

		return table;
	}

}
