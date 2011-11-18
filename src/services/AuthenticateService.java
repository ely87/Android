package services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import connection.HttpClient;

public class AuthenticateService {

	public String authenticateUser(String email, String password) {
		String url = "http://publizar.com.ve/api/api.php?o=login&e=" + email
				+ "&p=" + password;
		String response = null;

		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);

		validateUserGson(response);

		return response;
	}

	public String validateUserGson(String result) {
		String resultGson = null;
		JsonElement jsonParser = new JsonParser().parse(result);
		JsonArray jsonArray = jsonParser.getAsJsonArray();
		Object user = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			Gson gson = new Gson();
			user = gson.fromJson(jsonArray.get(i), Object.class);
		}
		return resultGson;
	}
}
