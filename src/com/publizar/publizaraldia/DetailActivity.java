package com.publizar.publizaraldia;

import java.text.DecimalFormat;
import java.util.Calendar;
import persistence.FileDatabaseHelper;
import persistence.PromotionHelper;
import persistence.UserHelper;
import domain.Promotion;
import services.PromotionServices;
import adapters.ActionBar;
import adapters.ImageLoader;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.util.Linkify;

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
	private TextView detail_name_comerce;
	private TextView detail_tlf_comerce;
	private TextView detail_email_comerce;
	private TextView detail_twitter_comerce;
	private TextView detail_facebook_comerce;
	private TextView detail_name_comerce_text;
	private TextView detail_tlf_comerce_text;
	private TextView detail_email_comerce_text;
	private TextView detail_twitter_comerce_text;
	private TextView detail_facebook_comerce_text;
	private ImageView imageView;
	public ImageLoader imageLoader;
	private Button button_url;
	private Button button_send;
	private Button button_save_promotion;
	private Button mapButton;

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
	private String promo_excerpt;
	private String promo_idcomerce;
	private String promo_type;
	private String promo_day;
	private String promo_month;
	private AlertDialog.Builder email_alert;
	private PromotionHelper promotionHelper = new PromotionHelper(this);
	private UserHelper userHelper = new UserHelper(this);
	private FileDatabaseHelper fileDatabase;
	private Promotion initial_promotion;
	double latitude = 10.478001, longitude = -66.924891;
	Promotion promoResult;
	private ActionBar actionBar;
	private Button buttonShare;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promo_detail);
		Intent intent = getIntent();
		fileDatabase = new FileDatabaseHelper();
		promo_type = intent.getStringExtra("Type");
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
		promo_excerpt = intent.getStringExtra("Promo_excerpt");
		promo_idcomerce = intent.getStringExtra("Promo_idcomerce");

		if (promo_type.equalsIgnoreCase("Calendar")) {
			promo_day = intent.getStringExtra("Promo_day");
			promo_month = intent.getStringExtra("Promo_month");
		}

		imageLoader = new ImageLoader(this.getApplicationContext());

		email_alert = new AlertDialog.Builder(this);
		email_alert
				.setMessage(
						"Esta seguro que desea enviarse la promoci—n por correo?")
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						PromotionServices promotionServices = new PromotionServices();
						Promotion promotion = new Promotion();
						promotion.setId(Integer.valueOf(promo_id));
						userHelper.open();
						int idUser = userHelper.getLastRowID();
						Cursor c = userHelper.fetchUser(idUser);
						promotionServices.sendEmailPromotion(promo_id,
								c.getString(1));
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
		detail_name_comerce = (TextView) findViewById(R.id.promo_detail_name_comerce);
		detail_tlf_comerce = (TextView) findViewById(R.id.promo_detail_tlf_comerce);
		detail_email_comerce = (TextView) findViewById(R.id.promo_detail_email_comerce);
		detail_twitter_comerce = (TextView) findViewById(R.id.promo_detail_twitter_comerce);
		detail_facebook_comerce = (TextView) findViewById(R.id.promo_detail_facebook_comerce);
		detail_name_comerce_text = (TextView) findViewById(R.id.promo_detail_name_comerce_text);
		detail_tlf_comerce_text = (TextView) findViewById(R.id.promo_detail_tlf_comerce_text);
		detail_email_comerce_text = (TextView) findViewById(R.id.promo_detail_email_comerce_text);
		detail_twitter_comerce_text = (TextView) findViewById(R.id.promo_detail_twitter_comerce_text);
		detail_facebook_comerce_text = (TextView) findViewById(R.id.promo_detail_facebook_comerce_text);

		button_url = (Button) findViewById(R.id.promo_detail_button_buy);
		button_send = (Button) findViewById(R.id.promo_send_promotion);
		button_save_promotion = (Button) findViewById(R.id.promo_favourite_promotion);

		mapButton = (Button) findViewById(R.id.button_maps);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		// actionBar.setHomeAction(new IntentAction(this, createIntent(this),
		// R.drawable.ic_title_home_demo));
		actionBar.setTitle("Detalle de promocion");
		actionBar
				.setHomeAction(new IntentAction(this,
						PreferencesTimelineActivity.createIntent(this),
						R.drawable.home));
		actionBar.setDisplayHomeAsUpEnabled(true);

		final Action promos = new IntentAction(this, new Intent(this,
				AllPromosActivity.class), R.drawable.list);
		actionBar.addAction(promos);

		final Action calendar = new IntentAction(this, new Intent(this,
				CalendarListActivity.class), R.drawable.calendar);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				SettingsActivity.class), R.drawable.settings);
		actionBar.addAction(settings);

		buttonShare = (Button) findViewById(R.id.button_share);
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

		Button.OnClickListener shareButtonOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				share();
			}
		};

		Button.OnClickListener saveFavouritePromotionOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				Promotion promotion = new Promotion();
				promotion = setPromotion();

				promotionHelper.open();

				Cursor c = promotionHelper.fetchPromotions(promotion.getId());
				if (c.getCount() == 0) {
					promotionHelper.createPromotion(
							String.valueOf(promotion.getId()),
							promotion.getDescription(), promotion.getExcerpt(),
							promotion.getTitle(), promotion.getId_comerce(),
							promotion.getImage_url(), promotion.getDiscount(),
							promotion.getSaved_price(),
							promotion.getOriginal_price(),
							promotion.getPromo_company(),
							promotion.getDue_date(),
							promotion.getPromo_complete_url(),
							promotion.getFoursquare(), promotion.getComerce(),
							promotion.getComerce_tlf(),
							promotion.getWebsite_comerce(),
							promotion.getTwitter(), promotion.getFacebook(),
							promotion.getContact_email(),
							promotion.getForm_link(), promotion.getStatus());
				}
				Toast.makeText(getApplicationContext(),
						"La promoci—n ha sido guardada en sus favoritos",
						Toast.LENGTH_SHORT).show();
				promotionHelper.close();
				fileDatabase.exportDatabase();

			}
		};

		Button.OnClickListener mapsOnClickListener = new Button.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this,
						MapsActivity.class);
				intent.putExtra("Foursquare", promoResult.getFoursquare());
				startActivity(intent);
			}
		};

		button_url.setOnClickListener(launchBrowserOnClickListener);
		button_send.setOnClickListener(sendEmailPromotionOnClickListener);
		button_save_promotion
				.setOnClickListener(saveFavouritePromotionOnClickListener);
		mapButton.setOnClickListener(mapsOnClickListener);
		buttonShare.setOnClickListener(shareButtonOnClickListener);
		buttonShare.setVisibility(View.GONE);
		mapButton.setVisibility(View.GONE);
		new CallWebServiceTask().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		boolean result = false;
		if (promo_type.equalsIgnoreCase("Calendar")) {
			inflater.inflate(R.menu.calendar_menu, menu);
			result = true;
		} else {
			result = false;
		}
		return result;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addevent:
			addEvent();
			break;
		}
		return true;
	}

	public class CallWebServiceTask extends AsyncTask<String, Void, Promotion> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			setDetailsPromotion();
			initial_promotion = new Promotion();
			initial_promotion = setPromotion();

		}

		@Override
		protected Promotion doInBackground(String... params) {

			PromotionServices promotionService = new PromotionServices();
			Promotion promotion = new Promotion();
			promotion = promotionService.getExtraInformation(initial_promotion);
			return promotion;

			// return operationResult;
		}

		@Override
		protected void onPostExecute(Promotion promo_result) {

			if (!promo_result.getComerce().equalsIgnoreCase("null")
					&& !(promo_result.getComerce().length() == 0)) {
				detail_name_comerce.setText(" " + promo_result.getComerce());
			} else {
				detail_name_comerce_text.setVisibility(View.GONE);
				detail_name_comerce.setVisibility(View.GONE);
			}

			if (!promo_result.getComerce_tlf().equalsIgnoreCase("null")
					&& !(promo_result.getComerce_tlf().length() == 0)) {
				detail_tlf_comerce.setText(" " + promo_result.getComerce_tlf());
				promo_result.getComerce_tlf().replace("(", "");
				promo_result.getComerce_tlf().replace(")", "");
				promo_result.getComerce_tlf().replace(".", "");
				Linkify.addLinks(detail_tlf_comerce, Linkify.PHONE_NUMBERS);
			} else {
				detail_tlf_comerce.setVisibility(View.GONE);
				detail_tlf_comerce_text.setVisibility(View.GONE);
			}
			if (!promo_result.getContact_email().equalsIgnoreCase("null")
					&& !(promo_result.getContact_email().length() == 0)) {
				detail_email_comerce.setText(" "
						+ promo_result.getContact_email());
				Linkify.addLinks(detail_email_comerce, Linkify.EMAIL_ADDRESSES);
			} else {
				detail_email_comerce.setVisibility(View.GONE);
				detail_email_comerce_text.setVisibility(View.GONE);

			}
			if (!promo_result.getTwitter().equalsIgnoreCase("null")
					&& !(promo_result.getTwitter().length() == 0)) {
				detail_twitter_comerce.setText(Html.fromHtml("<a href="
						+ "http://www.twitter.com/" + promo_result.getTwitter()
						+ ">" + " @" + promo_result.getTwitter() + "</a>"));
				detail_twitter_comerce.setMovementMethod(LinkMovementMethod
						.getInstance());

			} else {
				detail_twitter_comerce.setVisibility(View.GONE);
				detail_twitter_comerce_text.setVisibility(View.GONE);
			}
			if (!promo_result.getFacebook().equalsIgnoreCase("null")
					&& !(promo_result.getFacebook().length() == 0)) {
				detail_facebook_comerce
						.setText("" + promo_result.getFacebook());
				Linkify.addLinks(detail_facebook_comerce, Linkify.WEB_URLS);

			} else {
				detail_facebook_comerce.setVisibility(View.GONE);
				detail_facebook_comerce_text.setVisibility(View.GONE);
			}

			promoResult = promo_result;
			mapButton.setVisibility(View.VISIBLE);
			buttonShare.setVisibility(View.VISIBLE);

		}
	}

	public void setDetailsPromotion() {
		detail_title.setText(title);
		if (promo_type.equalsIgnoreCase("Calendar")) {
			detail_due.setText(due_date);
		} else {
			int days = Integer.valueOf(due_date);
			if (days > 14000) {
				detail_due.setText("");
			} else if (days == 0) {
				detail_due.setText("Expira hoy");
			} else {
				detail_due.setText("Expira en " + due_date + " dias");
			}
		}
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
				String result_price = promo_price.substring(4,
						promo_price.length());
				result_price = result_price.replace(".", "");

				double price = Float.valueOf(result_price);
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
		if (!promo_type.equalsIgnoreCase("Favourite")) {
			imageLoader.DisplayImage(image, detail_image);
		} else {
			promotionHelper.open();
			byte[] imageByteArray = promotionHelper
					.setImageFavourites(promo_id);
			java.io.ByteArrayInputStream imageStream = new java.io.ByteArrayInputStream(
					imageByteArray);

			imageView = (ImageView) findViewById(R.id.promo_detail_image);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setAdjustViewBounds(true);

			imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
			promotionHelper.close();
		}
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

	public void share() {

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		PromotionServices promotionServices = new PromotionServices();
		String link = promotionServices.getPromosLinkPublizar(promo_id);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " "
				+ link + " (via @publizar)");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Promocion: " + title);
		startActivity(Intent.createChooser(sharingIntent, "Compartir usando"));

	}

	public Promotion setPromotion() {
		Promotion promotion_storage;
		promotion_storage = new Promotion();

		promotion_storage.setId(Integer.valueOf(promo_id));
		promotion_storage.setDescription(promo_description);
		promotion_storage.setExcerpt(promo_excerpt);
		promotion_storage.setImage_url(image);
		promotion_storage.setDue_date(due_date);
		promotion_storage.setPromo_company(promo_company);
		promotion_storage.setComerce(promo_comerce);
		promotion_storage.setSaved_price(promo_price);
		promotion_storage.setOriginal_price(promo_original_price);
		promotion_storage.setDiscount(promo_discount);
		promotion_storage.setPromo_complete_url(promo_website);
		promotion_storage.setStatus("favorito");
		promotion_storage.setTitle(title);
		promotion_storage.setId_comerce(promo_idcomerce);
		return promotion_storage;

	}

	private void addEvent() {

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");

		Calendar calendar = parseDate();
		intent.putExtra("beginTime", calendar.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("endTime", calendar.getTimeInMillis());
		intent.putExtra("title", title);
		intent.putExtra("eventStatus", 1);
		intent.putExtra("visibility", 0);
		intent.putExtra("transparency", 0);
		intent.putExtra("hasAlarm", 1);
		intent.putExtra("description", promo_description);
		intent.putExtra("EVENT_LOCATION", promo_comerce);

		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this.getApplicationContext(),
					"Sorry, no compatible calendar is found!",
					Toast.LENGTH_LONG).show();
		}
	}

	public Calendar parseDate() {
		int day = Integer.valueOf(promo_day);
		int month = 0;
		if (promo_month.equalsIgnoreCase("enero")) {
			month = 0;
		} else if (promo_month.equalsIgnoreCase("febrero")) {
			month = 1;
		} else if (promo_month.equalsIgnoreCase("marzo")) {
			month = 2;
		} else if (promo_month.equalsIgnoreCase("abril")) {
			month = 3;
		} else if (promo_month.equalsIgnoreCase("mayo")) {
			month = 4;
		} else if (promo_month.equalsIgnoreCase("junio")) {
			month = 5;
		} else if (promo_month.equalsIgnoreCase("julio")) {
			month = 6;
		} else if (promo_month.equalsIgnoreCase("agosto")) {
			month = 7;
		} else if (promo_month.equalsIgnoreCase("septiembre")) {
			month = 8;
		} else if (promo_month.equalsIgnoreCase("octubre")) {
			month = 9;
		} else if (promo_month.equalsIgnoreCase("noviembre")) {
			month = 10;
		} else if (promo_month.equalsIgnoreCase("diciembre")) {
			month = 11;
		}

		int dateCount = due_date.length();
		int yearCount = dateCount - 4;
		int year = Integer.valueOf(due_date.substring(yearCount, dateCount));

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar;

	}
}
