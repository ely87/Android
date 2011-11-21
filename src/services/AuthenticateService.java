package services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import connection.HttpClient;

public class AuthenticateService {

	public String authenticateUser(String email, String password) {
		String url = "http://publizar.com.ve/api/api.php?o=login&e=" + email
				+ "&p=" + password;
		String response = null;
		String result = null;

		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);

		result = validateUserGson(response);

		return result;
	}

	public String validateUserGson(String result) {
		String resultGson = null;

		String prueba = null;

		String error = null;

		JSONArray jsonArray;

		JSONObject jsonObject = null;

		try {

			jsonArray = new JSONArray(result);

			jsonObject = jsonArray.getJSONObject(0);

			prueba = jsonObject.getString("user_login");

			if (prueba != null) {

				resultGson = prueba;

			}

		} catch (JSONException e) {

			try {

				error = jsonObject.getString("error");

			} catch (JSONException e1) {

				// TODO Auto-generated catch block

				e1.printStackTrace();

			}

			resultGson = error;

		}

		return resultGson;
	}
}
