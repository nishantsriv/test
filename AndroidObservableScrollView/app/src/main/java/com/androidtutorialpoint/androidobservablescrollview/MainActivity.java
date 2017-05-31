package com.androidtutorialpoint.androidobservablescrollview;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String CATEGORY_SAMPLES = MainActivity.class.getPackage().getName();
    private static final String TAG_CLASS_NAME = "className";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_INTENT = "intent";

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        refreshData();
    }

    private void refreshData() {
        listView.setAdapter(new SimpleAdapter(this, getData(),
                R.layout.list_item_main,
                new String[]{TAG_CLASS_NAME, TAG_DESCRIPTION,},
                new int[]{R.id.className, R.id.description,}));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menu) {
        return false;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> data = new ArrayList<>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.setPackage(getApplicationContext().getPackageName());
        mainIntent.addCategory(CATEGORY_SAMPLES);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (list == null) {
            return data;
        }

        addItem(data,
               "ParallaxToolbarScrollViewActivity",
                "Parallax scrollView & toolbar",
                activityIntent(
                        "com.androidtutorialpoint.androidobservablescrollview",
                        "com.androidtutorialpoint.androidobservablescrollview.ParallaxToolbarScrollViewActivity"));

        return data;
    }

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String className, String description,
                           Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        temp.put(TAG_CLASS_NAME, className);
        temp.put(TAG_DESCRIPTION, description);
        temp.put(TAG_INTENT, intent);
        data.add(temp);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);

        Intent intent = (Intent) map.get(TAG_INTENT);
        startActivity(intent);
    }
}