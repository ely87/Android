package com.publizar.publizaraldia;

import java.util.ArrayList;

import persistence.PromotionHelper;
import adapters.FavouriteAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import domain.Promotion;

public class AllFavouritesActivity extends Activity {

	public Bitmap placeholder;
	private ListView list;
	private FavouriteAdapter adapter;
	private ArrayList<Promotion> promotions;
	private PromotionHelper promotionHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.all_favourites);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		promotionHelper = new PromotionHelper(this);
		promotions = getPromotions();
		list = (ListView) findViewById(R.id.imagelist1);

		adapter = new FavouriteAdapter(this, promotions);
		list.setAdapter(adapter);
		list.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					Toast.makeText(getApplicationContext(), "On Key Pressed",
							Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				Promotion promotion = promotions.get(position);
				Intent intent = new Intent(AllFavouritesActivity.this,
						DetailActivity.class);
				intent.putExtra("Type", "Favourite");
				intent.putExtra("Promo_id", String.valueOf(promotion.getId()));
				intent.putExtra("Promo_image", promotion.getImage_url());
				intent.putExtra("Promo_title", promotion.getTitle());
				intent.putExtra("Promo_due", promotion.getDue_date());
				intent.putExtra("Promo_company", promotion.getPromo_company());
				intent.putExtra("Promo_comerce", promotion.getComerce());
				intent.putExtra("Promo_price", promotion.getSaved_price());
				intent.putExtra("Promo_original_price",
						promotion.getOriginal_price());
				intent.putExtra("Promo_discount", promotion.getDiscount());
				intent.putExtra("Promo_description", promotion.getDescription());
				intent.putExtra("Promo_website",
						promotion.getPromo_complete_url());
				intent.putExtra("Promo_excerpt", promotion.getExcerpt());
				intent.putExtra("Promo_idcomerce", promotion.getId_comerce());
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.account:
			Intent intent = new Intent(AllFavouritesActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			break;

		case R.id.timeline:
			Intent intentTimeline = new Intent(AllFavouritesActivity.this,
					PreferencesTimelineActivity.class);
			startActivity(intentTimeline);
			break;

		}
		return true;
	}

	public ArrayList<Promotion> getPromotions() {
		ArrayList<Promotion> allFavourites = new ArrayList<Promotion>();
		promotionHelper.open();
		Cursor mCur = promotionHelper.fetchAllPromotions();
		mCur.moveToFirst();

		while (mCur.isAfterLast() == false) {
			Promotion promotion = new Promotion();
			promotion.setId(Integer.valueOf(mCur.getString(0)));
			promotion.setDescription(mCur.getString(1));
			promotion.setExcerpt(mCur.getString(2));
			promotion.setTitle(mCur.getString(3));
			promotion.setId_comerce(mCur.getString(4));
			promotion.setDiscount(mCur.getString(6));
			promotion.setSaved_price(mCur.getString(7));
			promotion.setOriginal_price(mCur.getString(8));
			promotion.setPromo_company(mCur.getString(9));
			promotion.setDue_date(mCur.getString(10));
			promotion.setPromo_complete_url(mCur.getString(11));
			allFavourites.add(promotion);
			mCur.moveToNext();
		}
		promotionHelper.close();
		return allFavourites;

	}

}
