package com.publizar.publizaraldia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TableRow;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.preference_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);

		TableRow tablerow = (TableRow) findViewById(R.id.rowcontrasenas);
		tablerow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						ChangePasswordActivity.class);
				startActivity(intent);
			}
		});

		TableRow rowcategorias = (TableRow) findViewById(R.id.rowcategorias);
		rowcategorias.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						ChangePreferencesActivity.class);
				startActivity(intent);
			}
		});
	}
}
