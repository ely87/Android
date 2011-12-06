package com.publizar.publizaraldia;

import persistence.PromotionHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class TestDetailActivity extends Activity {

	private ImageView imageView;
	private PromotionHelper promotionHelper;
	private String id;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.testfavourite);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);

		Intent intent = getIntent();
		id = intent.getStringExtra("Promo_id");
		promotionHelper = new PromotionHelper(this);
		// Bitmap theImage = BitmapFactory.decodeStream(imageStream);

		byte[] imageByteArray = promotionHelper.setImageFavourites(id);
		java.io.ByteArrayInputStream imageStream = new java.io.ByteArrayInputStream(
				imageByteArray);

		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setAdjustViewBounds(true);

		imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
	}
}
