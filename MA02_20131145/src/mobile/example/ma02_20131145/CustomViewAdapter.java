
package mobile.example.ma02_20131145;

import hirondelle.date4j.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomViewAdapter extends CaldroidGridAdapter {

	public CustomViewAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData) {
		super(context, month, year, caldroidData, extraData);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tvDate = (TextView) cellView.findViewById(R.id.tvDate);
		TextView tvDetail = (TextView) cellView.findViewById(R.id.tvDetail);
		ImageView ivOption = (ImageView) cellView.findViewById(R.id.ivOption);
		tvDate.setTextColor(Color.BLACK);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			tvDate.setTextColor(resources
					.getColor(R.color.caldroid_darker_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tvDate.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(R.drawable.red_border_gray_bg);
			}

		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(R.color.caldroid_sky_blue));
			}

			tvDate.setTextColor(CaldroidFragment.selectedTextColor);

		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(R.drawable.red_border);
				ivOption.setImageResource(R.drawable.star);
				ivOption.setVisibility(ImageView.VISIBLE);
			} else {
				cellView.setBackgroundResource(R.drawable.cell_bg);
			}
		}
		
		
		tvDate.setText("" + dateTime.getDay());
		
		//get extra data and set text
		//날짜를 비교해서 년, 월, 일이 일치할때 setText 수행한다
		ArrayList<ScheduleInfoItem> tt = (ArrayList<ScheduleInfoItem>) extraData.get("info");
		String title = "";
		if(tt.size() != 0){
			for(int i=0; i < tt.size(); i++){
				Date date = new Date();
				DateTime dt = null;
				try {
					date = CalendarHelper.getDateFromString(tt.get(i).getDate(), "yyyy-MM-dd HH:mm");
					dt = CalendarHelper.convertDateToDateTime(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(dateTime.getYear().equals(dt.getYear())) {
					if(dateTime.getMonth().equals(dt.getMonth()))
						if(dateTime.getDay().equals(dt.getDay())){
							title += tt.get(i).getTitle() + "\n";
							tvDetail.setTextSize(10);
							tvDetail.setText(title);
						}
				}
				else{
					tvDetail.setText("");
				}
			}
		}
		else{
			tvDetail.setText("");
		}
		
		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);

		// Set custom color if required
		setCustomResources(dateTime, cellView, tvDate);

		return cellView;
	}

}

