package com.publizar.publizaraldia;

import java.text.DecimalFormat;
import adapters.ImageLoader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {

	private ImageView detail_image;
	private TextView detail_title;
	private TextView detail_due;
	private TextView detail_promo_company;
	private TextView detail_original_price;
	private TextView detail_saved_price;
	private TextView detail_discount;
	private TextView detail_description;
	private TextView promo_detail_text1;
	private TextView promo_detail_text2;
	private TextView promo_detail_text3;
	public ImageLoader imageLoader;
	private Button button_url;

	private String title;
	private String image;
	private String due_date;
	private String promo_company;
	private String promo_comerce;
	private String promo_price;
	private String promo_original_price;
	private String promo_discount;
	private String promo_description;

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
		promo_company = intent.getStringExtra("Promo_company");
		promo_comerce = intent.getStringExtra("Promo_comerce");
		promo_price = intent.getStringExtra("Promo_price");
		promo_original_price = intent.getStringExtra("Promo_original_price");
		promo_discount = intent.getStringExtra("Promo_discount");
		promo_description = intent.getStringExtra("Promo_description");
		imageLoader = new ImageLoader(this.getApplicationContext());

		detail_image = (ImageView) findViewById(R.id.promo_detail_image);
		detail_title = (TextView) findViewById(R.id.promo_detail_title);
		detail_due = (TextView) findViewById(R.id.promo_detail_due);
		detail_promo_company = (TextView) findViewById(R.id.promo_detail_company);
		detail_original_price = (TextView) findViewById(R.id.promo_detail_op);
		detail_saved_price = (TextView) findViewById(R.id.promo_detail_sp);
		detail_discount = (TextView) findViewById(R.id.promo_detail_d);
		detail_description = (TextView) findViewById(R.id.promo_detail_description);
		promo_detail_text1 = (TextView) findViewById(R.id.promo_detail_text);
		promo_detail_text2 = (TextView) findViewById(R.id.promo_detail_text2);
		promo_detail_text3 = (TextView) findViewById(R.id.promo_detail_text3);
		button_url = (Button) findViewById(R.id.promo_detail_button_buy);
		setDetailsPromotion();
	}

	public void setDetailsPromotion() {
		detail_title.setText(title);
		detail_due.setText("Expira en " + due_date + " dias");

		if (promo_company.length() > 0) {
			detail_promo_company.setText(promo_company);
			button_url.setText("Compralo por  " + promo_price);
			double original_price = Float.valueOf(promo_original_price
					.substring(4, promo_original_price.length()));
			double price = Float.valueOf(promo_price.substring(4,
					promo_price.length()));

			double saved_value = original_price - price;
			String saved_price = getCalculatedValue(saved_value);
			detail_original_price.setText(promo_original_price);
			detail_saved_price.setText("Bs. " + saved_price);
			detail_discount.setText(promo_discount);
		} else {
			promo_detail_text1.setVisibility(View.GONE);
			promo_detail_text2.setVisibility(View.GONE);
			promo_detail_text3.setVisibility(View.GONE);
			detail_original_price.setText("");
			detail_saved_price.setText("");
			detail_discount.setText("");
			detail_promo_company.setText(promo_comerce);
			button_url.setText("Ver promocion");
		}
		detail_description.setText(promo_description);
		imageLoader.DisplayImage(image, detail_image);
	}

	public String getCalculatedValue(double value) {
		DecimalFormat df = null;
		long iPart = (long) value;
		double fPart = value - iPart;
		String result = null;

		if (fPart == 0) {
			result = String.valueOf(iPart);

		} else {
			df = new DecimalFormat("0.000");
			result = df.format(value);
		}
		return result;

	}
}
