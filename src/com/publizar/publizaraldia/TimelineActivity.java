package com.publizar.publizaraldia;

import java.util.ArrayList;

import services.PromotionServices;

import domain.Promotion;

import adapters.ImageAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TimelineActivity extends Activity {
	public Bitmap placeholder;
	private ImageAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		ArrayList<Promotion> promotions = getPromotions(15);
		adapter = new ImageAdapter(this, R.layout.list_items, promotions);
		ListView listView = (ListView) findViewById(R.id.imagelist);
		listView.setAdapter(adapter);

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(listener);
	}

	public OnClickListener listener = new OnClickListener() {
		public void onClick(View arg0) {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}
	};

	public ArrayList<Promotion> getPromotions(int quantity) {
		String searchUrl = "http://www.publizar.com.ve/api/api.php?o=allpromos&l="
				+ quantity;
		PromotionServices promotionServices = new PromotionServices();

		ArrayList<Promotion> promotions = new ArrayList<Promotion>();
		promotions = promotionServices.getAllPromotions(searchUrl);
		return promotions;
	}

}
