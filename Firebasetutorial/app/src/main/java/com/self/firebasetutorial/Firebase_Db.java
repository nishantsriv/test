package com.self.firebasetutorial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase_Db extends AppCompatActivity {
    private DatabaseReference reference;
    private TextView textView;
    private String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase__db);
        textView = (TextView) findViewById(R.id.tv);
        reference = FirebaseDatabase.getInstance().getReference();
        id = reference.push().getKey();
        reference.child(id).setValue("Nishant");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(id).removeValue();
                textView.setText("Empty");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                name = String.valueOf(dataSnapshot.getChildren());
                textView.setText(name);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Toast.makeText(Firebase_Db.this, "Removed" + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}