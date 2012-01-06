package adapters;

import java.util.ArrayList;
import com.publizar.publizaraldia.R;
import domain.Category;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private ArrayList<Category> categories;
	private ImageView image;

	public CategoryAdapter(Activity a, ArrayList<Category> categories) {
		this.categories = categories;
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());

	}

	public int getCount() {
		return categories.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.categories_items, null);
			image = (ImageView) vi.findViewById(R.id.promo_image_categories);
		}
		final Category category = categories.get(position);

		TextView title = (TextView) vi.findViewById(R.id.promo_category);

		title.setText(category.getName());

		if (category.getImage() != null) {

			imageLoader.DisplayImage(category.getImage(), image);
		} else {
			ImageView image = (ImageView) vi
					.findViewById(R.id.promo_image_categories);
			image.setVisibility(View.GONE);
		}
		return vi;
	}
}