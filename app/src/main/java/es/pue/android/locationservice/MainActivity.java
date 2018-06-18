package es.pue.android.locationservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
        Log.d("BIND_SERVICE", "getLocationData: "+locationBindService.getLocations());
    }
}
