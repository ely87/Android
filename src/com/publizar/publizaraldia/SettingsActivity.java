package com.publizar.publizaraldia;

import persistence.FileDatabaseHelper;
import persistence.UserHelper;
import adapters.ActionBar;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;

public class SettingsActivity extends Activity {

	private UserHelper userHelper = new UserHelper(this);
	private FileDatabaseHelper fileDatabase;
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		fileDatabase = new FileDatabaseHelper();
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Preferencias");
		actionBar
				.setHomeAction(new IntentAction(this,
						PreferencesTimelineActivity.createIntent(this),
						R.drawable.home));
		actionBar.setDisplayHomeAsUpEnabled(true);

		final Action calendar = new IntentAction(this, new Intent(this,
				AllPromosActivity.class), R.drawable.list);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				CalendarListActivity.class), R.drawable.calendar);
		actionBar.addAction(settings);

		TableRow tablerow = (TableRow) findViewById(R.id.rowcontrasenas);
		tablerow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						ChangePasswordActivity.class);
				startActivity(intent);
			}
		});

		TableRow rowcerrarsesion = (TableRow) findViewById(R.id.rowcerrarsesion);
		rowcerrarsesion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userHelper.open();
				int idUser = userHelper.getLastRowID();
				userHelper.deleteUser(idUser);
				userHelper.close();
				fileDatabase.exportDatabase();
				SharedPreferences prefs = getApplicationContext()
						.getSharedPreferences("prefs_file", MODE_PRIVATE);

				SharedPreferences.Editor editor = prefs.edit();
				editor.remove("username");
				editor.remove("password");
				editor.commit();
				Intent intent = new Intent(SettingsActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

		TableRow rowfavoritos = (TableRow) findViewById(R.id.rowfavoritos);
		rowfavoritos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						AllFavouritesActivity.class);
				startActivity(intent);
			}
		});
	}

}
