package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.LinkedList;
import domain.Preference;
import domain.Promotion;
import persistence.UserHelper;
import services.PreferenceService;
import adapters.ActionBar;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import adapters.HorizontalListAdapter;
import adapters.SeparatedListAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class PreferencesTimelineActivity extends Activity {
	private UserHelper userHelper = new UserHelper(this);
	private String email;
	private ArrayList<Promotion> promotions;
	private String titles[] = null;
	private ListView listTimeline;
	private SeparatedListAdapter adapter;
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_list);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		// actionBar.setHomeAction(new IntentAction(this, createIntent(this),
		// R.drawable.ic_title_home_demo));
		actionBar.setTitle("Timeline");
		final Action timeline = new IntentAction(this, new Intent(this,
				AllPromosActivity.class), R.drawable.list);
		actionBar.addAction(timeline);

		final Action calendar = new IntentAction(this, new Intent(this,
				CalendarListActivity.class), R.drawable.calendar);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				SettingsActivity.class), R.drawable.settings);
		actionBar.addAction(settings);

		adapter = new SeparatedListAdapter(this);
		listTimeline = (ListView) findViewById(R.id.list_timeline);

		// listTimeline.setOnItemClickListener(new ItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v,
		// int position, long id) {

		/*
		 * Preference selectedPreference = new Preference();
		 * selectedPreference.setName(textSelected.getText() .toString());
		 * ArrayList<Promotion> resultGallery = new ArrayList<Promotion>();
		 * resultGallery = getArrayPromotions(selectedPreference); Promotion
		 * promotion = new Promotion(); promotion = resultGallery.get(position);
		 * Intent intent = new Intent( PreferencesTimelineActivity.this,
		 * DetailActivity.class); intent.putExtra("Promo_id",
		 * String.valueOf(promotion.getId())); intent.putExtra("Promo_image",
		 * promotion.getImage_url()); intent.putExtra("Promo_title",
		 * promotion.getTitle()); intent.putExtra("Promo_due",
		 * promotion.getDue_date()); intent.putExtra("Promo_company",
		 * promotion.getPromo_company()); intent.putExtra("Promo_comerce",
		 * promotion.getComerce()); intent.putExtra("Promo_price",
		 * promotion.getSaved_price()); intent.putExtra("Promo_original_price",
		 * promotion.getOriginal_price()); intent.putExtra("Promo_discount",
		 * promotion.getDiscount()); intent.putExtra("Promo_description",
		 * promotion.getDescription()); intent.putExtra("Promo_website",
		 * promotion.getPromo_complete_url()); intent.putExtra("Promo_excerpt",
		 * promotion.getExcerpt()); intent.putExtra("Promo_idcomerce",
		 * promotion.getId_comerce()); startActivity(intent);
		 */
		// }
		// });
		// listTimeline = (ListView) findViewById(R.id.list_timeline);
		CallWebServiceTask task = new CallWebServiceTask();
		task.applicationContext = PreferencesTimelineActivity.this;
		task.execute();
	}

	public class CallWebServiceTask extends
			AsyncTask<String, Void, SeparatedListAdapter> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
		}

		@Override
		protected SeparatedListAdapter doInBackground(String... params) {
			adapter = setPreferences();
			return adapter;
		}

		@Override
		protected void onPostExecute(SeparatedListAdapter adapter) {
			actionBar.setProgressBarVisibility(View.GONE);
			listTimeline.setAdapter(adapter);
		}
	}

	public SeparatedListAdapter setPreferences() {

		ArrayList<Preference> preferences = new ArrayList<Preference>();
		ArrayList<Preference> preferencesChosen = new ArrayList<Preference>();
		PreferenceService preferenceService = new PreferenceService();
		userHelper.open();
		int idUser = userHelper.getLastRowID();
		Cursor c = userHelper.fetchUser(idUser);
		email = c.getString(1);
		userHelper.close();

		preferences = preferenceService.getUserPreferences(email);

		for (int i = 0; i < preferences.size(); i++) {
			Preference pref = new Preference();
			pref = preferences.get(i);
			if (pref.getSelection() == 1) {
				preferencesChosen.add(pref);
			}
		}

		for (int i = 0; i < preferencesChosen.size(); i++) {

			String urls[] = null;
			urls = getPromotions(preferencesChosen.get(i));

			if (urls.length != 0) {
				HorizontalListAdapter horizontal = null;
				urls = getPromotions(preferences.get(i));
				horizontal = new HorizontalListAdapter(this, urls, titles);
				adapter.addSection(preferences.get(i).getName(), horizontal);

			}
		}
		return adapter;
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

	public LinkedList<Promotion> getRefreshPromotions(Preference preference,
			String date) {
		date = date.replace(" ", "%20");
		// String searchUrl = ;

		// PromotionServices promotionServices = new PromotionServices();

		LinkedList<Promotion> promotions = new LinkedList<Promotion>();
		// promotions = promotionServices.getRefreshPromotion(searchUrl);

		return promotions;
	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, PreferencesTimelineActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

}
