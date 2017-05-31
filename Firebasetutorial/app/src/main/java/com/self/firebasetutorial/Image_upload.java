package com.self.firebasetutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Image_upload extends AppCompatActivity {
    Bitmap bitmap;
    DatabaseReference databaseReference;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        imageView = (ImageView) findViewById(R.id.img);
        databaseReference = FirebaseDatabase.getInstance().getReference("Img");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpg");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            new Async().execute();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class Async extends AsyncTask<Void, Void, Void> {
        String string;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Image_upload.this);
            dialog.setMessage("Uploading");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            string = getStringImage(bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String id = databaseReference.push().getKey();
            databaseReference.child(id).setValue(string);
            dialog.dismiss();
            Toast.makeText(Image_upload.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
            byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
    }
}