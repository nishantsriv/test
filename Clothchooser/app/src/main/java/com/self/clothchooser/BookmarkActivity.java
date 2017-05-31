package com.self.clothchooser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.self.clothchooser.database.ClothPair;
import com.self.clothchooser.database.DBAdapter;
import com.self.clothchooser.database.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookmarkActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private LinearLayout btn_logout;
    private FirebaseAuth firebaseAuth;
    private TextView textView_bookmark;
    private RecyclerView recyclerView;
    private ArrayList<ClothPair> clothPairArrayList;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        init();
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("yourKey", null);
        if (set == null) {
            textView_bookmark.setVisibility(View.VISIBLE);
        } else {
            List<String> sample = new ArrayList<>(set);
            DBAdapter adapter = new DBAdapter(this);
            adapter.open();
            for (int i = 0; i < sample.size(); i++) {

                Cursor cursor = adapter.getsinglecolumn(Integer.parseInt(sample.get(i)));
                if (cursor != null && cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    byte[] blob_shirt = cursor.getBlob(cursor.getColumnIndex("shirtimage"));
                    byte[] blob_pant = cursor.getBlob(cursor.getColumnIndex("pantimage"));
                    clothPairArrayList.add(new ClothPair(id, blob_shirt, blob_pant));
                }

            }
            adapter.close();
            recyclerView.setAdapter(new BookmarkAdapter(this, clothPairArrayList));
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().add(R.id.container, new NavigationFragment()).commit();
    }

    private void init() {
        clothPairArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rview_bookmark);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView_bookmark = (TextView) findViewById(R.id.tv_bookmark);
        btn_logout = (LinearLayout) findViewById(R.id.logoutbtn);
        btn_logout.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_logout.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(BookmarkActivity.this, LoginActivity.class));
            finish();
            deleteDatabase(Table.DB_NAME);
            prefs.edit().remove("yourKey").commit();
        }
    }
}
