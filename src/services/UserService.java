package services;

import org.json.JSONException;
import org.json.JSONObject;
import connection.HttpClient;

public class UserService {

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

	public String registerNewUser(String email, String newpass) {

		String url = "http://www.publizar.com.ve/api/api.php?o=register&e="
				+ email + "&p=" + newpass;

		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			response = jsonObject.getString("id");
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
