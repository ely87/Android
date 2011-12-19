package adapters;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {
	/** The parent context */
	private Context myContext;

	/** URL-Strings to some remote images. */
	private String[] myRemoteImages = null;

	/** Simple Constructor saving the 'parent' context. */
	public GalleryAdapter(Context c, String[] images) {
		this.myContext = c;
		this.myRemoteImages = images;
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
		LinearLayout layout = new LinearLayout(myContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		try {
			/* Open a new URL and get the InputStream to load data from it. */
			URL aURL = new URL(myRemoteImages[position]);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			/* Buffered is always good for a performance plus. */
			BufferedInputStream bis = new BufferedInputStream(is);
			/* Decode url-data to a bitmap. */
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			/* Apply the Bitmap to the ImageView that will be returned. */
			i.setImageBitmap(bm);
		} catch (IOException e) {
			// i.setImageResource(R.drawabl);
		}

		TextView tv = new TextView(myContext);

		String titolo = "Titulo";
		tv.setText(titolo);
		tv.setGravity(Gravity.CENTER);

		/* Image should be scaled as width/height are set. */
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		/* Set the Width/Height of the ImageView. */
		i.setLayoutParams(new Gallery.LayoutParams(190, 190));

		layout.addView(i);
		layout.addView(tv);

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
