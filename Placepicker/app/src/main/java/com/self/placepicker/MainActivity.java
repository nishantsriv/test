package com.self.placepicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_placepicker, btn_currentlocation;
    private PlacePicker.IntentBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_placepicker = (Button) findViewById(R.id.btn_placepicker);
        btn_currentlocation = (Button) findViewById(R.id.btn_currentlocation);
        builder = new PlacePicker.IntentBuilder();
        btn_placepicker.setOnClickListener(this);
        btn_currentlocation.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            Toast.makeText(this, "" + place.getAddress(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_placepicker) {
            try {
                startActivityForResult(builder.build(MainActivity.this), 0);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else if (v == btn_currentlocation) {

        }
    }
}
