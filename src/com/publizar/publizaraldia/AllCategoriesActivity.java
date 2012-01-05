package com.publizar.publizaraldia;

import java.util.ArrayList;

import persistence.PromotionHelper;
import services.CategoryService;
import adapters.CategoryAdapter;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import domain.Category;
import domain.Promotion;

public class AllCategoriesActivity extends Activity {

	public Bitmap placeholder;
	private ListView list;
	private CategoryAdapter adapter;
	private ArrayList<Category> categories;
	private PromotionHelper promotionHelper;
	private ImageButton buttonDelete;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.all_categories);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		promotionHelper = new PromotionHelper(this);
		categories = getCategories();
		list = (ListView) findViewById(R.id.categories_list);
		String[] data = new String[3];
		adapter = new CategoryAdapter(this, categories, data);

		list.setAdapter(adapter);

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

		return allCategories;

	}
}
