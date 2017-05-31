package com.self.distance;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    BroadcastReceiver receiver;
    double lat, lng;
    String response_url;
    RequestQueue requestQueue;
    ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    private Button btn_autocurrent, btn_choosecurrent;
    private PlacePicker.IntentBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_autocurrent = (Button) findViewById(R.id.btn_autocurrentlocation);
        btn_choosecurrent = (Button) findViewById(R.id.currentlocation);
        btn_autocurrent.setOnClickListener(this);
        btn_choosecurrent.setOnClickListener(this);
        builder = new PlacePicker.IntentBuilder();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
/*                progressDialog.dismiss();*/
                lat = Double.parseDouble(SharedPrefManager.getInstance(MainActivity.this).getDetail("latlng", "lat"));
                lng = Double.parseDouble(SharedPrefManager.getInstance(MainActivity.this).getDetail("latlng", "lng"));
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                    Toast.makeText(MainActivity.this, "" + addressList.get(0).getAddressLine(0) + "," + addressList.get(0).getAddressLine(1), Toast.LENGTH_SHORT).show();
                    stopService(new Intent(MainActivity.this, CurrentLocationService.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        registerReceiver(receiver, new IntentFilter("PD"));
    }

    private String geturl(double lat1, double v, double lat, double lng) {
        return "http://maps.google.com/maps/api/directions/json?origin=" + lat1 + "," + v + "&destination=" + lat + "," + lng + "&sensor=false&units=metric";
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_autocurrent) {
            startService(new Intent(this, CurrentLocationService.class));
            String latlng = SharedPrefManager.getInstance(this).getDetail("latlng", "lat");
            Toast.makeText(this, ""+latlng, Toast.LENGTH_SHORT).show();
        } else if (v == btn_choosecurrent) {
            try {
                startActivityForResult(builder.build(this), 0);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    class Asynctime extends AsyncTask<Void, Void, Void> {
        String time;

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response_url);
                JSONArray jsonArray = jsonObject.getJSONArray("routes");
                JSONObject jsonobj = jsonArray.getJSONObject(0);
                JSONArray jsonArray2 = jsonobj.getJSONArray("legs");
                JSONObject jsonob = jsonArray2.getJSONObject(0);
                JSONObject jsonon = jsonob.getJSONObject("duration");
                time = jsonon.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("TEST", time);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            Log.d("TEST", "" + place.getName());
            LatLng latLng = place.getLatLng();
            lat = latLng.latitude;
            lng = latLng.longitude;
            Toast.makeText(this, "" + place.getAddress(), Toast.LENGTH_SHORT).show();
        }
    }
}
