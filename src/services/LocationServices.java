package services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connection.HttpClient;
import domain.LocationPromotion;

public class LocationServices {
	public ArrayList<LocationPromotion> getLocationVenue(String url) {

		String response = null;

		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		JSONArray jsonArray;
		String address;
		String name;
		String crossStreet;
		String latitude;
		String longitude;
		ArrayList<LocationPromotion> allLocations = new ArrayList<LocationPromotion>();

		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONObject responseJSON = jsonObj.getJSONObject("response");
			jsonArray = responseJSON.getJSONArray("venues");
			for (int i = 0; i < jsonArray.length(); i++) {
				LocationPromotion locationPromotion = new LocationPromotion();
				jsonObject = jsonArray.getJSONObject(i);
				name = jsonObject.getString("name");
				JSONObject location = jsonObject.getJSONObject("location");
				address = location.getString("address");
				crossStreet = location.getString("crossStreet");
				latitude = location.getString("lat");
				longitude = location.getString("lng");
				locationPromotion.setAddress(address);
				locationPromotion.setCrossStreet(crossStreet);
				locationPromotion.setLatitude(latitude);
				locationPromotion.setLongitude(longitude);
				locationPromotion.setName(name);
				allLocations.add(locationPromotion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allLocations;
	}
}
