package miniprojetmob.m2dl.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Jeu extends AppCompatActivity {

    private List<Double> listeSonore = new ArrayList<Double>();
    private Map<Float, Float> accelerometerValues = new ArrayMap<>();
    private List<Float> toucheValues = new ArrayList<>();
    private Map<Double, Double> locationValues = new ArrayMap<>();
    private List<Float> listeLight = new ArrayList<>();

    private TextView tvnoise, tv, tvLight, tvAccele, tvGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        listeLight = (ArrayList<Float>) getIntent().getSerializableExtra("lightValues");
        listeSonore = (ArrayList<Double>) getIntent().getSerializableExtra("listeSonore");

        LinearLayout ll = new LinearLayout(this);
        tv = new TextView(this);
        ll.addView(tv);

        if(listeLight != null) {
            tv.setText("Light Sensor: " + listeLight);
        } else {
            tv.setText("Sonor Sensor: " + listeSonore);
        }

        setContentView(ll);
    }
}
