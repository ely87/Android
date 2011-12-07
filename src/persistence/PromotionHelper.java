package persistence;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PromotionHelper {

	private final Context context;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private String DATABASE_TABLE = "promotions";
	public static final String KEY_ROWID = "idPromotion";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_EXCERPT = "excerpt";
	public static final String KEY_TITLE = "title";
	public static final String KEY_IDCOMERCE = "id_comerce";
	public static final String KEY_IMAGEURL = "image_url";
	public static final String KEY_DISCOUNT = "discount";
	public static final String KEY_SAVEDPRICE = "saved_price";
	public static final String KEY_ORIGINALPRICE = "original_price";
	public static final String KEY_PROMOCOMPANY = "promo_company";
	public static final String KEY_DUEDATE = "due_date";
	public static final String KEY_PROMOCOMPLETEURL = "promo_complete_url";
	public static final String KEY_FOURSQUARE = "foursquare";
	public static final String KEY_COMERCE = "comerce";
	public static final String KEY_COMERCETLF = "comerce_tlf";
	public static final String KEY_WEBSITECOMERCE = "website_comerce";
	public static final String KEY_TWITTER = "twitter";
	public static final String KEY_FACEBOOK = "facebook";
	public static final String KEY_CONTACTEMAIL = "contact_email";
	public static final String KEY_FORMLINK = "form_link";
	public static final String KEY_STATUS = "status";

	public PromotionHelper(Context ctx) {
		this.context = ctx;
	}

	public PromotionHelper open() throws SQLException {
		mDbHelper = new DatabaseHelper(context);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createPromotion(String id, String description, String excerpt,
			String title, String id_comerce, String image_url, String discount,
			String saved_price, String original_price, String promo_company,
			String due_date, String promo_complete_url, String foursquare,
			String comerce, String comerce_tlf, String website_comerce,
			String twitter, String facebook, String contact_email,
			String form_link, String status) {
		ContentValues initialValues = new ContentValues();
		ByteArrayBuffer baf = new ByteArrayBuffer(128);
		baf = transformToByteArray(image_url);
		initialValues.put(KEY_ROWID, id);
		initialValues.put(KEY_DESCRIPTION, description);
		initialValues.put(KEY_EXCERPT, excerpt);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_IDCOMERCE, id_comerce);
		initialValues.put(KEY_IMAGEURL, baf.toByteArray());
		initialValues.put(KEY_DISCOUNT, discount);
		initialValues.put(KEY_SAVEDPRICE, saved_price);
		initialValues.put(KEY_ORIGINALPRICE, original_price);
		initialValues.put(KEY_PROMOCOMPANY, promo_company);
		initialValues.put(KEY_DUEDATE, due_date);
		initialValues.put(KEY_PROMOCOMPLETEURL, promo_complete_url);
		initialValues.put(KEY_FOURSQUARE, foursquare);
		initialValues.put(KEY_COMERCE, comerce);
		initialValues.put(KEY_COMERCETLF, comerce_tlf);
		initialValues.put(KEY_WEBSITECOMERCE, website_comerce);
		initialValues.put(KEY_TWITTER, twitter);
		initialValues.put(KEY_FACEBOOK, facebook);
		initialValues.put(KEY_CONTACTEMAIL, contact_email);
		initialValues.put(KEY_FORMLINK, form_link);
		initialValues.put(KEY_STATUS, status);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deletePromotion(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllPromotions() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DESCRIPTION, KEY_EXCERPT, KEY_TITLE, KEY_IDCOMERCE,
				KEY_IMAGEURL, KEY_DISCOUNT, KEY_SAVEDPRICE, KEY_ORIGINALPRICE,
				KEY_PROMOCOMPANY, KEY_DUEDATE, KEY_PROMOCOMPLETEURL,
				KEY_FOURSQUARE, KEY_COMERCE, KEY_COMERCETLF,
				KEY_WEBSITECOMERCE, KEY_TWITTER, KEY_FACEBOOK,
				KEY_CONTACTEMAIL, KEY_FORMLINK, KEY_STATUS }, null, null, null,
				null, null);
	}

	public Cursor fetchPromotions(long empId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DESCRIPTION, KEY_EXCERPT, KEY_TITLE, KEY_IDCOMERCE,
				KEY_IMAGEURL, KEY_DISCOUNT, KEY_SAVEDPRICE, KEY_ORIGINALPRICE,
				KEY_PROMOCOMPANY, KEY_DUEDATE, KEY_PROMOCOMPLETEURL,
				KEY_FOURSQUARE, KEY_COMERCE, KEY_COMERCETLF,
				KEY_WEBSITECOMERCE, KEY_TWITTER, KEY_FACEBOOK,
				KEY_CONTACTEMAIL, KEY_FORMLINK, KEY_STATUS }, KEY_ROWID + "="
				+ empId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updatePromotion(int empId, String description,
			String excerpt, String title, String id_comerce, String image_url,
			String discount, String saved_price, String original_price,
			String promo_company, String due_date, String promo_complete_url,
			String foursquare, String comerce, String comerce_tlf,
			String website_comerce, String twitter, String facebook,
			String contact_email, String form_link, String status) {
		ContentValues args = new ContentValues();

		args.put(KEY_DESCRIPTION, description);
		args.put(KEY_EXCERPT, excerpt);
		args.put(KEY_TITLE, title);
		args.put(KEY_IDCOMERCE, id_comerce);
		args.put(KEY_IMAGEURL, image_url);
		args.put(KEY_DISCOUNT, discount);
		args.put(KEY_SAVEDPRICE, saved_price);
		args.put(KEY_ORIGINALPRICE, original_price);
		args.put(KEY_PROMOCOMPANY, promo_company);
		args.put(KEY_DUEDATE, due_date);
		args.put(KEY_PROMOCOMPLETEURL, promo_complete_url);
		args.put(KEY_FOURSQUARE, foursquare);
		args.put(KEY_COMERCE, comerce);
		args.put(KEY_COMERCETLF, comerce_tlf);
		args.put(KEY_WEBSITECOMERCE, website_comerce);
		args.put(KEY_TWITTER, twitter);
		args.put(KEY_FACEBOOK, description);
		args.put(KEY_CONTACTEMAIL, contact_email);
		args.put(KEY_FORMLINK, form_link);
		args.put(KEY_STATUS, status);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + empId, null) > 0;
	}

	public ByteArrayBuffer transformToByteArray(String urlImage) {

		ByteArrayBuffer baf = new ByteArrayBuffer(128);
		URL url;
		try {
			url = new URL(urlImage);
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 128);

			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baf;
	}

	public byte[] setImageFavourites(String imageId) {
		// select the data
		Cursor cursor = mDb.query(true, DATABASE_TABLE,
				new String[] { KEY_IMAGEURL }, KEY_ROWID + "=" + imageId, null,
				null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		// get it as a ByteArray
		byte[] imageByteArray = cursor.getBlob(0);
		// the cursor is not needed anymore
		cursor.close();

		return imageByteArray;

	}
}
