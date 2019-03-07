package miniprojetmob.m2dl.com;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SensorEventListener, LocationListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor lightsensor;

    private TextView tv;
    private TextView tvlight;
    private TextView tvaccele;
    private TextView tvGPS;

    private float mSensorX, mSensorY;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance to the accelerometer
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.lightsensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // on crée un nouveau TextView, qui est un widget permettant
        // d'afficher du texte
        tv = new TextView(this);
        tvlight = new TextView(this);
        tvaccele = new TextView(this);
        tvGPS = new TextView(this);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        //}

        //Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        //onLocationChanged(location);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        // on veut que notre objet soit averti lors d'un évènement OnTouch sur
        // notre TextView :
        tv.setOnTouchListener(this);

        ll.addView(tvlight);
        //ll.addView(tvGPS);
        ll.addView(tvaccele);
        ll.addView(tv);

        // remplacer tout le contenu de notre activité par le TextView
        setContentView(ll);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float posx = motionEvent.getX();
        tv.setText("X:" + Float.toString(posx));

        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvlight.setText("Light Sensor: " + String.valueOf(sensorEvent.values[0]));
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mSensorX = sensorEvent.values[0];
            mSensorY = sensorEvent.values[1];
            tvaccele.setText(String.valueOf("Acceleration X: " + mSensorX + " Acceleration Y: " + mSensorY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightsensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double longtitude = 0;
        longtitude = location.getLongitude();
        double latitude = 0;
        latitude = location.getLatitude();
        tvGPS.setText("Longtitude: " + longtitude + " Latitude: " + latitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
