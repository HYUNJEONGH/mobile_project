package mobile.example.ma02_20131145;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataManager {
	
	private Context context = null;
	private ScheduleInfoDBHelper helper = null;
	
	ArrayList<ScheduleInfoItem> infoList = null;
	
	public DataManager(Context context) {
		this.context = context;
		helper = new ScheduleInfoDBHelper(this.context);
	}
	
	//정보 저장
	public void saveScheduleInfo(ScheduleInfoItem item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("date", item.getDate());
		value.put("alarm", item.getAlarm());
		value.put("title", item.getTitle());
		value.put("place", item.getPlace());
		value.put("memo", item.getMemo());
		value.put("invite", item.getInvite());
		db.insert("info_table", null, value);
		helper.close();
	}
	
	//해당 일자의 데이터 가져오기
	public ArrayList<ScheduleInfoItem> searchSelecInfo(Date date){
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = null;
		String selection = "date=?";
		SimpleDateFormat format = CalendarHelper.yyyyMMddHHmmFormat;
		String s= format.format(date);
		String[] selectArgs = new String[]{s};
		Cursor cursor = db.query("info_table", columns, selection, selectArgs, null, null, null, null);
		if(cursor.getCount() != 0) {
			while(cursor.moveToNext()) {
				ScheduleInfoItem item = new ScheduleInfoItem();
				item.set_id(cursor.getInt(0));
				item.setDate(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setPlace(cursor.getString(3));
				item.setMemo(cursor.getString(4));
				item.setInvite(cursor.getString(5));
				item.setAlarm(cursor.getString(6));
				infoList.add(item);
			}
		}
		helper.close();
		cursor.close();
		return infoList;
	}
	
	public Cursor searchSelecInfo2(Date date){
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = null;
		String selection = "date Like ?";
		SimpleDateFormat format = CalendarHelper.yyyyMMddFormat;
		String s= format.format(date);
		Log.d("s", s);
		String[] selectArgs = new String[]{s+"%"};
		Cursor cursor = db.query("info_table", columns, selection, selectArgs, null, null, null, null);
		return cursor;
	}
	
	public Cursor searchOneInfo(long id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM info_table WHERE _id=" + id +";", null);
		
		return cursor;
	}
	
	public void deleteInfo(long id) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String whereClause = "_id=?";
		String[] whereArgs = new String[]{String.valueOf(id)};
		
		db.delete("info_table", whereClause, whereArgs);
	}
	
	public void infoUpdate(long id, ScheduleInfoItem item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put("date", item.getDate());
		row.put("title", item.getTitle());
		row.put("place", item.getPlace());
		row.put("memo", item.getMemo());
		row.put("invite", item.getInvite());
		row.put("alarm", item.getAlarm());
		
		String whereClause = "_id=?";
		String[] whereArgs = new String[]{String.valueOf(id)};
		
		db.update("info_table", row, whereClause, whereArgs);
	}
	
}
