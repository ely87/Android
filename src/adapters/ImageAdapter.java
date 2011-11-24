package adapters;

import java.util.ArrayList;

import com.publizar.publizaraldia.R;

import domain.Promotion;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<Promotion> {
	private ArrayList<Promotion> promotions;
	private Activity activity;
	public ImageManager imageManager;

	public ImageAdapter(Activity a, int textViewResourceId, ArrayList<Promotion> promotions) {
		super(a, textViewResourceId, promotions);
		this.promotions = promotions;
		activity = a;

		imageManager = 
			new ImageManager(activity.getApplicationContext());
	}

	public static class ViewHolder{
		public TextView title;
		public TextView excerpt;
		public ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {		
			LayoutInflater vi = 
				(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_items, null);
			holder = new ViewHolder();
			holder.title = (TextView) v.findViewById(R.id.promo_title);
			holder.excerpt = (TextView) v.findViewById(R.id.promo_excerpt);
			holder.image = (ImageView) v.findViewById(R.id.promo_image);
			v.setTag(holder);
		}
		else
			holder=(ViewHolder)v.getTag();

		final Promotion promotion = promotions.get(position);
		if (promotion != null) {
			holder.title.setText(promotion.getName());
			holder.excerpt.setText(promotion.getExcerpt());
			Object tag = promotion.getImageUrl();
			holder.image.setTag(tag);
			imageManager.displayImage(promotion.getImageUrl(), activity, holder.image);
		}
		return v;
	}

}
