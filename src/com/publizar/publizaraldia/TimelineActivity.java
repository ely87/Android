package com.publizar.publizaraldia;

import java.util.LinkedList;
import services.PromotionServices;
import domain.Promotion;
import adapters.ImageAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class TimelineActivity extends Activity {
	public Bitmap placeholder;
	private ListView list;
	private ImageAdapter adapter;
	private String[] mStrings = null;
	private LinkedList<Promotion> promotions;
	private LinkedList<Promotion> mListItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		mStrings = getPromotions(100);
		list = (ListView) findViewById(R.id.imagelist);
		mListItems = new LinkedList<Promotion>();
		mListItems.addAll(promotions);
		adapter = new ImageAdapter(this, mListItems, promotions);
		list.setAdapter(adapter);

		// Button b = (Button) findViewById(R.id.button1);
		// b.setOnClickListener(listener);
	}

	public OnClickListener listener = new OnClickListener() {
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	public String[] getPromotions(int quantity) {
		String searchUrl = "http://www.publizar.com.ve/api/api.php?o=allpromos&l="
				+ quantity;
		PromotionServices promotionServices = new PromotionServices();

		promotions = new LinkedList<Promotion>();
		promotions = promotionServices.getAllPromotions(searchUrl);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
		}
		return promotionsResult;
	}

}
