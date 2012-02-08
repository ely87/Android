package com.publizar.publizaraldia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {

	private SharedPreferences prefs;
	private String user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = this.getApplicationContext().getSharedPreferences("prefs_file",
				MODE_PRIVATE);
		user = prefs.getString("username", null);
		if (user != null) {
			Intent intent = new Intent(MainActivity.this,
					PreferencesTimelineActivity.class);
			//
			startActivity(intent);
		} else {
			// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.splash_screen);
			Thread splashThread = new Thread() {
				@Override
				public void run() {
					try {
						int waited = 0;
						while (waited < 5000) {
							sleep(100);
							waited += 100;
						}
					} catch (InterruptedException e) {
						// do nothing
					} finally {
						finish();
						Intent i = new Intent(MainActivity.this,
								SignInActivity.class);
						startActivity(i);
					}
				}
			};
			splashThread.start();
		}

	}

}