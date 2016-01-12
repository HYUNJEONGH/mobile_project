package mobile.example.ma02_20131145;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleInfoDBHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME="sInfoDB";
	private final static String TABLE_NAME="info_table";
	
	public ScheduleInfoDBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " + TABLE_NAME + "(_id integer primary key autoincrement, date DATE, title TEXT,"
				+ " place TEXT, memo TEXT, invite TEXT, alarm TEXT)";
		db.execSQL(query);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
