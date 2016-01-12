package mobile.example.ma02_20131145;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * MonthPagerAdapter holds 4 fragments, which provides fragment for current
 * month, previous month and next month. The extra fragment helps for recycle
 * fragments.
 * 
 * @author thomasdao
 * 
 */
public class MonthPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<DateGridFragment> fragments;

	// Lazily create the fragments
	public ArrayList<DateGridFragment> getFragments() {
		if (fragments == null) {
			fragments = new ArrayList<DateGridFragment>();
			for (int i = 0; i < getCount(); i++) {
				fragments.add(new DateGridFragment());
			}
		}
		return fragments;
	}

	public void setFragments(ArrayList<DateGridFragment> fragments) {
		this.fragments = fragments;
	}

	public MonthPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		DateGridFragment fragment = getFragments().get(position);
		return fragment;
	}
	
	@Override
	public int getItemPosition(Object object){
//		Log.d("pager", "call");
		return POSITION_NONE;
	}
	
	@Override
	public int getCount() {
		// We need 4 gridviews for previous month, current month and next month,
		// and 1 extra fragment for fragment recycle
		return CaldroidFragment.NUMBER_OF_PAGES;
	}

}
