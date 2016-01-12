/*
 * 알람설정을 선택하는 클래스이다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class AlarmActivity extends Activity {
	
	ArrayList<HashMap<String,String>> mapList;
	EditText etChoice;
	ArrayList<String> str;
	String s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_choice_view);
		//액션바 뒤로가기버튼
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	
		GridView gvAlarm = (GridView)findViewById(R.id.gvAlarm);
		etChoice = (EditText)findViewById(R.id.etChoiceAlarm);
		//문자 배열
		str = new ArrayList<String>();
		final ArrayList<Integer> selection = new ArrayList<Integer>();
		final String[] datas = {"정시", "5분전", "10분전", "15분전", "30분전", "1시간전", "2시간전", "3시간전", "12시간전", "1일전", "2일전", "1주전"};
		//그리드뷰 데이터 준비
		ArrayList<MyAlarmData> list = new ArrayList<MyAlarmData>();
		list.add(new MyAlarmData("정시" , ""));
		list.add(new MyAlarmData("5" , "분전"));
		list.add(new MyAlarmData("10" , "분전"));
		list.add(new MyAlarmData("15" , "분전"));
		list.add(new MyAlarmData("30" , "분전"));
		list.add(new MyAlarmData("1" , "시간전"));
		list.add(new MyAlarmData("2" , "시간전"));
		list.add(new MyAlarmData("3" , "시간전"));
		list.add(new MyAlarmData("12" , "시간전"));
		list.add(new MyAlarmData("1" , "일전"));
		list.add(new MyAlarmData("2" , "일전"));
		list.add(new MyAlarmData("1" , "주전"));
		
		//해쉬맵화
		mapList = new ArrayList<HashMap<String,String>>();
		
		for(MyAlarmData data : list) {
			HashMap<String, String> map = new HashMap<String, String>(2);
			map.put("time", data.getTime());
			map.put("op", data.getOp());
			mapList.add(map);
		}
		
		//어댑터생성
		SimpleAdapter adapter = new SimpleAdapter(this, mapList,
												android.R.layout.simple_list_item_2, 
												new String[]{"time", "op"},
												new int[]{android.R.id.text1, 
														  android.R.id.text2});
		
		gvAlarm.setAdapter(adapter);
		gvAlarm.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				/*
				 * 알람선택 여부 검사를위해 값을 selection에 저장
				 * 이미 선택한 설정을 다시 선택하면
				 * 선택위치값을 selection에서 remove하고 
				 * 배경색을 선택되지 않았을때의 색으로 바꿔준다.
				 * 그리고 수정된 선택값을 EditText에 업데이트
				 */
				for(int a : selection) {
					if(a == position){
						for(int i=0; i < str.size(); i++){
							if(str.get(i).equals(datas[position])) {
								if(selection.get(i) == position)
									selection.remove(i);
								parent.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
								str.remove(i);
								updateChoice();
								break;
							}
						}
						return;
					}
				}
				//선택한 위치값 저장
				selection.add(position);
				//minion yellow 245.220.80
				parent.getChildAt(position).setBackgroundColor(Color.rgb(245, 220, 80));
				//선택한 위치의 문자값 저장
				str.add(datas[position]);
				updateChoice();
			}
			
		});
	}
	
	//선택값을 EditText에 업데이트
	public void updateChoice(){
		s = "";
		for(int i=0; i < str.size(); i++){
			s += str.get(i) + ",";
		}
		etChoice.setText(s);
	}
	
	public void onPause(){
		super.onPause();
		//알람선택 저장
		SharedPreferences pref = getSharedPreferences("Pref", 0);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString("alarmOption", etChoice.getText().toString());
		edit.commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
