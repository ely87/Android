package com.publizar.publizaraldia;

import adapters.ImageLoader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {

	private ImageView detail_image;
	private TextView detail_title;
	private TextView detail_due;
	private String title;
	private String image;
	private String due_date;
	public ImageLoader imageLoader;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.promo_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		Intent intent = getIntent();
		title = intent.getStringExtra("Promo_title");
		image = intent.getStringExtra("Promo_image");
		due_date = intent.getStringExtra("Promo_due");

		imageLoader = new ImageLoader(this.getApplicationContext());

		detail_image = (ImageView) findViewById(R.id.promo_detail_image);
		detail_title = (TextView) findViewById(R.id.promo_detail_title);
		detail_due = (TextView) findViewById(R.id.promo_detail_due);
		setDetailsPromotion();
	}

	public void setDetailsPromotion() {
		detail_title.setText(title);
		detail_due.setText("Expira en " + due_date + " dias");
		imageLoader.DisplayImage(image, detail_image);
	}
}
