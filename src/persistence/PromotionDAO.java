package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import domain.Promotion;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class PromotionDAO {

	private static SQLiteDatabase db;

	public void initialize(Context context) {
		//context.deleteDatabase("publizar_db.db");
		db = context.openOrCreateDatabase("publizar_db.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable(db, "promotions");
	}

	private void createTable(SQLiteDatabase database, String tableName) {
		try {
			// begin the transaction
			database.beginTransaction();

			// Create a table
			String tableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
					+ " idPromotion INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " description TEXT NOT NULL," 
					+ " excerpt TEXT NOT NULL,"
					+ " title TEXT NOT NULL,"
					+ " id_comerce TEXT DEFAULT NULL,"
					+ " image_url TEXT NOT NULL,"
					+ " discount TEXT DEFAULT NULL,"
					+ " saved_price TEXT DEFAULT NULL,"
					+ " original_price TEXT DEFAULT NULL,"
					+ " promo_company TEXT DEFAULT NULL,"
					+ " due_date TEXT NOT NULL,"
					+ " promo_complete_url TEXT NOT NULL,"
					+ " foursquare TEXT DEFAULT NULL,"
					+ " comerce TEXT DEFAULT NULL,"
					+ " comerce_tlf TEXT DEFAULT NULL,"
					+ " website_comerce TEXT DEFAULT NULL,"
					+ " twitter TEXT DEFAULT NULL,"
					+ " facebook TEXT DEFAULT NULL,"
					+ " contact_email TEXT DEFAULT NULL,"
					+ " form_link TEXT DEFAULT NULL,"
					+ " status TEXT NOT NULL);";
			database.execSQL(tableSql);

			// this makes sure transaction is committed
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public boolean isTableEmpty(String table) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT count(*) FROM " + table, null);

			int countIndex = cursor.getColumnIndex("count(*)");
			cursor.moveToFirst();
			int rowCount = cursor.getInt(countIndex);
			if (rowCount > 0) {
				return false;
			}

			return true;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public void insert(Promotion promotion) {
		try {
			db.beginTransaction();

			// insert this row
			String description = promotion.getDescription();
			String excerpt = promotion.getExcerpt();
			String title = promotion.getTitle();
			String id_comerce = promotion.getId_comerce();
			String image_url = promotion.getImage_url();
			String discount = promotion.getDiscount();
			String saved_price = promotion.getSaved_price();
			String original_price = promotion.getOriginal_price();
			String promo_company = promotion.getPromo_company();
			String due_date = promotion.getDue_date();
			String promo_complete_url = promotion.getPromo_complete_url();
			String foursquare = promotion.getFoursquare();
			String comerce = promotion.getComerce();
			String comerce_tlf = promotion.getComerce_tlf();
			String website_comerce = promotion.getWebsite_comerce();
			String twitter = promotion.getTwitter();
			String facebook = promotion.getFacebook();
			String contact_email = promotion.getContact_email();
			String form_link = promotion.getForm_link();
			String status = promotion.getStatus();

			String insert = "INSERT INTO promotions "
					+ " (description,excerpt,title,id_comerce,image_url,discount,saved_price,"
					+ "original_price,promo_company,due_date,promo_complete_url,foursquare,comerce,comerce_tlf,"
					+ "website_comerce,twitter,facebook,contact_email,form_link,status) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			db.execSQL(insert, new Object[] { description, excerpt, title,
					id_comerce, image_url, discount, saved_price,
					original_price, promo_company, due_date,
					promo_complete_url, foursquare, comerce, comerce_tlf,
					website_comerce, twitter, facebook, contact_email,
					form_link, status });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public List<Promotion> readAll() {
		Cursor cursor = null;
		try {
			List<Promotion> all = new ArrayList<Promotion>();

			cursor = db.rawQuery("SELECT * FROM promotions", null);

			if (cursor.getCount() > 0) {
				int idPromotionIndex = cursor.getColumnIndex("idPromotion");
				int descriptionIndex = cursor.getColumnIndex("description");
				int excerptIndex = cursor.getColumnIndex("excerpt");
				int titleIndex = cursor.getColumnIndex("title");
				int idComerceIndex = cursor.getColumnIndex("id_comerce");
				int imageUrlIndex = cursor.getColumnIndex("image_url");
				int discountIndex = cursor.getColumnIndex("discount");
				int savedPriceIndex = cursor.getColumnIndex("saved_price");
				int originalPriceIndex = cursor
						.getColumnIndex("original_price");
				int promoCompanyIndex = cursor.getColumnIndex("promo_company");
				int dueDateIndex = cursor.getColumnIndex("due_date");
				int promoCompleteUrlIndex = cursor
						.getColumnIndex("promo_complete_url");
				int foursquareIndex = cursor.getColumnIndex("foursquare");
				int comerceIndex = cursor.getColumnIndex("comerce");
				int comerceTlfIndex = cursor.getColumnIndex("comerce_tlf");
				int websiteComerceIndex = cursor
						.getColumnIndex("website_comerce");
				int twitterIndex = cursor.getColumnIndex("twitter");
				int facebookIndex = cursor.getColumnIndex("facebook");
				int contactEmailIndex = cursor.getColumnIndex("contact_email");
				int formLinkIndex = cursor.getColumnIndex("form_link");
				int statusIndex = cursor.getColumnIndex("status");
				cursor.moveToFirst();
				do {
					int idPromotion = cursor.getInt(idPromotionIndex);
					String description = cursor.getString(descriptionIndex);
					String excerpt = cursor.getString(excerptIndex);
					String title = cursor.getString(titleIndex);
					String id_comerce = cursor.getString(idComerceIndex);
					String image_url = cursor.getString(imageUrlIndex);
					String discount = cursor.getString(discountIndex);
					String saved_price = cursor.getString(savedPriceIndex);
					String original_price = cursor
							.getString(originalPriceIndex);
					String promo_company = cursor.getString(promoCompanyIndex);
					String due_date = cursor.getString(dueDateIndex);
					String promo_complete_url = cursor
							.getString(promoCompleteUrlIndex);
					String foursquare = cursor.getString(foursquareIndex);
					String comerce = cursor.getString(comerceIndex);
					String comerce_tlf = cursor.getString(comerceTlfIndex);
					String website_comerce = cursor
							.getString(websiteComerceIndex);
					String twitter = cursor.getString(twitterIndex);
					String facebook = cursor.getString(facebookIndex);
					String contact_email = cursor.getString(contactEmailIndex);
					String form_link = cursor.getString(formLinkIndex);
					String status = cursor.getString(statusIndex);

					Promotion promotion = new Promotion();
					promotion.setId(idPromotion);
					promotion.setDescription(description);
					promotion.setExcerpt(excerpt);
					promotion.setTitle(title);
					promotion.setId_comerce(id_comerce);
					promotion.setImage_url(image_url);
					promotion.setDiscount(discount);
					promotion.setSaved_price(saved_price);
					promotion.setOriginal_price(original_price);
					promotion.setPromo_company(promo_company);
					promotion.setDue_date(due_date);
					promotion.setPromo_complete_url(promo_complete_url);
					promotion.setFoursquare(foursquare);
					promotion.setComerce(comerce);
					promotion.setComerce_tlf(comerce_tlf);
					promotion.setWebsite_comerce(website_comerce);
					promotion.setTwitter(twitter);
					promotion.setFacebook(facebook);
					promotion.setContact_email(contact_email);
					promotion.setForm_link(form_link);
					promotion.setStatus(status);
					all.add(promotion);

					cursor.moveToNext();
				} while (!cursor.isAfterLast());
			}

			return all;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public Promotion selectPromotion(String id) {
		Promotion promotion = new Promotion();
		Cursor cursor = null;
		cursor = db.rawQuery("SELECT * FROM promotions WHERE id= ?",
				new String[] { id });
		try {
			if (cursor.getCount() > 0) {
				int idPromotionIndex = cursor.getColumnIndex("idPromotion");
				int descriptionIndex = cursor.getColumnIndex("description");
				int excerptIndex = cursor.getColumnIndex("excerpt");
				int titleIndex = cursor.getColumnIndex("title");
				int idComerceIndex = cursor.getColumnIndex("id_comerce");
				int imageUrlIndex = cursor.getColumnIndex("image_url");
				int discountIndex = cursor.getColumnIndex("discount");
				int savedPriceIndex = cursor.getColumnIndex("saved_price");
				int originalPriceIndex = cursor
						.getColumnIndex("original_price");
				int promoCompanyIndex = cursor.getColumnIndex("promo_company");
				int dueDateIndex = cursor.getColumnIndex("due_date");
				int promoCompleteUrlIndex = cursor
						.getColumnIndex("promo_complete_url");
				int foursquareIndex = cursor.getColumnIndex("foursquare");
				int comerceIndex = cursor.getColumnIndex("comerce");
				int comerceTlfIndex = cursor.getColumnIndex("comerce_tlf");
				int websiteComerceIndex = cursor
						.getColumnIndex("website_comerce");
				int twitterIndex = cursor.getColumnIndex("twitter");
				int facebookIndex = cursor.getColumnIndex("facebook");
				int contactEmailIndex = cursor.getColumnIndex("contact_email");
				int formLinkIndex = cursor.getColumnIndex("form_link");
				int statusIndex = cursor.getColumnIndex("status");
				cursor.moveToFirst();
				do {
					int idPromotion = cursor.getInt(idPromotionIndex);
					String description = cursor.getString(descriptionIndex);
					String excerpt = cursor.getString(excerptIndex);
					String title = cursor.getString(titleIndex);
					String id_comerce = cursor.getString(idComerceIndex);
					String image_url = cursor.getString(imageUrlIndex);
					String discount = cursor.getString(discountIndex);
					String saved_price = cursor.getString(savedPriceIndex);
					String original_price = cursor
							.getString(originalPriceIndex);
					String promo_company = cursor.getString(promoCompanyIndex);
					String due_date = cursor.getString(dueDateIndex);
					String promo_complete_url = cursor
							.getString(promoCompleteUrlIndex);
					String foursquare = cursor.getString(foursquareIndex);
					String comerce = cursor.getString(comerceIndex);
					String comerce_tlf = cursor.getString(comerceTlfIndex);
					String website_comerce = cursor
							.getString(websiteComerceIndex);
					String twitter = cursor.getString(twitterIndex);
					String facebook = cursor.getString(facebookIndex);
					String contact_email = cursor.getString(contactEmailIndex);
					String form_link = cursor.getString(formLinkIndex);
					String status = cursor.getString(statusIndex);

					promotion.setId(idPromotion);
					promotion.setDescription(description);
					promotion.setExcerpt(excerpt);
					promotion.setTitle(title);
					promotion.setId_comerce(id_comerce);
					promotion.setImage_url(image_url);
					promotion.setDiscount(discount);
					promotion.setSaved_price(saved_price);
					promotion.setOriginal_price(original_price);
					promotion.setPromo_company(promo_company);
					promotion.setDue_date(due_date);
					promotion.setPromo_complete_url(promo_complete_url);
					promotion.setFoursquare(foursquare);
					promotion.setComerce(comerce);
					promotion.setComerce_tlf(comerce_tlf);
					promotion.setWebsite_comerce(website_comerce);
					promotion.setTwitter(twitter);
					promotion.setFacebook(facebook);
					promotion.setContact_email(contact_email);
					promotion.setForm_link(form_link);
					promotion.setStatus(status);
					cursor.moveToNext();
				} while (!cursor.isAfterLast());
			}
			return promotion;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public void delete(Promotion promotion) {
		try {
			db.beginTransaction();

			// delete this record
			String delete = "DELETE FROM promotion WHERE idPromotion='"
					+ promotion.getId() + "'";
			db.execSQL(delete);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public boolean exportDatabase() {
		File dbFile = new File(Environment.getDataDirectory()
				+ "/data/com.publizar.publizaraldia/databases/publizar_db.db");

		File exportDir = new File(Environment.getExternalStorageDirectory(), "");
		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}
		File file = new File(exportDir, dbFile.getName());

		try {
			file.createNewFile();
			this.copyFile(dbFile, file);
			return true;
		} catch (IOException e) {
			Log.e("mypck", e.getMessage(), e);
			return false;
		}
	}

	public void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}
}
