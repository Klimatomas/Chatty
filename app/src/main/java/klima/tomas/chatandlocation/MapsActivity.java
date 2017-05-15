package klima.tomas.chatandlocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static klima.tomas.chatandlocation.LoginScreenActivity.MyPREFERENCES;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
	private static GoogleMap mMap;
	SharedPreferences sharedPreferences;
	SharedPreferences.OnSharedPreferenceChangeListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);


		listener = (prefs, key) -> {
			LatLng changedLocation = new LatLng(Double.longBitsToDouble(sharedPreferences.getLong("Latitude", 0)), Double.longBitsToDouble(sharedPreferences.getLong("Longitude", 0)));
			mMap.clear();
			mMap.addMarker(new MarkerOptions().position(changedLocation).title("Current Location"));
		};

	}

	@Override
	public void onResume() {
		super.onResume();
		sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

	}

	@Override
	public void onPause() {
		super.onPause();
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng currentLocation = new LatLng(Double.longBitsToDouble(sharedPreferences.getLong("Latitude", 0)), Double.longBitsToDouble(sharedPreferences.getLong("Longitude", 0)));
		mMap.addMarker(new MarkerOptions().position(currentLocation).title("Last Known Location"));

	}

	public static Intent getStartIntent(Context context) {
		return new Intent(context, MapsActivity.class);
	}
}