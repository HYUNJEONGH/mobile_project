/*
 * 메인 엑티비티로서 맨 처음 달력화면을 보여준다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class MainActivity extends FragmentActivity {
	CaldroidFragment caldroidFragment;
	ArrayList<ScheduleInfoItem> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// **** This is to show customized fragment. ****
		caldroidFragment = new MyCustomFragment();
		//달력 초기화
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		caldroidFragment.setArguments(args);
		android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
		Date date = new Date();
		
		caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, date);
		caldroidFragment.setTextColorForDate(R.color.white, date);
		
		CaldroidListener listener = new CaldroidListener(){

			@Override
			public void onSelectDate(Date date, View view) {
				Intent intent = new Intent(MainActivity.this, SelectInfoActivity.class);
				intent.putExtra("date", date);
				startActivity(intent);
			}
			
		};
		
		setData();
		caldroidFragment.setCaldroidListener(listener);
	
	}
	
	public void setData() {
		//DB에서 데이터를 가져와 extraData에 넣어 준다.
		ScheduleInfoDBHelper helper = new ScheduleInfoDBHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM info_table", null);
		list = new ArrayList<ScheduleInfoItem>();
		while(cursor.moveToNext()) {
			ScheduleInfoItem t = new ScheduleInfoItem();
			t.setDate(cursor.getString(1));
			t.setTitle(cursor.getString(2));
			t.setPlace(cursor.getString(3));
			t.setMemo(cursor.getString(4));
			t.setInvite(cursor.getString(5));
			t.setAlarm(cursor.getString(6));
			list.add(t);
		}
		//ArrayList<ScheduleInfoItem>를  extraData에 저장
		HashMap<String, Object> extraData = caldroidFragment.getExtraData();
		
		cursor.close();
		helper.close();
		extraData.put("info", list);
	}
	
	public void onResume() {
		// Refresh view
		super.onResume();
		setData();
		caldroidFragment.refreshView();
		Log.d("Resume", "dr");
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.schedule_info, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//일정 추가 액티비티로 이동
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.add_info:
			Intent intent = new Intent(this, ScheduleInfoActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
