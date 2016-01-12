package mobile.example.ma02_20131145;

public class MyCustomFragment extends CaldroidFragment {
	@Override
	//custom adapter로 커스텀뷰를 생성
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CustomViewAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
		
	}
}
