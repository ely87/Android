package com.publizar.publizaraldia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.FileDatabaseHelper;
import persistence.UserHelper;

import services.AuthenticateService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sign_in);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		fileDatabase = new FileDatabaseHelper();

		email = (EditText) findViewById(R.id.loginEmail);
		password = (EditText) findViewById(R.id.loginPassword);
		login = (Button) findViewById(R.id.loginButton);
		loginDialog = new ProgressDialog(this);
		loginDialog.setIndeterminate(true);
		loginDialog.setTitle("Iniciando sesi—n");
		loginDialog.setMessage("Espere un momento");

		loginAlert = new AlertDialog.Builder(this);
		loginAlert.setTitle("Inicio sesión");
		loginAlert.setPositiveButton("Ok", null);
		loginAlert.setCancelable(true);

		Button.OnClickListener validateUserOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				CallWebServiceTask task = new CallWebServiceTask();
				task.applicationContext = MainActivity.this;

				username = email.getText().toString();
				userpwd = password.getText().toString();
				task.execute(username, userpwd);
			}
		};

		login.setOnClickListener(validateUserOnClickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sign_out_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sign_out:
			userHelper.open();
			userHelper.deleteUser(1);
			userHelper.close();
			fileDatabase.exportDatabase();
			break;
		}
		return true;
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
			if (checkEmailCorrect(email) == true) {
				AuthenticateService authenticate = new AuthenticateService();
				result = authenticate.authenticateUser(email, password);
				if (result != null) {
					if (result.equalsIgnoreCase(email)) {
						userHelper.open();
						Cursor c = userHelper.fetchUser(1);
						if (c.getCount() == 0) {
							userHelper.createUser(email, password);
						}
						userHelper.close();
						boolean bool = true;
						if (bool == true) {
							operationResult = 1;
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

			}
			fileDatabase.exportDatabase();
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
			} else {
				Intent intent = new Intent(MainActivity.this,
						AllPromosActivity.class);
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