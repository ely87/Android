package com.publizar.publizaraldia;

import java.util.ArrayList;

import persistence.UserHelper;

import domain.Category;

import services.UserService;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableRow.LayoutParams;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePreferencesActivity extends Activity {

	private UserHelper userHelper = new UserHelper(this);
	private Button buttonPreferences;
	private ArrayList<Category> categories;
	private String email;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_preferences);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		final TableLayout table = setTablePreferences();

		buttonPreferences = (Button) findViewById(R.id.savePreferences);

		Button.OnClickListener savePreferencesClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				showSelectedItems(table);
			}
		};

		buttonPreferences.setOnClickListener(savePreferencesClickListener);
	}

	public TableLayout setTablePreferences() {

		categories = new ArrayList<Category>();
		userHelper.open();
		Cursor c = userHelper.fetchUser(1);
		email = c.getString(1);
		userHelper.close();
		UserService userService = new UserService();
		categories = userService.getUserPreferences(email);
		// table.removeAllViews();
		TableLayout table = null;
		for (int i = 0; i < categories.size(); i++) {
			table = (TableLayout) findViewById(R.id.tablePreferences);
			// create a new TableRow
			TableRow row = new TableRow(this);
			Category category = new Category();
			category = categories.get(i);
			TextView t = new TextView(this);
			// set the text to "text xx"
			t.setText(category.getName());
			t.setTextColor(Color.DKGRAY);

			// create a CheckBox
			CheckBox check = new CheckBox(this);

			// add the TextView and the CheckBox to the new TableRow
			row.addView(t);
			if (category.getSelection() == 1) {
				check.setChecked(true);
			}

			row.addView(check);
			// add the TableRow to the TableLayout

			table.addView(row, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

		return table;
	}

	private void showSelectedItems(TableLayout table) {

		TableLayout contact_table = (TableLayout) findViewById(R.id.tablePreferences);

		for (int i = 1; i < contact_table.getChildCount(); i++) {

			TableRow row = (TableRow) contact_table.getChildAt(i);
			CheckBox check = (CheckBox) row.getChildAt(1);
			if (check.isChecked()) {
				categories.get(i - 1).setSelection(1);
			} else {
				categories.get(i - 1).setSelection(0);
			}
		}
		ArrayList<String> preferencesChecked = new ArrayList<String>();
		UserService userService = new UserService();
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getSelection() == 1) {
				preferencesChecked.add(categories.get(i).getName());
			}
		}
		String response = userService.getSendPreferences(email, preferencesChecked);
		
		Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
	}
}
