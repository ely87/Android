package com.publizar.publizaraldia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connection.VerifyConnection;
import persistence.FileDatabaseHelper;
import persistence.UserHelper;
import services.AuthenticateService;
import adapters.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends Activity {

	private EditText email;
	private EditText password;
	private Button login;
	private ProgressDialog loginDialog;
	private String username;
	private String userpwd;
	private AlertDialog.Builder loginAlert;
	private String error = null;
	private UserHelper userHelper = new UserHelper(this);
	private FileDatabaseHelper fileDatabase;
	private SharedPreferences prefs;
	private String user;
	private String pass;
	private Button registerUser;
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = this.getApplicationContext().getSharedPreferences("prefs_file",
				MODE_PRIVATE);
		user = prefs.getString("username", null);
		pass = prefs.getString("password", null);

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sign_in);
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Publizar");
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.navigation_bar);
		fileDatabase = new FileDatabaseHelper();

		email = (EditText) findViewById(R.id.loginEmail);
		password = (EditText) findViewById(R.id.loginPassword);
		login = (Button) findViewById(R.id.loginButton);
		registerUser = (Button) findViewById(R.id.registerButton);
		loginDialog = new ProgressDialog(this);
		loginDialog.setIndeterminate(true);
		loginDialog.setTitle("Iniciando sesi—n");
		loginDialog.setMessage("Espere un momento");

		loginAlert = new AlertDialog.Builder(this);
		loginAlert.setTitle("Inicio sesi—n");
		loginAlert.setPositiveButton("Ok", null);
		loginAlert.setCancelable(true);

		Button.OnClickListener validateUserOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				CallWebServiceTask task = new CallWebServiceTask();
				task.applicationContext = SignInActivity.this;

				if (pass == null && user == null) {
					Editor edit = prefs.edit();
					edit.putString("username", email.getText().toString());
					edit.putString("password", password.getText().toString()
							.toString());
					edit.commit();
				} else {
					if (email.getText().toString().equals(user)
							&& password.getText().toString()
									.equalsIgnoreCase(pass)) {
						Intent intent = new Intent(SignInActivity.this,
								PreferencesTimelineActivity.class);
						startActivity(intent);
					}
				}
				username = email.getText().toString();
				userpwd = password.getText().toString();
				task.execute(username, userpwd);
			}
		};

		Button.OnClickListener registerUserOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(SignInActivity.this,
						RegisterUserActivity.class);
				startActivity(intent);

			}
		};

		registerUser.setOnClickListener(registerUserOnClickListener);
		login.setOnClickListener(validateUserOnClickListener);
	}

	public class CallWebServiceTask extends AsyncTask<String, Void, Integer> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			loginDialog.show();
		}

		@Override
		protected Integer doInBackground(String... params) {
			String email = params[0];
			String password = params[1];
			String result = null;

			int operationResult = 0;
			VerifyConnection connection = new VerifyConnection(
					applicationContext);
			boolean wifi = false;
			boolean mobile = false;

			wifi = connection.haveNetworkWiFiConnection();
			mobile = connection.haveNetworkMobileConnection();
			if (wifi || mobile) {
				if (checkEmailCorrect(email) == true) {
					AuthenticateService authenticate = new AuthenticateService();
					result = authenticate.authenticateUser(email, password);
					if (result != null) {
						if (result.equalsIgnoreCase(email)) {
							userHelper.open();
							int id = userHelper.getLastRowID();
							Cursor c = userHelper.fetchUser(id);
							if (c.getCount() == 0) {
								userHelper.createUser(email, password);
							}
							userHelper.close();
							boolean bool = true;
							if (bool == true) {
								operationResult = 1;
								fileDatabase.exportDatabase();
							} else {
								operationResult = 0;
							}
						} else {
							error = result;
							operationResult = 0;
						}
					} else {
						operationResult = -1;
					}
				} else {
					operationResult = -0;
				}
			} else {
				operationResult = -2;
			}

			return operationResult;
		}

		@Override
		protected void onPostExecute(Integer operationResult) {
			loginDialog.cancel();
			if (operationResult == 0) {
				error = "La contrase–a o el correo electr—nico no son v‡lidos";
				loginAlert.setMessage(error);
				loginAlert.create().show();
			} else if (operationResult == -1) {
				error = "El usuario no es v‡lido";
				loginAlert.setMessage(error);
				loginAlert.create().show();
			} else if (operationResult == -2) {
				error = "El dispositivo no posee conexi—n. Por favor intente nuevamente.";
				loginAlert.setMessage(error);
				loginAlert.create().show();
			} else {
				Intent intent = new Intent(SignInActivity.this,
						PreferencesTimelineActivity.class);
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
