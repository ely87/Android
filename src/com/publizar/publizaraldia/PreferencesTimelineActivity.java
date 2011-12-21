package com.publizar.publizaraldia;

import java.util.ArrayList;

import domain.Preference;
import domain.Promotion;
import persistence.UserHelper;
import services.PreferenceService;
import adapters.GalleryAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PreferencesTimelineActivity extends Activity {
	private UserHelper userHelper = new UserHelper(this);
	private String email;
	private ArrayList<Promotion> promotions;
	private String titles[] = null;

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
			final TextView t = new TextView(this);
			if (pref.getSelection() == 1) {
				// set the text to "text xx"
				t.setText(pref.getName());
				t.setTextColor(Color.WHITE);
				t.setTextSize(14);
				t.setBackgroundColor(Color.GRAY);
				t.setId(i);
			}

			row.setBackgroundColor(Color.MAGENTA);
			row.addView(t);
			TableRow rowGallery = new TableRow(this);

			String urls[] = null;
			urls = getPromotions(preferencesChosen.get(i));

			if (urls.length != 0) {

				final Gallery gallery = new Gallery(this);
				gallery.setAdapter(new GalleryAdapter(this, urls, titles));
				gallery.onFling(null, null, 1000, 0);
				gallery.setBackgroundColor(Color.GRAY);
				gallery.setMinimumHeight(148);
				gallery.setId(i);

				gallery.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

				gallery.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						TextView textSelected = (TextView) findViewById(t
								.getId());
						Preference selectedPreference = new Preference();
						selectedPreference.setName(textSelected.getText()
								.toString());
						ArrayList<Promotion> resultGallery = new ArrayList<Promotion>();
						resultGallery = getArrayPromotions(selectedPreference);
						Promotion promotion = new Promotion();
						promotion = resultGallery.get(position);
						Intent intent = new Intent(
								PreferencesTimelineActivity.this,
								DetailActivity.class);
						intent.putExtra("Promo_id",
								String.valueOf(promotion.getId()));
						intent.putExtra("Promo_image", promotion.getImage_url());
						intent.putExtra("Promo_title", promotion.getTitle());
						intent.putExtra("Promo_due", promotion.getDue_date());
						intent.putExtra("Promo_company",
								promotion.getPromo_company());
						intent.putExtra("Promo_comerce", promotion.getComerce());
						intent.putExtra("Promo_price",
								promotion.getSaved_price());
						intent.putExtra("Promo_original_price",
								promotion.getOriginal_price());
						intent.putExtra("Promo_discount",
								promotion.getDiscount());
						intent.putExtra("Promo_description",
								promotion.getDescription());
						intent.putExtra("Promo_website",
								promotion.getPromo_complete_url());
						intent.putExtra("Promo_excerpt", promotion.getExcerpt());
						intent.putExtra("Promo_idcomerce",
								promotion.getId_comerce());
						startActivity(intent);

						/*
						 * Toast.makeText( PreferencesTimelineActivity.this,
						 * "Title=" + resultGallery.get(position) .getTitle(),
						 * Toast.LENGTH_SHORT) .show();
						 */
					}
				});

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
				rowGallery.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						v.setBackgroundColor(Color.GRAY);
					}
				});
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
		titles = new String[promotions.size()];
		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
			titles[i] = promotions.get(i).getTitle();

		}

		return promotionsResult;
	}

	public ArrayList<Promotion> getArrayPromotions(Preference preferences) {

		PreferenceService prefService = new PreferenceService();
		ArrayList<Promotion> promotionsResult = new ArrayList<Promotion>();
		promotionsResult = prefService.getPromosByPreference(preferences);

		return promotionsResult;
	}
}
