package com.publizar.publizaraldia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import persistence.FileDatabaseHelper;
import persistence.UserHelper;
import services.UserService;
import adapters.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
//import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUserActivity extends Activity {

	private FileDatabaseHelper fileDatabase;
	private EditText email;
	private EditText password;
	private EditText confirmPassword;
	private ProgressDialog registerDialog;
	private AlertDialog.Builder registerAlert;
	private Button register;
	private String username;
	private String userpwd;
	private String confirmPwd;
	private String error = null;
	private UserHelper userHelper = new UserHelper(this);
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.register);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.navigation_bar);
		fileDatabase = new FileDatabaseHelper();

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Registro de usuarios");
		email = (EditText) findViewById(R.id.registerEmail);
		password = (EditText) findViewById(R.id.registerPassword);
		confirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);

		register = (Button) findViewById(R.id.registerButton);
		registerDialog = new ProgressDialog(this);
		registerDialog.setIndeterminate(true);
		registerDialog.setTitle("Registro de usuario");
		registerDialog.setMessage("Espere un momento");

		registerAlert = new AlertDialog.Builder(this);
		registerAlert.setTitle("Registro de usuario");
		registerAlert.setPositiveButton("Ok", null);
		registerAlert.setCancelable(true);
		Button.OnClickListener validateUserOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				CallWebServiceTask task = new CallWebServiceTask();
				task.applicationContext = RegisterUserActivity.this;
				username = email.getText().toString();
				userpwd = password.getText().toString();
				confirmPwd = confirmPassword.getText().toString();
				task.execute(username, userpwd, confirmPwd);
			}
		};

		register.setOnClickListener(validateUserOnClickListener);

	}

	public class CallWebServiceTask extends AsyncTask<String, Void, Integer> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			registerDialog.show();
		}

		@Override
		protected Integer doInBackground(String... params) {
			String email = params[0];
			String password = params[1];
			String confirmPassword = params[2];

			int operationResult = 0;
			if (confirmPassword.equalsIgnoreCase(password)) {
				if (checkEmailCorrect(email) == true) {
					userHelper.open();
					int idUser = userHelper.getLastRowID();
					Cursor c = userHelper.fetchUser(idUser);
					if (c.getCount() == 0) {
						userHelper.createUser(email, password);
					}
					userHelper.close();
					UserService userService = new UserService();
					if (userService.registerNewUser(email, password) != null) {
						operationResult = 1;
					}
				} else {
					operationResult = 0;
				}

			} else {
				operationResult = -1;
			}
			fileDatabase.exportDatabase();
			return operationResult;
		}

		@Override
		protected void onPostExecute(Integer operationResult) {
			registerDialog.cancel();
			if (operationResult == 0) {
				error = "La contrase–a o el correo electr—nico no son v‡lidos";
				registerAlert.setMessage(error);
				registerAlert.create().show();
			} else if (operationResult == -1) {
				error = "Las contrase–as ingresadas no coinciden. Ingrese de nuevo la contrase–a";
				registerAlert.setMessage(error);
				registerAlert.create().show();
			} else {
				Intent intent = new Intent(RegisterUserActivity.this,
						RegisterPreferenceActivity.class);
				startActivity(intent);

			}
		}
	}

	public boolean checkEmailCorrect(String Email) {

		String pttn = "^\\D.+@.+\\.[a-z]+";
		Pattern p = Pattern.compile(pttn);
		Matcher m = p.matcher(Email);

		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}
}
