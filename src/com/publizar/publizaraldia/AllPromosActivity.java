package com.publizar.publizaraldia;

import java.util.ArrayList;

import connection.VerifyConnection;
import services.CategoryService;
import services.PromotionServices;
import domain.Category;
import domain.Promotion;
import adapters.CategoryAdapter;
import adapters.ImageAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.SlidingDrawer;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AllPromosActivity extends Activity {
	public Bitmap placeholder;
	private ListView list;
	private ImageAdapter adapter;
	private String[] mStrings = null;
	private ArrayList<Promotion> promotions;
	private ArrayList<Category> categories;
	private ListView listCategory;
	private CategoryAdapter adapterCategory;
	private VerifyConnection connection;
	private boolean wifi = false;
	private boolean mobile = false;
	private ProgressDialog promotionDialog;
	private AlertDialog.Builder promotionAlert;

	// private TextView appTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		connection = new VerifyConnection(this);
		// boolean wifi = false;
		// boolean mobile = false;
		list = (ListView) findViewById(R.id.imagelist);
		wifi = connection.haveNetworkWiFiConnection();
		mobile = connection.haveNetworkMobileConnection();
		promotionDialog = new ProgressDialog(this);
		promotionDialog.setIndeterminate(true);
		promotionDialog.setTitle("Cargando promociones");
		promotionDialog.setMessage("Espere un momento");

		promotionAlert = new AlertDialog.Builder(this);
		promotionAlert.setTitle("Promociones");
		promotionAlert.setPositiveButton("Ok", null);
		promotionAlert.setCancelable(true);

		if (wifi || mobile) {
			mStrings = getPromotions(100);
			adapter = new ImageAdapter(this, mStrings, promotions);
			list.setAdapter(adapter);

			final SlidingDrawer slider = (SlidingDrawer) findViewById(R.id.slidingDrawer);
			/*
			 * RelativeLayout navigationBar = (RelativeLayout)
			 * findViewById(R.id.navigation_bar_main); appTitle = (TextView)
			 * navigationBar.getChildAt(1);
			 * 
			 * appTitle.setOnClickListener(new OnClickListener() {
			 * 
			 * public void onClick(View v) {
			 * list.setSelection(adapter.getCount() - 1); } });
			 */

			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long duration) {
					Promotion promotion = promotions.get(position);
					Intent intent = new Intent(AllPromosActivity.this,
							DetailActivity.class);
					intent.putExtra("Type", "Server");
					intent.putExtra("Promo_id",
							String.valueOf(promotion.getId()));
					intent.putExtra("Promo_image", promotion.getImage_url());
					intent.putExtra("Promo_title", promotion.getTitle());
					intent.putExtra("Promo_due", promotion.getDue_date());
					intent.putExtra("Promo_company",
							promotion.getPromo_company());
					intent.putExtra("Promo_comerce", promotion.getComerce());
					intent.putExtra("Promo_price", promotion.getSaved_price());
					intent.putExtra("Promo_original_price",
							promotion.getOriginal_price());
					intent.putExtra("Promo_discount", promotion.getDiscount());
					intent.putExtra("Promo_description",
							promotion.getDescription());
					intent.putExtra("Promo_website",
							promotion.getPromo_complete_url());
					intent.putExtra("Promo_excerpt", promotion.getExcerpt());
					intent.putExtra("Promo_idcomerce",
							promotion.getId_comerce());
					startActivity(intent);
				}
			});

			categories = getCategories();
			listCategory = (ListView) findViewById(R.id.categories_list);
			adapterCategory = new CategoryAdapter(this, this, categories);

			listCategory.setAdapter(adapterCategory);

			listCategory.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long duration) {
					Category category = new Category();
					category = categories.get(position);

					if (category.isImageContainer()) {
						String[] promosByTags = null;
						promosByTags = getPromotionsByTags(category);
						adapter = new ImageAdapter(AllPromosActivity.this,
								promosByTags, promotions);
						list.setAdapter(adapter);
					} else {
						String[] promosByCategories = null;
						promosByCategories = getPromotionsByCategories(category);
						adapter = new ImageAdapter(AllPromosActivity.this,
								promosByCategories, promotions);
						list.setAdapter(adapter);
					}
					slider.animateClose();
				}
			});
		}
	}

	public OnClickListener listener = new OnClickListener() {
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	public ArrayList<Category> getCategories() {
		ArrayList<Category> allCategories = new ArrayList<Category>();

		CategoryService categoryService = new CategoryService();
		allCategories = categoryService.getAllCategories();

		return allCategories;

	}

	public String[] getPromotions(int quantity) {
		String searchUrl = "http://www.publizar.com.ve/api/api.php?o=allpromos&l="
				+ quantity;
		PromotionServices promotionServices = new PromotionServices();

		promotions = new ArrayList<Promotion>();
		promotions = promotionServices.getAllPromotions(searchUrl);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
		}
		return promotionsResult;
	}

	public String[] getPromotionsByCategories(Category category) {
		CategoryService categoryServices = new CategoryService();

		promotions = new ArrayList<Promotion>();
		promotions = categoryServices.getPromosByCategories(category);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
		}
		return promotionsResult;
	}

	public String[] getPromotionsByTags(Category category) {
		CategoryService categoryServices = new CategoryService();

		promotions = new ArrayList<Promotion>();
		promotions = categoryServices.getPromosByTags(category);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
		}
		return promotionsResult;
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
		return;
	}

	public void onResume() {
		wifi = connection.haveNetworkWiFiConnection();
		mobile = connection.haveNetworkMobileConnection();
		if (wifi || mobile) {
			LoadPromotionsTask task = new LoadPromotionsTask();
			task.applicationContext = AllPromosActivity.this;
			task.execute();
			adapter.notifyDataSetChanged();
		} else {
			// adapter = new ImageAdapter(applicationContext, mStrings,
			// promotions);
		}
		super.onResume();
	}

	public class LoadPromotionsTask extends AsyncTask<String, Void, Integer> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			promotionDialog.show();
		}

		@Override
		protected Integer doInBackground(String... params) {

			int operationResult = 0;
			VerifyConnection connection = new VerifyConnection(
					applicationContext);
			boolean wifi = false;
			boolean mobile = false;

			wifi = connection.haveNetworkWiFiConnection();
			mobile = connection.haveNetworkMobileConnection();
			if (wifi || mobile) {
				mStrings = getPromotions(100);
				adapter = new ImageAdapter(applicationContext, mStrings,
						promotions);

			} else {
				operationResult = -2;
			}

			return operationResult;
		}

		@Override
		protected void onPostExecute(Integer operationResult) {
			promotionDialog.cancel();
			list.setAdapter(adapter);
		}
	}
}
