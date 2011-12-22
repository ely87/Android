package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {
	/** The parent context */
	private Context myContext;
	public ImageLoader imageLoader;

	/** URL-Strings to some remote images. */
	private String[] myRemoteImages = null;

	private String[] titles = null;

	/** Simple Constructor saving the 'parent' context. */
	public GalleryAdapter(Context c, String[] images, String[] title) {
		this.myContext = c;
		this.myRemoteImages = images;
		this.titles = title;
		imageLoader = new ImageLoader(c);
	}

	/** Returns the amount of images we have defined. */
	public int getCount() {
		return this.myRemoteImages.length;
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
		ImageView i = new ImageView(this.myContext);
		// FrameLayout bottomFrameLayout = new FrameLayout(myContext);
		// RelativeLayout layout = new RelativeLayout(myContext);
		FrameLayout layout = new FrameLayout(myContext);

		imageLoader.DisplayImage(myRemoteImages[position], i);
		/* Image should be scaled as width/height are set. */
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		// i.setAdjustViewBounds(true);
		i.setMinimumHeight(148);
		i.setMinimumWidth(280);

		TextView tvTitle = new TextView(myContext);
		tvTitle.setMinimumHeight(74);
		tvTitle.setMinimumWidth(271);
		tvTitle.setText(" "+ titles[position]);
		tvTitle.setLayoutParams(new FrameLayout.LayoutParams(271, 40,
				Gravity.BOTTOM));
		tvTitle.setBackgroundColor(Color.BLACK);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setTextSize(8);
		tvTitle.setGravity(Gravity.TOP);
		layout.addView(i);
		layout.addView(tvTitle);

		return layout;
		// return i;
	}

	/**
	 * Returns the size (0.0f to 1.0f) of the views depending on the 'offset' to
	 * the center.
	 */
	public float getScale(boolean focused, int offset) {
		/* Formula: 1 / (2 ^ offset) */
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
