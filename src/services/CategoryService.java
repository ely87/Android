package services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import connection.HttpClient;
import domain.Category;

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
	//
	//
}
