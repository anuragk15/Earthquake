package com.example.earthquake;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String API_KEY = String.valueOf(R.string.google_maps_key);
    private GoogleMap mMap;
    LocationManager locationManager;
    boolean buildVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    LocationListener locationListener;
    public Location mLocation;

    Marker mMarker;
    MarkerOptions marker=new MarkerOptions();
    public static final String tag = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocation = new Location("");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("tag", "API_KEY = " + API_KEY);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;

                mMarker.remove();
                onMapUpdate();

            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (buildVersion)
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
                return;
            } else if (providerEnabled) {
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
             else
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void onMapUpdate() {
        // GoogleMap mMap = googleMap;
        Log.d(tag, "Latitude and longitudes are " + mLocation.getLatitude() + " " + mLocation.getLongitude());
        checkPerm();
        //Add a marker in Sydney and move the camera
        LatLng currentLoc = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

        marker=new MarkerOptions().position(currentLoc).title("current");
        mMarker=mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(buildVersion) {
        if(grantResults.length>0 && grantResults[0]==0)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==0)
                //locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                if(checkPerm())
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                else
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

        }
        }

    }
    boolean checkPerm()
    {
        Log.d(tag," "+ (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED));
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(tag,"Latitude and longitudes are "+mLocation.getLatitude()+ " "+mLocation.getLongitude());
        //Add a marker in Sydney and move the camera
        checkPerm();
        LatLng IntialLoc = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
        mMarker=mMap.addMarker(marker.position(IntialLoc).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(IntialLoc));
      mMap.setMinZoomPreference(100);
      mMap.setMaxZoomPreference(150);


    }
}
