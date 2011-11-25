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
		TextView excerpt = (TextView) vi.findViewById(R.id.promo_excerpt);

		title.setText(promotion.getName());
		excerpt.setText(promotion.getExcerpt());
		ImageView image = (ImageView) vi.findViewById(R.id.promo_image);
		imageLoader.DisplayImage(data[position], image);
		return vi;
	}
}