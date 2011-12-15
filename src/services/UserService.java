package services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connection.HttpClient;
import domain.Category;

public class UserService {

	public ArrayList<Category> getUserPreferences(String email) {
		String url = "http://www.publizar.com.ve/api/api.php?o=matchPreference&t=pref&e="
				+ email;
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		String name = null;
		String value = null;
		ArrayList<Category> results = new ArrayList<Category>();
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				Category category = new Category();

				jsonObject = jsonArray.getJSONObject(i);
				name = jsonObject.getString("nombre");
				value = jsonObject.getString("seleccion");
				category.setName(name);
				category.setSelection(Integer.valueOf(value));
				results.add(category);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

	public String getSendPreferences(String email, ArrayList<String> preferences) {
		// String[] preference = new String[preferences.size()];
		String preference = null;
		for (int i = 0; i < preferences.size(); i++) {

			preference = preference + "," + preferences.get(i);
		}

		String pref = preference.substring(5, preference.length());
		pref = pref.replace(" ", "%20");
		String url = "http://www.publizar.com.ve/api/api.php?o=updatePreferences&e="
				+ email + "&list=" + pref;

		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);

		try {
			JSONObject jsonObject = new JSONObject(response);
			response = jsonObject.getString("mensaje");
		} catch (JSONException e) {
			response = "Las preferencias no pudieron ser modificadas";
		}
		return response;
	}

	public String sendNewPassword(String email, String newpass) {

		String url = "http://www.publizar.com.ve/api/api.php?o=changePassword&e="
				+ email + "&pass=" + newpass;

		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			response = jsonObject.getString("mensaje");
		} catch (JSONException e) {
			try {
				jsonObject = new JSONObject(response);
				response = jsonObject.getString("error");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return response;
	}
}
