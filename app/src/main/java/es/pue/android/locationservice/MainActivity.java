package es.pue.android.locationservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Maps variables.
    private GoogleMap map;
    private boolean isMapReady = false;

    // Locatino binded service variables.
    private LocationService locationBindService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationBindService = ((LocationService.LocalBinder) service).getService();
            Toast.makeText(
                    MainActivity.this,
                    "Connection stablished!",
                    Toast.LENGTH_SHORT
            ).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationBindService = null;
            Toast.makeText(
                    MainActivity.this,
                    "Connection disconnected!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.frMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(conn);
    }

    private void initService() {
        Intent i = new Intent(this, LocationService.class);
        bindService(i, conn, Context.BIND_AUTO_CREATE);
    }

    public void getLocationData(View view) {
        Location lastLocation = locationBindService.getLastLocation();
        if (null != lastLocation && isMapReady) {
            LatLng bcnCoordinates = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            CameraPosition target = CameraPosition.builder().target(bcnCoordinates).zoom(16).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        map = googleMap;

        LatLng bcnCoordinates = new LatLng(41.3870154,2.1678531);
        CameraPosition target = CameraPosition.builder().target(bcnCoordinates).zoom(16).tilt(65).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
