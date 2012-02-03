package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.LinkedList;
import domain.Preference;
import domain.Promotion;
import persistence.UserHelper;
import services.PreferenceService;
import services.PromotionServices;
import adapters.HorizontalListAdapter;
import adapters.SeparatedListAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

public class PreferencesTimelineActivity extends Activity {
	private UserHelper userHelper = new UserHelper(this);
	private String email;
	private ArrayList<Promotion> promotions;
	private String titles[] = null;
	private ListView listTimeline;
	private SeparatedListAdapter adapter;

	// private Date date;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_list);
		adapter = new SeparatedListAdapter(this);
		listTimeline = (ListView) findViewById(R.id.list_timeline);
		// date = new Date(1987, 01, 01);
		setPreferences();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.navigation_menu, menu);
		return true;

	}

	public void setPreferences() {

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
		listTimeline.setAdapter(adapter);
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

		PromotionServices promotionServices = new PromotionServices();

		LinkedList<Promotion> promotions = new LinkedList<Promotion>();
		// promotions = promotionServices.getRefreshPromotion(searchUrl);

		return promotions;
	}
}
