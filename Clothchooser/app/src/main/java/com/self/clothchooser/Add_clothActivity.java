package com.self.clothchooser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.self.clothchooser.database.DBAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class Add_clothActivity extends AppCompatActivity implements View.OnClickListener {
    private Button shirt_galletybtn, shirt_camerabtn, pant_gallerybtn, pant_camerabtn, upload;
    private Bitmap bitmap1, bitmap2;
    private ImageView imageView1, imageView2;
    DatabaseReference databaseReference2;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cloth);
        init();
        String id = databaseReference2.push().getKey();
        String value = getIntent().getStringExtra("userid");
        databaseReference2.child(id).setValue(value);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().add(R.id.container, new NavigationFragment()).commit();
    }

    private void init() {
        databaseReference2 = FirebaseDatabase.getInstance().getReference("exist");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        shirt_galletybtn = (Button) findViewById(R.id.shirtgallery);
        shirt_camerabtn = (Button) findViewById(R.id.shirtcamera);
        pant_gallerybtn = (Button) findViewById(R.id.pantgallery);
        pant_camerabtn = (Button) findViewById(R.id.pantcamera);
        imageView1 = (ImageView) findViewById(R.id.image_shirt);
        imageView2 = (ImageView) findViewById(R.id.image_pant);
        shirt_galletybtn.setOnClickListener(this);
        shirt_camerabtn.setOnClickListener(this);
        pant_camerabtn.setOnClickListener(this);
        pant_gallerybtn.setOnClickListener(this);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            try {
                /*bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);*/
                bitmap1 = new ImageResizer(imageuri, getContentResolver()).getBitmap();
                imageView1.setImageBitmap(imageresize(bitmap1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            try {
                /*bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);*/
                bitmap2 = new ImageResizer(imageuri, getContentResolver()).getBitmap();
                imageView2.setImageBitmap(imageresize(bitmap2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            bitmap1 = (Bitmap) data.getExtras().get("data");
            imageView1.setImageBitmap(bitmap1);
        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            bitmap2 = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(bitmap2);
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == shirt_galletybtn) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 0);
        } else if (v == shirt_camerabtn) {
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 2);
        } else if (v == pant_gallerybtn) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 3);
        } else if (v == pant_camerabtn) {
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 4);
        } else if (v == upload) {
            if (bitmap1 != null && bitmap2 != null) {
                new Asyncinsert().execute();
            } else Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap imageresize(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 450, 450, false);
        return bitmap;
    }

    class Asyncinsert extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        long result;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Add_clothActivity.this);
            progressDialog.setMessage("Saving...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DBAdapter adapter = new DBAdapter(Add_clothActivity.this);
            adapter.open();
            result = adapter.insert(bitmap1, bitmap2);
            adapter.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Toast.makeText(Add_clothActivity.this, "" + result, Toast.LENGTH_SHORT).show();
        }
    }


}
