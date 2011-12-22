package persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserHelper {

	private final Context context;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private String DATABASE_TABLE = "users";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_ROWID = "idUser";

	public UserHelper(Context ctx) {
		this.context = ctx;
	}

	public UserHelper open() throws SQLException {
		mDbHelper = new DatabaseHelper(context);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createUser(String email, String password) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("email", email);
		initialValues.put("password", password);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteUser(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllUser() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_EMAIL,
				KEY_PASSWORD }, null, null, null, null, null);
	}

	public Cursor fetchUser(int empId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_EMAIL,
				KEY_PASSWORD }, KEY_ROWID + "=" + empId, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public int getLastRowID() {
		int lastId = 0;
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_EMAIL, KEY_PASSWORD }, null, null, null, null, KEY_ROWID
				+ " DESC");
		if (c != null && c.moveToFirst()) {
			lastId = Integer.valueOf(c.getString(0)); // The 0 is the column
														// index, we only have 1
			// column, so the index is 0
		}
		return lastId;

	}

	public boolean updateUser(int empId, String empName, String empDesignation) {
		ContentValues args = new ContentValues();
		args.put(KEY_EMAIL, empName);
		args.put(KEY_PASSWORD, empDesignation);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + empId, null) > 0;
	}

}
