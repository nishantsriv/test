package com.self.clothchooser;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.self.clothchooser.database.DBAdapter;
import com.self.clothchooser.database.Utility;

import java.io.ByteArrayOutputStream;

public class BookmarkSingleActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView_shirt, imageView_pant;
    private LinearLayout btn_screenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_single);
        init();
        int id = getIntent().getIntExtra("id", 1);
        DBAdapter adapter = new DBAdapter(this);
        adapter.open();
        Cursor cursor = adapter.getsinglecolumn(id);
        if (cursor != null && cursor.moveToFirst()) {
            imageView_shirt.setImageBitmap(imageresize(Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex("shirtimage")))));
            imageView_pant.setImageBitmap(imageresize(Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex("pantimage")))));
        }
        adapter.close();
    }

    private void init() {
        btn_screenshot = (LinearLayout) findViewById(R.id.btn_screenshot);
        btn_screenshot.setVisibility(View.VISIBLE);
        btn_screenshot.setOnClickListener(this);
        imageView_shirt = (ImageView) findViewById(R.id.img1);
        imageView_pant = (ImageView) findViewById(R.id.img2);
    }

    private Bitmap imageresize(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 450, 450, false);
        return bitmap;
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
        inImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_screenshot) {
            checkForexternalPermission();
        }
    }

    private void checkForexternalPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Store Access is compulsory to send screenshot!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                6);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
