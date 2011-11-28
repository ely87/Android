package adapters;

import java.util.ArrayList;

import com.publizar.publizaraldia.R;

import domain.Promotion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private Activity activity;
	private String[] data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private ArrayList<Promotion> promotions;

	public ImageAdapter(Activity a, String[] d, ArrayList<Promotion> promotions) {
		this.promotions = promotions;
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_items, null);

		final Promotion promotion = promotions.get(position);

		TextView title = (TextView) vi.findViewById(R.id.promo_title);
		TextView discount = (TextView) vi.findViewById(R.id.promo_discount);
		TextView price = (TextView) vi.findViewById(R.id.promo_amount);
		TextView date = (TextView) vi.findViewById(R.id.promo_due);
		TextView date_text = (TextView) vi.findViewById(R.id.promo_due_text);
		title.setText(promotion.getName());

		if ((!promotion.getDiscount().equalsIgnoreCase(""))
				|| (!promotion.getPromotional_price().equalsIgnoreCase(""))) {
			discount.setText(promotion.getDiscount());
			price.setText(promotion.getPromotional_price());
		} else {
			discount.setText("-");
			price.setText("-");
		}
		if (promotion.getDue_date().equalsIgnoreCase("0")) {
			date_text.setText("Finaliza");
			date.setText("Hoy");
		} else if (promotion.getDue_date().equalsIgnoreCase("15305")) {
			date.setText("-");
		} else {
			date_text.setText("Finaliza en");
			date.setText(promotion.getDue_date() + " dias");
		}
		ImageView image = (ImageView) vi.findViewById(R.id.promo_image);
		imageLoader.DisplayImage(data[position], image);
		return vi;
	}
}