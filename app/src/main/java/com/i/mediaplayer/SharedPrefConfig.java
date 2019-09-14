package com.i.mediaplayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.i.mediaplayer.R;

public class SharedPrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.permission_pref),Context.MODE_PRIVATE);
    }

    public void writePermissionStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.permission_status_pref),status);
        editor.commit();
    }

    public boolean readPermissionStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.permission_status_pref),false);
        return status;
    }
}
