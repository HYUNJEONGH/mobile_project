/*
 * 해당 일정을 클릭할 경우 나오는 상세페이지로 
 * 수정, 삭제, sms발송화면을 띄어주는 역할을 담당하는 액티비티이다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import hirondelle.date4j.DateTime;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class SelectDetailInfoActivity extends Activity {
	
	int mYear, mMonth, mDay, mHour, mMinute;
	//날짜,시간
	DatePickerDialog dateDialog;
	TimePickerDialog timeDialog;
	DateTime setDatetime;
	EditText title;
	TextView date;
	EditText place;
	EditText memo;
	EditText invite;
	TextView alarm;
	long id;
	Intent intent;
	DataManager manager;
	boolean dateupdate;
	boolean timeupdate;
	boolean alarmupdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_info_update_view);
		//액션바 뒤로가기버튼
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Calendar c = Calendar.getInstance();
		manager = new DataManager(this);
		title = (EditText)findViewById(R.id.etInput);
		place = (EditText)findViewById(R.id.etMarker);
		memo = (EditText)findViewById(R.id.etMemo);
		invite = (EditText)findViewById(R.id.etInvite);
		date = (TextView)findViewById(R.id.tvsetdate);
		alarm = (TextView)findViewById(R.id.tvSetAlarm);
		
		//현재 날짜, 시간 설정
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR);
		mMinute = c.get(Calendar.MINUTE);
		
		//정보 가져와 표시
		intent = getIntent();
		id = intent.getLongExtra("_id", 0);
		Cursor cursor = manager.searchOneInfo(id);
		Log.e("cursor", cursor.getCount()+"");
		if(cursor.getCount() != 0) {
			cursor.moveToNext();
			date.setText(cursor.getString(1));
			title.setText(cursor.getString(2));
			place.setText(cursor.getString(3));
			memo.setText(cursor.getString(4));
			invite.setText(cursor.getString(5));
			alarm.setText(cursor.getString(6));
		}
		cursor.close();
		
		//디비에서 가져온 데이트값 임시저장 string
		String setDatetime1 = date.getText().toString();
		//DateTime형으로 변환
		setDatetime = CalendarHelper.getDateTimeFromString(setDatetime1, "yyyy-MM-dd HH:mm");
		Log.d("setdatetime", setDatetime.getHour()+""+setDatetime.getMinute());		
		//변경여부 검사값
		dateupdate = false;
		timeupdate = false;
		alarmupdate = false;
		//datepickerDialog 생성
		dateDialog = new DatePickerDialog(this, dateSetListener, mYear, mMonth, mDay);
		//timepickerDialog 생성
		timeDialog = new TimePickerDialog(this, timeSetListener, mHour, mMinute, false);	
 	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.imgbtnDelete:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("삭제")
				   .setMessage("삭제하시겠습니까?")
				   .setIcon(R.drawable.fulltrash50)
				   .setNegativeButton("취소", null)
				   .setPositiveButton("확인", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						manager.deleteInfo(id);
						finish();
					}
				})
					.show();
				break;
		case R.id.imgMessage:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			String msg = title.getText().toString() + ", " + memo.getText().toString() + ", " + date.getText().toString();
			intent.putExtra("sms_body", msg);
			intent.setType("vnd.android-dir/mms-sms");
			startActivity(intent);
			break;
		case R.id.btnDate:
			dateDialog.show();
			dateupdate = true;
			break;
		case R.id.btnTime:
			timeDialog.show();
			timeupdate = true;
			break;
		case R.id.btnMap:
			intent = new Intent(this, SearchLocationActivity.class);
			intent.putExtra("InputLoc", place.getText().toString());
			startActivity(intent);
			break;		
		case R.id.alarmlayout:
			alarmupdate = true;
			intent = new Intent(this, AlarmActivity.class);
			startActivity(intent);
			break;	
		}
	}
	
	public void onResume() {
		super.onResume();
		
		SharedPreferences pref = getSharedPreferences("Pref", 0);
		String setOp = pref.getString("alarmOption", "");
		if(alarmupdate)
			alarm.setText(setOp);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.update_info, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
//			시간은 원래 설정된 시간으로 냅두고 날짜만 바꾼다.
			if(!timeupdate){
				date.setText(String.format("%d년  %d월  %d일  %d시  %d분", mYear, mMonth+1, mDay, setDatetime.getHour(), setDatetime.getMinute()));
			}
			else
				updateDateDisplay();
		}		
	};
	
	TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
//			날짜는 원래 설정된 날짜로 냅두고 시간만 바꾼다.
			if(!dateupdate){
				date.setText(String.format("%d년  %d월  %d일  %d시  %d분", setDatetime.getYear(), setDatetime.getMonth(), setDatetime.getDay(), mHour, mMinute));
			}
			else 
				updateDateDisplay();
		}
	};
	
	//선택 날짜, 시간 화면에 업데이트
	private void updateDateDisplay() {
		date.setText(String.format("%d년  %d월  %d일  %d시  %d분", mYear, mMonth+1, mDay, mHour, mMinute));
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.update_info:
			ScheduleInfoItem infoitem = new ScheduleInfoItem();
			int mMonth1 = mMonth+1;
			String date1 = mYear+"-"+mMonth1+"-"+mDay+" "+mHour+":"+mMinute;
			//둘다 변경할 경우
			if(dateupdate && timeupdate) 
				infoitem.setDate(date1);
			else if(dateupdate) {
				date1 = mYear+"-"+mMonth1+"-"+mDay+" "+setDatetime.getHour()+":"+setDatetime.getMinute();
				infoitem.setDate(date1);
			}
			else if(timeupdate) {
				date1 = setDatetime.getYear()+"-"+setDatetime.getMonth()+"-"+setDatetime.getDay()+" "+mHour+":"+mMinute;
				infoitem.setDate(date1);
			}
			else	
				infoitem.setDate(date.getText().toString());
			infoitem.setAlarm(alarm.getText().toString());
			infoitem.setInvite(invite.getText().toString());
			infoitem.setMemo(memo.getText().toString());
			infoitem.setPlace(place.getText().toString());
			infoitem.setTitle(title.getText().toString());
			manager.infoUpdate(id, infoitem);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
