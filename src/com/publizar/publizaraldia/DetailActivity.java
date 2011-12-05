package com.publizar.publizaraldia;

import java.text.DecimalFormat;

import persistence.UserDAO;

import domain.Promotion;
import domain.User;

import services.PromotionServices;
import adapters.ImageLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private Button button_send;

	private String title;
	private String image;
	private String due_date;
	private String promo_id;
	private String promo_company;
	private String promo_comerce;
	private String promo_price;
	private String promo_original_price;
	private String promo_discount;
	private String promo_description;
	private String promo_website;
	private AlertDialog.Builder email_alert;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.promo_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.navigation_bar);
		Intent intent = getIntent();
		promo_id = intent.getStringExtra("Promo_id");
		title = intent.getStringExtra("Promo_title");
		image = intent.getStringExtra("Promo_image");
		due_date = intent.getStringExtra("Promo_due");
		promo_company = intent.getStringExtra("Promo_company");
		promo_comerce = intent.getStringExtra("Promo_comerce");
		promo_price = intent.getStringExtra("Promo_price");
		promo_original_price = intent.getStringExtra("Promo_original_price");
		promo_discount = intent.getStringExtra("Promo_discount");
		promo_description = intent.getStringExtra("Promo_description");
		promo_website = intent.getStringExtra("Promo_website");
		imageLoader = new ImageLoader(this.getApplicationContext());

		email_alert = new AlertDialog.Builder(this);
		email_alert
				.setMessage(
						"¿Está seguro que desea enviarse la promoción por correo?")
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						PromotionServices promotionServices = new PromotionServices();
						Promotion promotion = new Promotion();
						promotion.setId(Integer.valueOf(promo_id));
						UserDAO userDAO = new UserDAO();
						User user = new User();
						user = userDAO.selectUser();
						promotionServices.sendEmailPromotion(promo_id,
								user.getEmail());
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

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
		button_send = (Button) findViewById(R.id.promo_send_promotion);

		setDetailsPromotion();

		Button.OnClickListener launchBrowserOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(DetailActivity.this,
						WebLoaderActivity.class);
				intent.putExtra("Promo_website", promo_website);
				startActivity(intent);
			}
		};

		Button.OnClickListener sendEmailPromotionOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				email_alert.show();
			}
		};

		button_url.setOnClickListener(launchBrowserOnClickListener);
		button_send.setOnClickListener(sendEmailPromotionOnClickListener);
	}

	public void setDetailsPromotion() {
		detail_title.setText(title);
		detail_due.setText("Expira en " + due_date + " dias");

		if (promo_company.length() > 0) {
			detail_promo_company.setText(promo_company);
			button_url.setText("Compralo por  " + promo_price);
			String saved_price = null;

			if (promo_original_price.length() == 0) {
				saved_price = String.valueOf(0);
				promo_original_price = promo_price;
				promo_discount = "Sin descuento";
			} else {
				String result = promo_original_price.substring(4,
						promo_original_price.length());
				result = result.replace(".", "");
				double original_price = Float.valueOf(result);
				double price = Float.valueOf(promo_price.substring(4,
						promo_price.length()));
				double saved_value = original_price - price;

				saved_price = getCalculatedValue(saved_value);
			}

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
			// int index = result.lastIndexOf('.');
			// result.
		}

		return result;

	}
}
