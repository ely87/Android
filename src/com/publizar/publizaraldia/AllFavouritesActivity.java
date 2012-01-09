package com.publizar.publizaraldia;

import java.util.ArrayList;

import persistence.PromotionHelper;
import adapters.FavouriteAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
		setContentView(R.layout.all_favourites);
		promotionHelper = new PromotionHelper(this);
		promotions = getPromotions();
		list = (ListView) findViewById(R.id.imagelist1);

		adapter = new FavouriteAdapter(this, promotions);

		list.setAdapter(adapter);

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
		inflater.inflate(R.menu.favourite_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			for (int i = 0; i < list.getChildCount(); i++) {
				RelativeLayout itemLayout = (RelativeLayout) list.getChildAt(i);
				CheckBox cb = (CheckBox) itemLayout
						.findViewById(R.id.checkBox1);
				cb.setVisibility(View.VISIBLE);

			}

			deleteFavourites();

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

	public boolean deleteFavourites() {
		boolean result = false;
		promotionHelper.open();
		for (int i = 0; i < list.getChildCount(); i++) {
			RelativeLayout itemLayout = (RelativeLayout) list.getChildAt(i);
			CheckBox cb = (CheckBox) itemLayout.findViewById(R.id.checkBox1);
			if (cb.isChecked()) {
				TextView title = (TextView) itemLayout
						.findViewById(R.id.promo_title1);
				for (int j = 0; j < promotions.size(); j++) {
					Promotion promotion = new Promotion();
					promotion = promotions.get(j);
					if (title.getText().toString()
							.equalsIgnoreCase(promotion.getTitle())) {
						promotionHelper.deletePromotion(promotion.getId());
						adapter.delete(promotion);
					}
				}

			} else {
				cb.setChecked(false);
			}
		}
		promotionHelper.close();
		return result;
	}

	public void onResume() {
		promotions = getPromotions();
		list = (ListView) findViewById(R.id.imagelist1);

		adapter = new FavouriteAdapter(this, promotions);

		list.setAdapter(adapter);

		adapter.notifyDataSetChanged();
		super.onResume();
	}

}
