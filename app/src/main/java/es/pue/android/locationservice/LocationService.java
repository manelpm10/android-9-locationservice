package es.pue.android.locationservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LocationService extends Service {

    private final IBinder mBinder = new LocalBinder();

    public LocationService() {
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

    public String getLocations() {
        return "Locations data";
    }
}
