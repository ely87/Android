package com.publizar.publizaraldia;

import java.util.ArrayList;

import persistence.UserHelper;

import domain.Category;

import services.UserService;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ChangePreferencesActivity extends Activity {

	private UserHelper userHelper = new UserHelper(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_preferences);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		setTablePreferences();
	}

	public void setTablePreferences() {

		ArrayList<Category> categories = new ArrayList<Category>();
		userHelper.open();
		Cursor c = userHelper.fetchUser(1);
		String email = c.getString(1);
		userHelper.close();
		UserService userService = new UserService();
		categories = userService.getUserPreferences(email);

		// table.removeAllViews();
		for (int i = 0; i < categories.size(); i++) {
			TableLayout table = (TableLayout) findViewById(R.id.tablePreferences);
			// create a new TableRow
			TableRow row = new TableRow(this);
			Category category = new Category();
			category = categories.get(i);
			TextView t = new TextView(this);
			// set the text to "text xx"
			t.setText(category.getName());

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
	}
}
