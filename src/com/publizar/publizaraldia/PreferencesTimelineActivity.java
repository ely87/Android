package com.publizar.publizaraldia;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PreferencesTimelineActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_password);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
	}
}
