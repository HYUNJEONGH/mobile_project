/*
 * 알람을 수신시 실행되는 액티비티이다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AlarmStartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_start);

		Intent intent = getIntent();
		TextView tv1 = (TextView)findViewById(R.id.tvStarttitle);
		TextView tv2 = (TextView)findViewById(R.id.tvStartdate);

		tv1.setText(intent.getStringExtra("title"));
		tv2.setText(intent.getStringExtra("date"));
		
	}
	
	//메인으로 돌아가기
	public void onClick(View v){
		switch(v.getId()) {
		case R.id.imgHome:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
	}
	
}
