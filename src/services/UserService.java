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
}
