/*
 * 일정을 저장하는 클래스이며 알람도 설정된다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import java.util.Calendar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleInfoActivity extends Activity {
	
	int mYear, mMonth, mDay, mampm, mHour, mMinute;
	//날짜,시간
	DatePickerDialog dateDialog;
	TimePickerDialog timeDialog;
	TextView tvtimedate;
	TextView tvAlarm;
	EditText etMarker;
	EditText etMemo;
	EditText etInvite;
	EditText etInput;
	Intent intent;
	ScheduleInfoDBHelper dbhelper;
	DataManager manager;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_info_view);
		Calendar c = Calendar.getInstance();
		//db helper 객체 생성
		dbhelper = new ScheduleInfoDBHelper(this);
		manager = new DataManager(this);
		//액션바 뒤로가기버튼
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		//현재 날짜, 시간 설정
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		
		tvtimedate = (TextView)findViewById(R.id.tvsetdate);
		tvAlarm = (TextView)findViewById(R.id.tvSetAlarm);
		etMarker = (EditText)findViewById(R.id.etMarker);
		etMemo = (EditText)findViewById(R.id.etMemo);
		etInvite = (EditText)findViewById(R.id.etInvite);
		etInput = (EditText)findViewById(R.id.etInput);
		//datepickerDialog 생성
		dateDialog = new DatePickerDialog(this, dateSetListener, mYear, mMonth, mDay);
		//timepickerDialog 생성
		timeDialog = new TimePickerDialog(this, timeSetListener, mHour, mMinute, false);
		
		updateDateDisplay();
	}
	
	public void onResume(){
		super.onResume();
		SharedPreferences pref = getSharedPreferences("Pref", 0);
		String setOp = pref.getString("alarmOption", "");
		tvAlarm.setText(setOp);
	}
	
	private void updateDateDisplay(){
		tvtimedate.setText(String.format("%d년  %d월  %d일  %d시  %d분", mYear, mMonth+1, mDay, mHour, mMinute));
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnDate:
			dateDialog.show();
			break;
		case R.id.btnTime:
			timeDialog.show();
			break;
		case R.id.btnMap:
			intent = new Intent(this, SearchLocationActivity.class);
			intent.putExtra("InputLoc", etMarker.getText().toString());
			startActivity(intent);
			break;
		case R.id.alarmlayout:
			intent = new Intent(this, AlarmActivity.class);
			startActivity(intent);
			break;
		case R.id.imgbtnSave:
			ScheduleInfoItem item = new ScheduleInfoItem();
			int mMonth1 = mMonth+1;
			String date = mYear+"-"+mMonth1+"-"+mDay+" "+mHour+":"+mMinute;
			String alarmset = tvAlarm.getText().toString();
			item.setDate(date);
			item.setAlarm(alarmset);
			item.setInvite(etInvite.getText().toString());
			item.setMemo(etMemo.getText().toString());
			item.setPlace(etMarker.getText().toString());
			item.setTitle(etInput.getText().toString());

			Intent intent;
			intent = new Intent(this, AlarmReceiver.class);
			intent.putExtra("date", date);
			intent.putExtra("alarm", alarmset);
			intent.putExtra("title", etInput.getText().toString());
			
			//현재시간
			Calendar current = Calendar.getInstance();
			//목적시간
			Calendar cal = Calendar.getInstance();
			cal.set(mYear, mMonth, mDay, mHour, mMinute, 0);
			//알림 설정 검사
			if(cal.compareTo(current) <= 0 && alarmset.length() != 0) {
				//already passed time/date
				Toast.makeText(this, "유효하지 않은 알람시간 입니다", Toast.LENGTH_SHORT).show();
				break;
			} else if(alarmset.length() != 0){
				Log.d("알람 설정", "ok");
				setAlarm(intent, cal);
			}
			manager.saveScheduleInfo(item);
			Toast.makeText(this, "일정 저장", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	public void setAlarm(Intent intent, Calendar targetCal) {
		PendingIntent sender;
		//알람 매니저
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		String op =intent.getStringExtra("alarm");
		//알람 설정 문자열 자르기
		String oplist[] = op.split(",");
		//시간 설정
		for(int i=0; i < oplist.length; i++) {
			if(oplist[i].equals("정시")) {
				//id identifier
				final int _id = (int)targetCal.getTimeInMillis();
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), sender);
			}
			else if(oplist[i].equals("5분전")) {
				long minute = 5 *60000;
				long target = targetCal.getTimeInMillis()-minute;	
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("10분전")) {
				long minute = 10 *60000;
				long target = targetCal.getTimeInMillis()-minute;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("15분전")) {
				long minute = 15 *60000;
				long target = targetCal.getTimeInMillis()-minute;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("30분전")) {
				long minute = 30 *60000;
				long target = targetCal.getTimeInMillis()-minute;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("1시간전")) {
				long hour = 3600000;
				long target = targetCal.getTimeInMillis()-hour;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("2시간전")) {
				long hour = 2 *3600000;
				long target = targetCal.getTimeInMillis()-hour;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("3시간전")) {
				long hour = 3 *3600000;
				long target = targetCal.getTimeInMillis()-hour;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("12시간전")) {
				long hour = 12 *3600000;
				long target = targetCal.getTimeInMillis()-hour;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("1일전")) {
				long day = 86400000;
				long target = targetCal.getTimeInMillis()-day;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("2일전")) {
				long day = 2 *86400000;
				long target = targetCal.getTimeInMillis()-day;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			else if(oplist[i].equals("1주전")) {
				long week = 604800000;
				long target = targetCal.getTimeInMillis()-week;
				//id identifier
				final int _id = (int)target;
				sender = PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				am.set(AlarmManager.RTC_WAKEUP, target, sender);
			}
			Log.e("split", oplist[i]);
		}
	}
	
	DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDateDisplay();
		}
	};
	
	TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDateDisplay();
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}

