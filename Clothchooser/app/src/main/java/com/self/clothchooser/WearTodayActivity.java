package com.self.clothchooser;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.self.clothchooser.database.ClothPair;
import com.self.clothchooser.database.DBAdapter;
import com.self.clothchooser.database.Table;
import com.self.clothchooser.database.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

public class WearTodayActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<ClothPair> clothPairArrayList;
    private ArrayList<String> stringArrayList;
    private SharedPreferences.Editor edit;
    private int random;
    private ImageView imageView1, imageView2;
    private Button dislikebtn, bookmarkbtn;
    private Random r;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private LinearLayout btn_logout, btn_screenshot;
    private FirebaseAuth firebaseAuth;
    private Set<String> set;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_today);
        init();
        btn_screenshot.setVisibility(View.VISIBLE);
        btn_logout.setVisibility(View.VISIBLE);
        btn_logout.setOnClickListener(this);
        clothPairArrayList = new ArrayList<>();
        /*new Asyncv().execute();*/
        DBAdapter adapter = new DBAdapter(WearTodayActivity.this);
        adapter.open();
        clothPairArrayList.clear();
        Cursor cursor = adapter.getAllpairs();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                byte[] blob_shirt = cursor.getBlob(cursor.getColumnIndex("shirtimage"));
                byte[] blob_pant = cursor.getBlob(cursor.getColumnIndex("pantimage"));
                clothPairArrayList.add(new ClothPair(id, blob_shirt, blob_pant));
            }
            adapter.close();
        }
        if (clothPairArrayList.size() == 0) {
            startActivity(new Intent(WearTodayActivity.this, Add_clothActivity.class));
            finish();
            Toast.makeText(WearTodayActivity.this, "Redirecting to Add Pair Page due to empty catalogue", Toast.LENGTH_SHORT).show();
        } else {
            r = new Random();
            random = r.nextInt(clothPairArrayList.size());
            imageView1 = (ImageView) findViewById(R.id.img1);
            imageView2 = (ImageView) findViewById(R.id.img2);
            imageView1.setImageBitmap(imageresize(Utility.getPhoto(clothPairArrayList.get(random).getShirt())));
            imageView2.setImageBitmap(imageresize(Utility.getPhoto(clothPairArrayList.get(random).getPant())));
        }
        stringArrayList = new ArrayList<>();
        dislikebtn.setOnClickListener(this);
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        edit = prefs.edit();
        set = prefs.getStringSet("yourKey", null);
        if (set == null) {
            set = new HashSet<>();
        }
        bookmarkbtn.setOnClickListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().add(R.id.container, new NavigationFragment()).commit();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dislikebtn = (Button) findViewById(R.id.dislike);
        bookmarkbtn = (Button) findViewById(R.id.bookmark);
        btn_logout = (LinearLayout) findViewById(R.id.logoutbtn);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        btn_screenshot = (LinearLayout) findViewById(R.id.btn_screenshot);
        btn_screenshot.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    private Bitmap imageresize(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 450, 450, false);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        if (v == bookmarkbtn) {
            stringArrayList.add(String.valueOf(clothPairArrayList.get(random).getId()));
            set.addAll(stringArrayList);
            edit.putStringSet("yourKey", set);
            edit.apply();
            Toast.makeText(WearTodayActivity.this, "Added to Bookmark", Toast.LENGTH_SHORT).show();
        } else if (v == btn_screenshot) {
            checkForexternalPermission();
        } else if (v == dislikebtn) {
            random = r.nextInt(clothPairArrayList.size());
            imageView1.setImageBitmap(imageresize(Utility.getPhoto(clothPairArrayList.get(random).getShirt())));
            imageView2.setImageBitmap(imageresize(Utility.getPhoto(clothPairArrayList.get(random).getPant())));
        } else if (v == btn_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(WearTodayActivity.this, LoginActivity.class));
            finish();
            deleteDatabase(Table.DB_NAME);
            prefs.edit().remove("yourKey").commit();
        }
    }



    public void share_bitMap_to_Apps(Bitmap bitmap) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap));
        try {
            startActivity(Intent.createChooser(i, "My Profile ..."));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void checkForexternalPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Store Access is compulsory to send screenshot!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                6);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        6);


            }
        } else {
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            share_bitMap_to_Apps(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 6) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);
                share_bitMap_to_Apps(bitmap);
            }
        }
    }
}
