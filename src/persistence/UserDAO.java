package persistence;

import java.util.ArrayList;
import java.util.List;

import Domain.User;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {

	private static SQLiteDatabase db;

	public void initialize(Context context) {
		db = context.openOrCreateDatabase("publizar_db.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable(db, "users");
	}

	private void createTable(SQLiteDatabase database, String tableName) {
		try {
			// begin the transaction
			database.beginTransaction();

			// Create a table
			String tableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
					+ "idUser INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " email TEXT NOT NULL UNIQUE,"
					+ " password TEXT NOT NULL);";
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

	public void insert(User user) {
		try {
			db.beginTransaction();

			// insert this row
			String email = user.getEmail();
			String password = user.getPassword();
			String insert = "INSERT INTO users "
					+ " (email,password) VALUES (?,?);";
			db.execSQL(insert, new Object[] { email, password });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public List<User> readAll() {
		Cursor cursor = null;
		try {
			List<User> all = new ArrayList<User>();

			cursor = db.rawQuery("SELECT * FROM tickets", null);

			if (cursor.getCount() > 0) {
				int idIndex = cursor.getColumnIndex("id");
				int emailIndex = cursor.getColumnIndex("email");
				int passwordIndex = cursor.getColumnIndex("password");
				cursor.moveToFirst();
				do {
					int id = cursor.getInt(idIndex);
					String email = cursor.getString(emailIndex);
					String password = cursor.getString(passwordIndex);

					User user = new User();
					user.setIdUser(id);
					user.setEmail(email);
					user.setPassword(password);
					all.add(user);

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

	public User selectUser(String email) {
		User user = new User();
		Cursor cursor = null;
		cursor = db.rawQuery(
				"SELECT idUser, email,password FROM users WHERE email = ?", new String[]{email});
		try {
			if (cursor.getCount() > 0) {
				int idIndex = cursor.getColumnIndex("idUser");
				int emailIndex = cursor.getColumnIndex("email");
				int passwordIndex = cursor.getColumnIndex("password");
				cursor.moveToFirst();
				do {
					int id = cursor.getInt(idIndex);
					String email1 = cursor.getString(emailIndex);
					String password = cursor.getString(passwordIndex);
					user.setIdUser(id);
					user.setEmail(email1);
					user.setPassword(password);
					cursor.moveToNext();
				} while (!cursor.isAfterLast());
			}
			return user;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public void delete(User user) {
		try {
			db.beginTransaction();

			// delete this record
			String delete = "DELETE FROM users WHERE idUser='" + user.getIdUser()
					+ "'";
			db.execSQL(delete);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

}
