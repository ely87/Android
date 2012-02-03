package services;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connection.HttpClient;
import domain.Preference;
import domain.Promotion;

public class PreferenceService {

	private Date max_date;

	public Date getMax_date() {
		return max_date;
	}

	public void setMax_date(Date max_date) {
		this.max_date = max_date;
	}

	public ArrayList<Preference> getUserPreferences(String email) {
		String url = "http://www.publizar.com.ve/api/api.php?o=matchPreference&t=pref&e="
				+ email;
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		String name = null;
		String value = null;
		ArrayList<Preference> results = new ArrayList<Preference>();
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				Preference category = new Preference();

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

	public ArrayList<Preference> getAllPreferences() {
		String url = "http://www.publizar.com.ve/api/api.php?o=allpreferences";
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		String name = null;
		String value = null;
		ArrayList<Preference> results = new ArrayList<Preference>();
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				Preference category = new Preference();

				jsonObject = jsonArray.getJSONObject(i);
				name = jsonObject.getString("nombre");
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

	public ArrayList<Promotion> getPromosByPreference(Preference preferences) {
		ArrayList<Promotion> promotions = new ArrayList<Promotion>();

		String pref = preferences.getName().replace(" ", "%20");
		String url = "http://www.publizar.com.ve/api/api.php?o=getppreferences&pref="
				+ pref;

		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		JSONArray jsonArrayPromotions = null;
		try {
			JSONArray jsonArray = new JSONArray(response);

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String tag = jsonObject.get("tag").toString()
						.replace(" ", "%20");
				if (tag.equalsIgnoreCase(pref)) {
					jsonArrayPromotions = jsonObject
							.getJSONArray("promociones");
				}

			}
			if (jsonArrayPromotions != null) {
				for (int i = 0; i < jsonArrayPromotions.length(); i++) {
					JSONArray jsonArrayInside = jsonArrayPromotions
							.getJSONArray(i);

					for (int j = 0; j < jsonArrayInside.length(); j++) {
						JSONObject jsonObjectInside = jsonArrayInside
								.getJSONObject(j);
						String id = jsonObjectInside.getString("id");
						String description = jsonObjectInside
								.getString("content");
						String title = jsonObjectInside.getString("title");
						String urlImage = jsonObjectInside.getString("imagen");
						String discount = jsonObjectInside
								.getString("descuento");
						String price = jsonObjectInside.getString("precio");
						String original_price = jsonObjectInside
								.getString("precio original");
						String date = jsonObjectInside
								.getString("fecha_vencimiento");
						String id_comerce = jsonObjectInside
								.getString("comercio");
						String company = jsonObjectInside.getString("empresa");
						String promo_link = jsonObjectInside
								.getString("promo_link");
						String post_date = jsonObjectInside
								.getString("post_date");
						Promotion promotion = new Promotion();
						promotion.setId(Integer.valueOf(id));
						promotion.setDescription(description);
						promotion.setTitle(title);
						promotion.setId_comerce(id_comerce);
						promotion.setImage_url(urlImage);
						promotion.setDiscount(discount);
						promotion.setSaved_price(price);
						promotion.setOriginal_price(original_price);
						promotion.setPromo_company(company);
						promotion.setDue_date(date);
						promotion.setPromo_complete_url(promo_link);
						promotion.setStatus("server");
						promotion.setPost_date(post_date);
						promotions.add(promotion);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return promotions;
	}

	public ArrayList<Promotion> refreshPromosByPreference(
			Preference preferences, String date) {
		ArrayList<Promotion> promotions = new ArrayList<Promotion>();

		String pref = preferences.getName().replace(" ", "%20");
		date = date.replace(" ", "%20");
		String url = "http://publizar.com.ve/api/api.php?o=refreshPromosByPrefs&d="
				+ date + "&pref=" + preferences;

		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		JSONArray jsonArrayPromotions = null;
		try {
			JSONArray jsonArray = new JSONArray(response);

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String tag = jsonObject.get("tag").toString()
						.replace(" ", "%20");
				if (tag.equalsIgnoreCase(pref)) {
					jsonArrayPromotions = jsonObject
							.getJSONArray("promociones");
				}

			}
			if (jsonArrayPromotions != null) {
				for (int i = 0; i < jsonArrayPromotions.length(); i++) {
					JSONArray jsonArrayInside = jsonArrayPromotions
							.getJSONArray(i);

					for (int j = 0; j < jsonArrayInside.length(); j++) {
						JSONObject jsonObjectInside = jsonArrayInside
								.getJSONObject(j);
						String id = jsonObjectInside.getString("id");
						String description = jsonObjectInside
								.getString("content");
						String title = jsonObjectInside.getString("title");
						String urlImage = jsonObjectInside.getString("imagen");
						String discount = jsonObjectInside
								.getString("descuento");
						String price = jsonObjectInside.getString("precio");
						String original_price = jsonObjectInside
								.getString("precio original");
						String date_finish = jsonObjectInside
								.getString("fecha_vencimiento");
						String id_comerce = jsonObjectInside
								.getString("comercio");
						String company = jsonObjectInside.getString("empresa");
						String promo_link = jsonObjectInside
								.getString("promo_link");
						String post_date = jsonObjectInside
								.getString("post_date");
						Promotion promotion = new Promotion();
						promotion.setId(Integer.valueOf(id));
						promotion.setDescription(description);
						promotion.setTitle(title);
						promotion.setId_comerce(id_comerce);
						promotion.setImage_url(urlImage);
						promotion.setDiscount(discount);
						promotion.setSaved_price(price);
						promotion.setOriginal_price(original_price);
						promotion.setPromo_company(company);
						promotion.setDue_date(date_finish);
						promotion.setPromo_complete_url(promo_link);
						promotion.setStatus("server");
						promotion.setPost_date(post_date);
						promotions.add(promotion);
					}
				}
			}
			setMaxDate(jsonArrayPromotions);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return promotions;
	}

	public Date setMaxDate(JSONArray jsonArrayPromotions) throws JSONException {

		if (jsonArrayPromotions != null) {
			for (int i = 0; i < jsonArrayPromotions.length(); i++) {
				JSONArray jsonArrayInside = jsonArrayPromotions.getJSONArray(i);

				for (int j = 0; j < jsonArrayInside.length(); j++) {
					JSONObject jsonObjectInside = jsonArrayInside
							.getJSONObject(j);
					String post_date = jsonObjectInside.getString("post_date");
					Date date = new Date(post_date);
					if (date.after(max_date)) {
						max_date = date;
					}
				}
			}
		}

		return max_date;

	}
}
