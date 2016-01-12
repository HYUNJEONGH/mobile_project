package mobile.example.ma02_20131145;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "일정을 확인하세요", Toast.LENGTH_LONG).show();
		Log.d("alarm receiver", "receive");
		
		Intent intent1 = new Intent(context, AlarmStartActivity.class);
		intent1.putExtra("date", intent.getStringExtra("date"));
		intent1.putExtra("title", intent.getStringExtra("title"));
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(intent1);
	}

	

}
