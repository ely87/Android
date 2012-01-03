package adapters;

import java.util.ArrayList;

import persistence.PromotionHelper;

import com.publizar.publizaraldia.R;

import domain.Promotion;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavouriteAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private ArrayList<Promotion> promotions;
	private PromotionHelper promotionHelper;
	private ImageView imageView;

	public FavouriteAdapter(Activity a, ArrayList<Promotion> promotions) {
		this.promotions = promotions;
		activity = a;
		promotionHelper = new PromotionHelper(a);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return promotions.size();
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
			vi = inflater.inflate(R.layout.favourite_items, null);

		final Promotion promotion = promotions.get(position);

		TextView title = (TextView) vi.findViewById(R.id.promo_title1);
		TextView discount = (TextView) vi.findViewById(R.id.promo_discount1);
		TextView price = (TextView) vi.findViewById(R.id.promo_amount1);
		TextView date = (TextView) vi.findViewById(R.id.promo_due1);
		TextView date_text = (TextView) vi.findViewById(R.id.promo_due_text1);
		imageView = (ImageView) vi.findViewById(R.id.promo_image_favourites);
		title.setText(promotion.getTitle());

		if ((!promotion.getDiscount().equalsIgnoreCase(""))
				|| (!promotion.getSaved_price().equalsIgnoreCase(""))) {
			discount.setText(promotion.getDiscount());
			price.setText(promotion.getSaved_price());
		} else {
			discount.setText("-");
			price.setText("-");
		}
		if (promotion.getDue_date().equalsIgnoreCase("0")) {
			date_text.setText("Finaliza");
			date.setText("Hoy");
		} else if (Integer.valueOf(promotion.getDue_date()) > 15000) {
			date.setText("-");
		} else {
			date_text.setText("Finaliza en");
			date.setText(promotion.getDue_date() + " dias");
		}
		promotionHelper.open();
		byte[] imageByteArray = promotionHelper.setImageFavourites(String
				.valueOf(promotion.getId()));
		java.io.ByteArrayInputStream imageStream = new java.io.ByteArrayInputStream(
				imageByteArray);

		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setAdjustViewBounds(true);

		imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
		promotionHelper.close();
		return vi;
	}
}