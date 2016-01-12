package mobile.example.ma02_20131145;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MySelectInfoAdapter extends CursorAdapter {
	LayoutInflater inflater; 
	int layout;
	
	public MySelectInfoAdapter(Context context, int layout, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tvTitle = (TextView)view.findViewById(R.id.tvlvTitle);
		TextView tvMemo = (TextView)view.findViewById(R.id.tvlvMemo);
		TextView tvDate= (TextView)view.findViewById(R.id.tvlvDate);

		tvTitle.setText(cursor.getString(2));
		tvMemo.setText(cursor.getString(4));
		tvDate.setText(cursor.getString(1));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(layout, parent, false);
	}

}
