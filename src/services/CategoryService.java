package services;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import connection.HttpClient;
import domain.Category;
import domain.Promotion;

public class CategoryService {

	public ArrayList<Category> getCategories() {
		String url = "http://publizar.com.ve/api/api.php?o=allcategories";
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		ArrayList<Category> categories = new ArrayList<Category>();
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				Category category = new Category();
				jsonObject = jsonArray.getJSONObject(i);
				category.setName(jsonObject.getString("name"));
				category.setSlug(jsonObject.getString("slug"));
				category.setImageContainer(false);
				categories.add(category);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return categories;
	}

	public ArrayList<Category> getSpecialSections() {
		ArrayList<Category> categories = new ArrayList<Category>();
		String url = "http://publizar.com.ve/api/api.php?o=getSpecialSections";
		String response = null;
		HttpClient httpClient = new HttpClient();
		response = httpClient.getHTTPRequest(url);
		JSONObject jsonObject = null;
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				Category category = new Category();
				jsonObject = jsonArray.getJSONObject(i);
				JSONObject section = jsonObject.getJSONObject("section");
				category.setName(section.getString("nombre"));
				category.setSlug(section.getString("tag"));
				category.setImage(section.getString("url"));
				category.setImageContainer(true);
				categories.add(category);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}

	public ArrayList<Category> getAllCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		ArrayList<Category> specialSections = new ArrayList<Category>();
		ArrayList<Category> all = new ArrayList<Category>();

		specialSections = getSpecialSections();
		categories = getCategories();

		int totalSize = specialSections.size() + categories.size();

		for (int i = 0; i < totalSize; i++) {
			if (i < specialSections.size()) {
				all.add(specialSections.get(i));
			} else {
				int j = categories.size() - (totalSize - i);
				all.add(categories.get(j));
			}
		}
		return all;
	}

	public LinkedList<Promotion> getPromosByCategories(Category category) {
		LinkedList<Promotion> promotions = new LinkedList<Promotion>();

		String pref = category.getSlug().replace(" ", "%20");
		String url = "http://www.publizar.com.ve/api/api.php?o=getpcategories&cat="
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

	public LinkedList<Promotion> getPromosByTags(Category category) {
		LinkedList<Promotion> promotions = new LinkedList<Promotion>();

		String pref = category.getSlug().replace(" ", "%20");
		String url = "http://www.publizar.com.ve/api/api.php?o=getptags&tag="
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
}
