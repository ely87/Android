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
			
			for(int i = 0; i< jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String description = jsonObject.getString("content");
				String excerpt = jsonObject.getString("excerpt");
				String title = jsonObject.getString("title");
				String urlImage = jsonObject.getString("imagen");
				String discount = jsonObject.getString("descuento");
				String price = jsonObject.getString("precio");
				String date = jsonObject.getString("fecha_vencimiento");
				Promotion promotion = new Promotion();
				promotion.setId(Integer.valueOf(id));
				promotion.setDescription(description);
				promotion.setExcerpt(excerpt);
				promotion.setName(title);
				promotion.setImageUrl(urlImage);
				promotion.setDue_date(date);
				promotion.setDiscount(discount);
				promotion.setPromotional_price(price);
				promotions.add(promotion);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//promotions.add(promotion);
		return promotions;

	}
}
