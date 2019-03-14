package miniprojetmob.m2dl.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Jeu extends AppCompatActivity {

    private ArrayList<String> myList = new ArrayList<String>();
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        myList = (ArrayList<String>) getIntent().getSerializableExtra("lightValues");

        tv = new TextView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.addView(tv);
        tv.setText("Light Sensor: " + myList);
        setContentView(ll);
    }
}
