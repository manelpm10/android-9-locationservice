package es.pue.android.locationservice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class LocationService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private final IBinder mBinder = new LocalBinder();
    private List<Location> locations;

    /**
     * Min. time to ask the location again. This is expensive in battery usage.
     *
     * GPS - More accuracy (+-15m), but more battery usage every request.
     * Coarse (Wifi or Celular) - Less accuracy but less battery usage.
     */
    private final static int MIN_TIME = 0; // 0 = ASAP

    /**
     * Min. distance the mobile has moved to ask the location again.
     */
    private final static int MIN_DISTANCE = 0; // 0 = ASAP

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locations = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locations.add(location);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                this.MIN_TIME,
                this.MIN_DISTANCE,
                locationListener
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Location getLastLocation() {
        if (0 == locations.size()) {
            return null;
        }
        return locations.get(locations.size() - 1);
    }
}
