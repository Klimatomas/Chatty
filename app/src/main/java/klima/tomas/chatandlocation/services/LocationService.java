package klima.tomas.chatandlocation.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static klima.tomas.chatandlocation.LoginScreenActivity.MyPREFERENCES;

public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	GoogleApiClient mGoogleApiClient;
	SharedPreferences sharedPreferences;
	final double lastLatitude = 0;
	final double lastLongitude = 0;


	public LocationService() {
		super("LocationService");

	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		mGoogleApiClient.connect();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}
		super.onStart(intent, startId);


	}


	@Override
	public void onConnected(@Nullable Bundle bundle) {
		LocationRequest mLocationRequest = LocationRequest.create();


		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(5000);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// request permissions?
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);

		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		sharedPreferences.edit().putLong("Latitude", Double.doubleToLongBits(mLastLocation.getLatitude())).apply();
		sharedPreferences.edit().putLong("Longitude", Double.doubleToLongBits(mLastLocation.getLongitude())).apply();

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
		Toast.makeText(this, "Invalid name or password", Toast.LENGTH_SHORT).show();



	}

	@Override
	public void onLocationChanged(Location location) {
		sharedPreferences.edit().putLong("Latitude", Double.doubleToLongBits(location.getLatitude())).apply();
		sharedPreferences.edit().putLong("Longitude", Double.doubleToLongBits(location.getLongitude())).apply();
	}
}
