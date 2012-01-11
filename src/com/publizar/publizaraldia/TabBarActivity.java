package com.publizar.publizaraldia;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabBarActivity extends TabActivity implements OnTabChangeListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tab_bar_controller);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, AllPromosActivity.class);
		spec = tabHost.newTabSpec("Promociones").setIndicator("Promociones")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PreferencesTimelineActivity.class);
		spec = tabHost.newTabSpec("Time").setIndicator("Timeline")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CalendarListActivity.class);
		spec = tabHost.newTabSpec("Calendario").setIndicator("Calendario")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SettingsActivity.class);
		spec = tabHost.newTabSpec("Preferencias").setIndicator("Preferencias")
				.setContent(intent);
		tabHost.addTab(spec);

	}

	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub

	}

}
