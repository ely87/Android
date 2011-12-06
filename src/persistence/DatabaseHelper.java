package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "publizar_db.db";

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createUser = "CREATE TABLE IF NOT EXISTS users ("
				+ " idUser INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " email TEXT NOT NULL UNIQUE," + " password TEXT NOT NULL)";

		String createPromotion = "CREATE TABLE IF NOT EXISTS promotions ("
				+ " idPromotion INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " description TEXT NOT NULL," + " excerpt TEXT NOT NULL,"
				+ " title TEXT NOT NULL," + " id_comerce TEXT DEFAULT NULL,"
				+ " image_url TEXT NOT NULL," + " discount TEXT DEFAULT NULL,"
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
				+ " form_link TEXT DEFAULT NULL," + " status TEXT NOT NULL)";
		db.execSQL(createUser);
		db.execSQL(createPromotion);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS promotions");
		onCreate(db);
	}

	public boolean exportDatabase() {
		File dbFile = new File(Environment.getDataDirectory()
				+ "/data/com.publizar.publizaraldia/databases/publizar_db2.db");

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
