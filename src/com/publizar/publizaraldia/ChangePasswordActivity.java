package com.publizar.publizaraldia;

import persistence.UserHelper;
import services.UserService;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {

	private EditText password1;
	private EditText password2;
	private Button savePassword;
	private AlertDialog.Builder passwordAlert;
	private UserHelper userHelper = new UserHelper(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_password);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);

		password1 = (EditText) findViewById(R.id.password1);
		password2 = (EditText) findViewById(R.id.password2);
		savePassword = (Button) findViewById(R.id.savePassword);

		passwordAlert = new AlertDialog.Builder(this);
		passwordAlert.setTitle("Cambio de contrase–a");
		passwordAlert.setPositiveButton("Ok", null);
		passwordAlert.setCancelable(true);

		Button.OnClickListener savePasswordOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				String newPass1 = password1.getText().toString();
				String newPass2 = password2.getText().toString();

				if (newPass1.equalsIgnoreCase(newPass2)) {
					userHelper.open();
					int id = userHelper.getLastRowID();
					Cursor c = userHelper.fetchUser(id);
					String email = c.getString(1);
					userHelper.close();
					UserService userService = new UserService();
					String response = userService.sendNewPassword(email,
							newPass1);
					if (response.equalsIgnoreCase("actualizado")) {
						response = "Su contrase–a fue modificada exitosamente.";
					}
					Toast.makeText(getApplicationContext(), response,
							Toast.LENGTH_LONG).show();
				} else {
					String error = "La contrase–as ingresadas son diferentes.Por favor ingrese nuevamente las contrase–as.";
					passwordAlert.setMessage(error);
					passwordAlert.create().show();
				}
			}
		};

		savePassword.setOnClickListener(savePasswordOnClickListener);
	}
}
