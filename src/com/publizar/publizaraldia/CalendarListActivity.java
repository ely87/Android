package com.publizar.publizaraldia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import services.PromotionServices;
import domain.Promotion;
import adapters.ActionBar;
import adapters.CalendarAdapter;
import adapters.SeparatedListAdapter;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CalendarListActivity extends Activity {
	public final static String ITEM_TITLE = "title";
	public final static String ITEM_CAPTION = "caption";
	private ArrayList<Promotion> promotions;
	private ListView calendarListView;
	private ActionBar actionBar;
	// SectionHeaders
	private final static String[] months = new String[] { "Enero", "Febrero",
			"Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
			"Octubre", "Noviembre", "Diciembre" };

	// Adapter for ListView Contents
	private SeparatedListAdapter adapter;

	public Map<String, ?> createItem(String title, String caption) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.calendar_list);
		adapter = new SeparatedListAdapter(this);
		calendarListView = (ListView) this.findViewById(R.id.list_months);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Calendario");
		actionBar
				.setHomeAction(new IntentAction(this,
						PreferencesTimelineActivity.createIntent(this),
						R.drawable.home));
		actionBar.setDisplayHomeAsUpEnabled(true);

		final Action calendar = new IntentAction(this, new Intent(this,
				AllPromosActivity.class), R.drawable.list);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				SettingsActivity.class), R.drawable.settings);
		actionBar.addAction(settings);

		calendarListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				Integer item = (Integer) adapter.getPosition(position);
				String mes = (String) adapter.getSection(position);
				Promotion promotion = getPromotionSelected(mes, item);

				Intent intent = new Intent(CalendarListActivity.this,
						DetailActivity.class);
				intent.putExtra("Type", "Calendar");
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
				intent.putExtra("Promo_day", promotion.getDay());
				intent.putExtra("Promo_month", promotion.getMonth());
				startActivity(intent);

			}
		});
		CallWebServiceTask task = new CallWebServiceTask();
		task.applicationContext = CalendarListActivity.this;
		task.execute();
	}

	public class CallWebServiceTask extends
			AsyncTask<String, Void, SeparatedListAdapter> {

		protected Context applicationContext;
		protected int result;

		@Override
		protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
		}

		@Override
		protected SeparatedListAdapter doInBackground(String... params) {
			ArrayList<Promotion> promosByMonths = new ArrayList<Promotion>();
			PromotionServices promotionServices = new PromotionServices();
			promotions = promotionServices.getPromosByMonths();
			for (int i = 0; i < months.length; i++) {
				promosByMonths = getPromotionsByMonths(months[i], promotions);
				if (promosByMonths.size() > 0) {
					CalendarAdapter listCalendar = new CalendarAdapter(
							applicationContext, promosByMonths);
					adapter.addSection(months[i], listCalendar);
				}
			}
			return adapter;
		}

		@Override
		protected void onPostExecute(SeparatedListAdapter adapter) {
			actionBar.setProgressBarVisibility(View.GONE);
			calendarListView.setAdapter(adapter);
		}
	}

	public ArrayList<Promotion> getPromotionsByMonths(String month,
			ArrayList<Promotion> proms) {
		ArrayList<Promotion> promosByMonth = new ArrayList<Promotion>();

		for (int i = 0; i < proms.size(); i++) {
			Promotion promotion = new Promotion();
			promotion = proms.get(i);

			if (promotion.getMonth().equalsIgnoreCase(month)) {
				promosByMonth.add(promotion);
			}

		}
		return promosByMonth;
	}

	public Promotion getPromotionSelected(String month, int position) {
		ArrayList<Promotion> promosByMonth = new ArrayList<Promotion>();
		Promotion promotionSelected = new Promotion();
		for (int i = 0; i < promotions.size(); i++) {
			Promotion promotion = new Promotion();
			promotion = promotions.get(i);
			if (promotion.getMonth().equalsIgnoreCase(month)) {
				promosByMonth.add(promotion);
			}

		}

		promotionSelected = promosByMonth.get(position);

		return promotionSelected;
	}

	public void onResume() {
		PromotionServices promotionServices = new PromotionServices();
		ArrayList<Promotion> promosByMonths = new ArrayList<Promotion>();
		promotions = promotionServices.getPromosByMonths();
		for (int i = 0; i < months.length; i++) {
			promosByMonths = getPromotionsByMonths(months[i], promotions);
			if (promosByMonths.size() > 0) {
				CalendarAdapter listCalendar = new CalendarAdapter(this,
						promosByMonths);
				adapter.addSection(months[i], listCalendar);
			}
		}

		calendarListView.setAdapter(adapter);

		adapter.notifyDataSetChanged();
		super.onResume();
	}

}
