package com.publizar.publizaraldia;

import java.util.ArrayList;

import domain.Preference;
import domain.Promotion;
import persistence.UserHelper;
import services.PreferenceService;
import adapters.GalleryAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Gallery;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PreferencesTimelineActivity extends Activity {
	private UserHelper userHelper = new UserHelper(this);
	private String email;
	private ArrayList<Promotion> promotions;

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
		ArrayList<Preference> preferencesChosen = new ArrayList<Preference>();
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
				preferencesChosen.add(pref);
			}
		}

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

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

			row.setBackgroundColor(Color.MAGENTA);
			row.addView(t);
			TableRow rowGallery = new TableRow(this);

			String urls[] = null;
			urls = getPromotions(preferencesChosen.get(i));

			if (urls.length != 0) {

				Gallery gallery = new Gallery(this);
				gallery.setAdapter(new GalleryAdapter(this, urls));
				gallery.onFling(null, null, 1000, 0);
				gallery.setBackgroundColor(Color.GRAY);
				gallery.setMinimumHeight(148);
				gallery.setSelection(0);

				gallery.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

				int galleryWidth = dm.widthPixels;
				int itemWidth = 271;
				int spacing = 0;

				// The offset is how much we will pull the gallery to the left
				// in order
				// to simulate
				// left alignment of the first item
				int offset;
				if (galleryWidth <= itemWidth) {
					offset = galleryWidth / 2 - itemWidth / 2 - spacing;
				} else {
					offset = galleryWidth - itemWidth - 2 * spacing;
				}
				// offset = 0;

				// Now update the layout parameters of the gallery in order to
				// set the
				// left margin
				MarginLayoutParams mlp = (MarginLayoutParams) gallery
						.getLayoutParams();
				mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
						mlp.bottomMargin);

				// gallery.setLayoutParams(mlp);
				rowGallery.addView(gallery);

				rowGallery.setMinimumHeight(148);
				table.addView(row, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				table.addView(rowGallery, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}

		return table;
	}

	public String[] getPromotions(Preference preferences) {

		PreferenceService prefService = new PreferenceService();
		promotions = prefService.getPromosByPreference(preferences);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
		}

		return promotionsResult;
	}

}
