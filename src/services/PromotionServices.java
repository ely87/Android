package services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import connection.HttpClient;
import domain.Promotion;

public class PromotionServices {

	ArrayList<Promotion> promotions;

	public ArrayList<Promotion> getAllPromotions(String url) {
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		promotions = new ArrayList<Promotion>();
		JSONArray jsonArray;

		try {
			jsonArray = new JSONArray(response);

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String description = jsonObject.getString("content");
				String excerpt = jsonObject.getString("excerpt");
				String title = jsonObject.getString("title");
				String urlImage = jsonObject.getString("imagen");
				String discount = jsonObject.getString("descuento");
				String price = jsonObject.getString("precio");
				String original_price = jsonObject.getString("precio original");
				String date = jsonObject.getString("fecha_vencimiento");
				String id_comerce = jsonObject.getString("comercio");
				String company = jsonObject.getString("empresa");
				String promo_link = jsonObject.getString("promo_link");
				Promotion promotion = new Promotion();
				promotion.setId(Integer.valueOf(id));
				promotion.setDescription(description);
				promotion.setExcerpt(excerpt);
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

				promotions.add(promotion);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// promotions.add(promotion);
		return promotions;

	}

	public Promotion getExtraInformation(Promotion initialPromotion) {
		String url = "http://www.publizar.com/api/api.php?o=extraInformation&i="
				+ initialPromotion.getId_comerce();
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		Promotion promotion = new Promotion();
		promotion = initialPromotion;
		try {
			jsonObject = new JSONObject(response);
			String foursquare = jsonObject.getString("foursquare");
			String comerce = jsonObject.getString("comercio");
			String comerce_tlf = jsonObject.getString("telefono_comercio");
			String website_comerce = jsonObject.getString("sitio_web_comercio");
			String twitter = jsonObject.getString("twitter");
			String facebook = jsonObject.getString("facebook");
			String email_contact = jsonObject.getString("email_contacto");
			String form_link = jsonObject.getString("formulario_link");

			promotion.setFoursquare(foursquare);
			promotion.setComerce(comerce);
			promotion.setComerce_tlf(comerce_tlf);
			promotion.setWebsite_comerce(website_comerce);
			promotion.setTwitter(twitter);
			promotion.setFacebook(facebook);
			promotion.setContact_email(email_contact);
			promotion.setForm_link(form_link);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return promotion;
	}

	public void sendEmailPromotion(String id, String email) {
		String url = "http://www.publizar.com/api/api.php?o=sendbymail&i=" + id
				+ "&u=" + email;
		HttpClient httpClient = new HttpClient();
		httpClient.getHTTPRequest(url);
	}

	public ArrayList<Promotion> getPromosByMonths() {
		ArrayList<Promotion> promotions = new ArrayList<Promotion>();

		String url = "http://www.publizar.com.ve/api/api.php?o=getpcal";

		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		JSONArray jsonArrayPromotions = null;
		try {
			JSONArray jsonArray = new JSONArray(response);

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				jsonArrayPromotions = jsonObject.getJSONArray("promociones");
				if (jsonArrayPromotions != null) {
					for (int j = 0; j < jsonArrayPromotions.length(); j++) {
						JSONArray jsonArrayInside = jsonArrayPromotions
								.getJSONArray(j);

						for (int k = 0; k < jsonArrayInside.length(); k++) {
							JSONObject jsonObjectInside = jsonArrayInside
									.getJSONObject(k);
							String id = jsonObjectInside.getString("id");
							String description = jsonObjectInside
									.getString("content");
							String title = jsonObjectInside.getString("title");
							String urlImage = jsonObjectInside
									.getString("imagen");
							String discount = jsonObjectInside
									.getString("descuento");
							String price = jsonObjectInside.getString("precio");
							String original_price = jsonObjectInside
									.getString("precio original");
							String date = jsonObjectInside
									.getString("fecha_vencimiento");
							String id_comerce = jsonObjectInside
									.getString("comercio");
							String company = jsonObjectInside
									.getString("empresa");
							String promo_link = jsonObjectInside
									.getString("promo_link");
							String dia = jsonObjectInside.getString("dia");
							String mes = jsonObjectInside.getString("mes");
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
							promotion.setDay(dia);
							promotion.setMonth(mes);
							promotions.add(promotion);
						}
					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return promotions;
	}

	public String getPromosLinkPublizar(String id) {

		String url = "http://publizar.com.ve/api/api.php?o=getPromoLink&i="
				+ id;
		String link = null;
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(response);
			link = jsonObject.getString("link");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return link;
	}
}
