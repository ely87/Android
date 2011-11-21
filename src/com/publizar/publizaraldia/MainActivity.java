package com.publizar.publizaraldia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.UserDAO;

import services.AuthenticateService;

import Domain.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sign_in);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);

		email = (EditText) findViewById(R.id.loginEmail);
		password = (EditText) findViewById(R.id.loginPassword);
		login = (Button) findViewById(R.id.loginButton);
		loginDialog = new ProgressDialog(this);
		loginDialog.setIndeterminate(true);
		loginDialog.setTitle("Iniciando sesión");
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
				if (result.equalsIgnoreCase(email)) {
					UserDAO userDao = new UserDAO();
					userDao.initialize(applicationContext);
					boolean bool = insertUser(email, password);
					if (bool == true) {
						operationResult = 1;
					}
					else {
						operationResult = 0;
					}
				} else {
					error = result;
					operationResult = 0;
				}

			}
			return operationResult;
		}

		@Override
		protected void onPostExecute(Integer operationResult) {
			loginDialog.cancel();
			if (operationResult == 0) {
				loginAlert.setMessage(error);
				loginAlert.create().show();
			} else {
				loginAlert
						.setMessage("El usuario fue autenticado exitosamente");
				loginAlert.create().show();
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

	public boolean insertUser(String email, String password) {
		String tableName = "users";
		boolean bool = false;
		UserDAO userDAO = new UserDAO();
		bool = userDAO.isTableEmpty(tableName);
		User user = new User();
		if (bool == true) {
			user.setEmail(email);
			user.setPassword(password);
			userDAO.insert(user);
			bool = true;
		} else {
			user = userDAO.selectUser(email);
			if (user == null) {
				user.setEmail(email);
				user.setPassword(password);
				userDAO.insert(user);
				bool = true;
				userDAO.delete(user);
			} else {
				bool = true;
			}
		}
		return bool;

	}

}