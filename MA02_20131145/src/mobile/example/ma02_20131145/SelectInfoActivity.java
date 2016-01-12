/*
 * 일정 리스트를 보여주는 액티비티이다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SelectInfoActivity extends Activity {
	ListView lv;
	MySelectInfoAdapter adapter;
	DataManager manager;
	Date sdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_info);
		//액션바 뒤로가기버튼
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		lv = (ListView)findViewById(R.id.lvSelectInfo);
		adapter = new MySelectInfoAdapter(this, R.layout.listview_item, null);
		lv.setAdapter(adapter);
		//textview에 해당 날짜표시
		Intent intent = getIntent();
		sdate = (Date) intent.getSerializableExtra("date");
		TextView tvsdate = (TextView)findViewById(R.id.tvSdate);
		SimpleDateFormat f = CalendarHelper.yyyyMMddFormat;
		tvsdate.setText(f.format(sdate));
		//클릭시 상세정보 보여주기
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				Intent intent = new Intent(SelectInfoActivity.this, SelectDetailInfoActivity.class);
				intent.putExtra("_id", id);
				startActivity(intent);
			}
			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		manager = new DataManager(this);
		Cursor cursor = manager.searchSelecInfo2(sdate);
		if(cursor != null)
			adapter.changeCursor(cursor);
		else
			Log.e("cursor", "isnull");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		현재 어댑터에 설정한 cursor를 activity 종료 시 close
		if (!adapter.getCursor().isClosed()) adapter.getCursor().close(); 
	}

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
