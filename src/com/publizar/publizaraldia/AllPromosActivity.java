package com.publizar.publizaraldia;

import java.util.ArrayList;

import services.PromotionServices;

import domain.Promotion;

import adapters.ImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AllPromosActivity extends Activity {
	public Bitmap placeholder;
	private ListView list;
	private ImageAdapter adapter;
	private String[] mStrings = null;
	private ArrayList<Promotion> promotions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		mStrings = getPromotions(100);
		list = (ListView) findViewById(R.id.imagelist);
		adapter = new ImageAdapter(this, mStrings, promotions);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				Promotion promotion = promotions.get(position);
				Intent intent = new Intent(AllPromosActivity.this,
						DetailActivity.class);
				intent.putExtra("Promo_image", promotion.getImageUrl());
				intent.putExtra("Promo_title", promotion.getName());
				intent.putExtra("Promo_due", promotion.getDue_date());
				startActivity(intent);
			}
		});
		// Button b = (Button) findViewById(R.id.button1);
		// b.setOnClickListener(listener);
	}


	public OnClickListener listener = new OnClickListener() {
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.navigation_menu, menu);
		return true;

	}

	public String[] getPromotions(int quantity) {
		String searchUrl = "http://www.publizar.com.ve/api/api.php?o=allpromos&l="
				+ quantity;
		PromotionServices promotionServices = new PromotionServices();

		promotions = new ArrayList<Promotion>();
		promotions = promotionServices.getAllPromotions(searchUrl);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImageUrl();
		}
		return promotionsResult;
	}

}
