package adapters;

import com.publizar.publizaraldia.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class HorizontalListAdapter extends BaseAdapter {
	/** The parent context */
	private Context myContext;
	public ImageLoader imageLoader;
	private static LayoutInflater inflater = null;

	/** URL-Strings to some remote images. */
	private String[] myRemoteImages = null;

	private String[] titles = null;

	/** Simple Constructor saving the 'parent' context. */
	public HorizontalListAdapter(Context c, String[] urls, String[] titles) {
		this.myContext = c;
		this.myRemoteImages = urls;
		this.titles = titles;
		imageLoader = new ImageLoader(c);
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public HorizontalListAdapter() {

	}

	/** Returns the amount of images we have defined. */
	public int getCount() {
		return 1;
	}

	/* Use the array-Positions as unique IDs */
	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * Returns a new ImageView to be displayed, depending on the position
	 * passed.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.horizontal_list, null);

		}

		HorizontalListView listview = (HorizontalListView) vi
				.findViewById(R.id.listview);

		// Gallery gallery = new Gallery(myContext);
		GalleryAdapter ga = new GalleryAdapter(myContext, myRemoteImages,
				titles);
		// gallery.setAdapter(ga);

		listview.setLayoutParams(new ListView.LayoutParams(
				ListView.LayoutParams.FILL_PARENT, 140));
		listview.setAdapter(ga);
		return listview;

	}

	/**
	 * Returns the size (0.0f to 1.0f) of the views depending on the 'offset' to
	 * the center.
	 */
	public float getScale(boolean focused, int offset) {
		/* Formula: 1 / (2 ^ offset) */
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
