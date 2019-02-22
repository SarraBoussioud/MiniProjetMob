package miniprojetmob.m2dl.com;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance to the accelerometer
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // on crée un nouveau TextView, qui est un widget permettant
        // d'afficher du texte
        tv = new TextView(this);

        // on veut que notre objet soit averti lors d'un évènement OnTouch sur
        // notre TextView :
        tv.setOnTouchListener(this);

        // remplacer tout le contenu de notre activité par le TextView
        setContentView(tv);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float posx = motionEvent.getX();
        tv.setText(Float.toString(posx));

        return true;
    }
}
