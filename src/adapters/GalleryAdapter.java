package adapters;

import android.content.Context;
import android.graphics.Color;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GalleryAdapter extends BaseAdapter {
	/** The parent context */
	private Context myContext;
	public ImageLoader imageLoader;

	/** URL-Strings to some remote images. */
	private String[] myRemoteImages = null;

	/** Simple Constructor saving the 'parent' context. */
	public GalleryAdapter(Context c, String[] images) {
		this.myContext = c;
		this.myRemoteImages = images;
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
		RelativeLayout layout = new RelativeLayout(myContext);

		imageLoader.DisplayImage(myRemoteImages[position], i);
		/* Image should be scaled as width/height are set. */
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		i.setAdjustViewBounds(true);
		i.setBackgroundColor(Color.RED);
		/* Set the Width/Height of the ImageView. */
		i.setLayoutParams(new Gallery.LayoutParams(271, 148));

		layout.addView(i);

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
