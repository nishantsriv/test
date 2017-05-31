package com.self.distance;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nishant on 08-05-2017.
 */

public class SharedPrefManager {


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public boolean saveDetail(String name, String key, String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }



    public String getDetail(String name, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }


}
