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
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<String> lightValues = new ArrayList<>();
    private Map<Float, Float> accelerometerValues = new ArrayMap<>();
    private List<String> toucheValues = new ArrayList<>();
    private Map<Double, Double> locationValues = new ArrayMap<>();
    private List<String> sonorValues = new ArrayList<>();

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
        if(toucheValues.size() < 5){
            toucheValues.add(Float.toString(posx));
            Log.d("valeurs touch", toucheValues.toString());
        }
        tv.setText("X:" + Float.toString(posx));

        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        String lightvalue = String.valueOf(sensorEvent.values[0]);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if(lightValues.size() < 5){
                lightValues.add(lightvalue);
                Log.d("valeurs light", lightValues.toString());
            }
            tvlight.setText("Light Sensor: " + lightvalue);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mSensorX = sensorEvent.values[0];
            mSensorY = sensorEvent.values[1];
            if(accelerometerValues.size() < 5){
                accelerometerValues.put(mSensorX, mSensorY);
                Log.d("valeurs accelerometre", accelerometerValues.toString());
            }
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
        if(locationValues.size()<5){
            locationValues.put(longtitude, latitude);
            Log.d("valeurs de localisation", locationValues.toString());
        }

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
