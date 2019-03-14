package miniprojetmob.m2dl.com;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SensorEventListener, LocationListener {
    //les capteurs
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor lightSensor;

    //listes
    private List<Double> listeSonore = new ArrayList<Double>();
    private Map<Float, Float> accelerometerValues = new ArrayMap<>();
    private List<Float> toucheValues = new ArrayList<>();
    private Map<Double, Double> locationValues = new ArrayMap<>();
    private List<Float> listeLight = new ArrayList<>();

    //variables
    private boolean mRunning = false;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private DetectNoise mSensor;
    private float mSensorX, mSensorY;
    private LocationManager locationManager;
    private LocationListener locationListener;


    private TextView tvnoise, tv, tvLight, tvAccele, tvGPS;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            start();
        }
    };


    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, 500);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensor = new DetectNoise();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseAlert");

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.lightSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //TextView
        tv = findViewById(R.id.tv);
        tvLight = findViewById(R.id.tvLight);
        tvAccele = findViewById(R.id.tvAccele);
        tvGPS = findViewById(R.id.tvGPS);
        tvnoise = findViewById(R.id.tvnoise);

        tv.setOnTouchListener(this);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                tvGPS.setText("logitude: "+ location.getLongitude() + " latitude " + location.getLongitude());
                locationValues.put(longitude, latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        configure();

        /**
         * When the user touch a Sensor TextView
         * Then it starts the Game with the values from the sensor
         */
        tvLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Jeu.class);
                intent.putExtra("lightValues", (ArrayList<Float>)listeLight);
                startActivity(intent);
            }
        });

        tvnoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Jeu.class);
                intent.putExtra("listeSonore", (ArrayList<Double>)listeSonore);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (!mRunning) {
            mRunning = true;
            start();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        stop();
    }
    private void start() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    0);
        }

        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
        mHandler.postDelayed(mPollTask, 500);
    }
    private void stop() {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        updateDisplay(0.0);
        mRunning = false;

    }

    private void updateDisplay(double signalEMA) {
        listeSonore.add(signalEMA);
        tvnoise.setText(signalEMA+"dB" +"\n");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float posx = event.getX();
        toucheValues.add(posx);
        tv.setText("X:" + Float.toString(posx) +"\n");
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            float lightvalue = event.values[0];
            listeLight.add(lightvalue);
            tvLight.setText("light sensor" + lightvalue+"\n");
        }else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mSensorX = event.values[0];
            mSensorY = event.values[1];
            accelerometerValues.put(mSensorX, mSensorY);
            tvAccele.setText("acceleration X: "+mSensorX + " acceleration Y: " +mSensorY +"\n");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        locationValues.put(longitude, latitude);
        tvGPS.setText("logitude: "+longitude + "latitude: "+ latitude +"\n");

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configure();
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void configure(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,0,locationListener);
    }
}
