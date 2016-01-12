/*
 * googlemap을 이용하여 검색 위치를 표시하고
 * 목적지까지 가는 경로를 알려주는 액티비티이다.
 * 2015-12-17
 * @author 황윤정
 */
package mobile.example.ma02_20131145;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchLocationActivity extends Activity {
	
	LocationManager locManager;
	String bestProvider;
	MarkerOptions markerOption;
	EditText etLocName;
	GoogleMap googlemap;
	Geocoder geocoder;
	double latitude;
	double longitude;
	Location lastLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_location);
		//액션바 뒤로가기버튼
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		etLocName = (EditText)findViewById(R.id.etLocName);
		//location manager 준비
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//조건 지정
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		bestProvider = locManager.getBestProvider(criteria, true);
		locManager.requestLocationUpdates(bestProvider, 0, 0, listener);
		//사용자 입력값 받기
		Intent intent = getIntent();
		String inputloc = intent.getStringExtra("InputLoc");
		etLocName.setText(inputloc);
		//google 지도
		googlemap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		//마커 모양 지정
		markerOption = new MarkerOptions();
		markerOption.title("현재위치");
		markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.userlocation50));
		//그전에 받아온 위치
		lastLocation = locManager.getLastKnownLocation(bestProvider);
		if(lastLocation != null) {
			LatLng lastLoc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
			markerOption.position(lastLoc);
			googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 17));
		}
	}
	
	public void onClick(View v) {
		String des;
		switch(v.getId()){
		case R.id.btnOk:
			geocoder = new Geocoder(this);
			List<Address> list = null;
			des = etLocName.getText().toString();
			if(des.length() == 0){
				Toast.makeText(this, "장소를 입력하세요.", Toast.LENGTH_SHORT).show();
				break;
			}
			try {
				list = geocoder.getFromLocationName(des, 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LatLng currentLoc = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
			googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
			markerOption.position(currentLoc);
			Marker centerMarker = googlemap.addMarker(markerOption);
			centerMarker.showInfoWindow();
			break;
		case R.id.btnCall:
			geocoder = new Geocoder(this);
			List<Address> list2 = null;
			des = etLocName.getText().toString();
			if(des.length() == 0){
				Toast.makeText(this, "장소를 입력하세요.", Toast.LENGTH_SHORT).show();
				break;
			}
			try {
				list2 = geocoder.getFromLocationName(des, 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String route = String.format("http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
										lastLocation.getLatitude(), lastLocation.getLongitude(),
										list2.get(0).getLatitude(),list2.get(0).getLongitude());
			Uri uri = Uri.parse(route);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;
		}
	}
	
	LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	};
	
	public void onPause(){
		super.onPause();
		locManager.removeUpdates(listener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_location, menu);
		return true;
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
