package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.LinkedList;
import services.CategoryService;
import services.PromotionServices;
import domain.Category;
import domain.Promotion;
import adapters.ActionBar;
import adapters.CategoryAdapter;
import adapters.ImageAdapter;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import adapters.PullToRefreshListView.OnRefreshListener;
import android.app.Activity;
import android.app.AlertDialog;
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
import adapters.PullToRefreshListView;

public class AllPromosActivity extends Activity {

	public Bitmap placeholder;
	private LinkedList<Promotion> mListItems;
	private ListView list;
	private ImageAdapter adapter;
	private String[] mStrings = null;
	private LinkedList<Promotion> promotions;
	private ArrayList<Category> categories;
	private ListView listCategory;
	private CategoryAdapter adapterCategory;
	private AlertDialog.Builder promotionAlert;
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		// actionBar.setHomeAction(new IntentAction(this, createIntent(this),
		// R.drawable.ic_title_home_demo));
		actionBar.setTitle("Promociones");
		actionBar
				.setHomeAction(new IntentAction(this,
						PreferencesTimelineActivity.createIntent(this),
						R.drawable.home));
		actionBar.setDisplayHomeAsUpEnabled(true);

		final Action calendar = new IntentAction(this, new Intent(this,
				CalendarListActivity.class), R.drawable.calendar);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				SettingsActivity.class), R.drawable.settings);
		actionBar.addAction(settings);

		list = (ListView) findViewById(R.id.imagelist);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				int pos = position - 1;
				Promotion promotion = promotions.get(pos);
				Intent intent = new Intent(AllPromosActivity.this,
						DetailActivity.class);
				intent.putExtra("Type", "Server");
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

		promotionAlert = new AlertDialog.Builder(this);
		promotionAlert.setTitle("Promociones");
		promotionAlert.setPositiveButton("Ok", null);
		promotionAlert.setCancelable(true);

		((PullToRefreshListView) list)
				.setOnRefreshListener(new OnRefreshListener() {
					public void onRefresh() {
						// Do work to refresh the list here.
						new GetDataTask().execute();
					}
				});

		final SlidingDrawer slider = (SlidingDrawer) findViewById(R.id.slidingDrawer);

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
					LinkedList<Promotion> promosByTags = new LinkedList<Promotion>();
					promosByTags = getPromotionsByTags(category);
					LinkedList<Promotion> mListTagsItems = new LinkedList<Promotion>();
					mListTagsItems.addAll(promosByTags);
					ImageAdapter adapterTags = new ImageAdapter(
							AllPromosActivity.this, mListTagsItems,
							promosByTags);
					list.setAdapter(adapterTags);
				} else {
					LinkedList<Promotion> promosByCategories = new LinkedList<Promotion>();
					promosByCategories = getPromotionsByCategories(category);
					LinkedList<Promotion> mListCategoriesItems = new LinkedList<Promotion>();
					mListCategoriesItems.addAll(promosByCategories);
					ImageAdapter adapterCategories = new ImageAdapter(
							AllPromosActivity.this, mListCategoriesItems,
							promosByCategories);
					list.setAdapter(adapterCategories);
				}
				slider.animateClose();
			}
		});

		CallWebServiceTask task = new CallWebServiceTask();
		task.applicationContext = AllPromosActivity.this;
		task.execute();
	}

	public class CallWebServiceTask extends
			AsyncTask<String, Void, ImageAdapter> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
		}

		@Override
		protected ImageAdapter doInBackground(String... params) {
			mStrings = getPromotions(100);
			mListItems = new LinkedList<Promotion>();
			mListItems.addAll(promotions);
			final ImageAdapter adapter = new ImageAdapter(applicationContext,
					mListItems, promotions);
			return adapter;
		}

		@Override
		protected void onPostExecute(ImageAdapter adapter) {
			actionBar.setProgressBarVisibility(View.GONE);
			list.setAdapter(adapter);
		}
	}

	public OnClickListener listener = new OnClickListener() {
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				;
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			Promotion promotion = new Promotion();
			promotion = promotions.get(0);

			LinkedList<Promotion> promotionsRefresh = new LinkedList<Promotion>();
			promotionsRefresh = getRefreshPromotions(promotion.getPost_date());
			for (int i = 0; i < promotionsRefresh.size(); i++) {
				promotions.addFirst(promotionsRefresh.get(i));
				mListItems.addFirst(promotionsRefresh.get(i));
			}

			((PullToRefreshListView) list).onRefreshComplete();

			super.onPostExecute(result);
		}
	}

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

		promotions = new LinkedList<Promotion>();
		promotions = promotionServices.getAllPromotions(searchUrl);

		String[] promotionsResult = new String[promotions.size()];

		for (int i = 0; i < promotions.size(); i++) {
			promotionsResult[i] = promotions.get(i).getImage_url();
		}
		return promotionsResult;
	}

	public LinkedList<Promotion> getPromotionsByCategories(Category category) {
		CategoryService categoryServices = new CategoryService();

		LinkedList<Promotion> promotionsByCategories = new LinkedList<Promotion>();
		promotionsByCategories = categoryServices
				.getPromosByCategories(category);

		return promotionsByCategories;
	}

	public LinkedList<Promotion> getPromotionsByTags(Category category) {
		CategoryService categoryServices = new CategoryService();

		LinkedList<Promotion> promotions = new LinkedList<Promotion>();
		promotions = categoryServices.getPromosByTags(category);

		return promotions;
	}

	public LinkedList<Promotion> getRefreshPromotions(String date) {
		date = date.replace(" ", "%20");
		String searchUrl = "http://publizar.com.ve/api/api.php?o=refreshAllPromos&d="
				+ date;

		PromotionServices promotionServices = new PromotionServices();

		LinkedList<Promotion> promotions = new LinkedList<Promotion>();
		promotions = promotionServices.getRefreshPromotion(searchUrl);

		return promotions;
	}

}
