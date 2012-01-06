package com.publizar.publizaraldia;

import java.util.ArrayList;

import services.CategoryService;

import domain.Category;
import adapters.CategoryAdapter;
import adapters.MultiDirectionSlidingDrawer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class SlidingDownActivity extends Activity {

	Button mCloseButton;
	Button mOpenButton;
	MultiDirectionSlidingDrawer mDrawer;
	public Bitmap placeholder;
	private ListView list;
	private CategoryAdapter adapter;
	private ArrayList<Category> categories;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.all_categories);
		// mCloseButton = (Button) findViewById(R.id.button_close);
		categories = getCategories();
		list = (ListView) findViewById(R.id.categories_list);
		adapter = new CategoryAdapter(this, categories);

		list.setAdapter(adapter);
		/*
		 * mCloseButton.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { mDrawer.animateClose(); } });
		 */

		/*
		 * mOpenButton.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { if (!mDrawer.isOpened())
		 * mDrawer.animateOpen(); } });
		 */

	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		// mCloseButton = (Button) findViewById(R.id.button_close);
		mOpenButton = (Button) findViewById(R.id.button_open);
		mDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
	}

	public ArrayList<Category> getCategories() {
		ArrayList<Category> allCategories = new ArrayList<Category>();

		CategoryService categoryService = new CategoryService();
		allCategories = categoryService.getAllCategories();

		return allCategories;

	}
}