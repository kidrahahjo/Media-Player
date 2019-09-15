package com.i.mediaplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SharedPrefConfig sharedPrefConfig;
    private static final int REQUEST_STORAGE = 123;
    private int length;
    private String[] indexList;
    private String[] songNameList;
    private RecyclerView.Adapter musicAdapter;
    private RecyclerView songlist;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Music> musicArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        songlist = findViewById(R.id.songView);
        layoutManager = new LinearLayoutManager(this);
        songlist.setLayoutManager(layoutManager);
        songlist.setHasFixedSize(true);


        sharedPrefConfig = new SharedPrefConfig(getApplicationContext());

        permissionCheck();


    }

    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                showPermissionExplanation();
            }
            else if(!sharedPrefConfig.readPermissionStatus()){
                requestPermission();
                sharedPrefConfig.writePermissionStatus(true);

            }
            else{
                Toast.makeText(getApplicationContext(),"PLEASE GRANT STORAGE PERMISSION FROM SETTINGS",Toast.LENGTH_LONG).show();
                Intent in =new Intent();
                in.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package",this.getPackageName(),null);
                in.setData(uri);
                this.startActivity(in);
            }

        }
        else{
            loadSongs();

        }
    }

    private void loadSongs(){
        musicArrayList = new ArrayList<>();
        getSongs();
        musicAdapter = new MusicAdapter(this,songNameList);
        songlist.setAdapter(musicAdapter);
    }

    private void getSongs() {
        ContentResolver contentResolver = getContentResolver();
        Uri songURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songURI,null,null,null,null);
        length = songCursor.getCount();
        indexList = new String[length];
        songNameList = new String[length];
        Log.d("SongLength",length+"");
        if(songCursor!=null && songCursor.moveToFirst()){
            int count=0;
            int intPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int intSongName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do{
                String path = songCursor.getString(intPath);
                String songName = songCursor.getString(intSongName);
                Music music = new Music(songName,path);
                musicArrayList.add(music);
                songNameList[count] = songName;
                indexList[count] = path;
                count++;
            }while(songCursor.moveToNext());
        }
        else{
            Toast.makeText(getApplicationContext(),"No songs present",Toast.LENGTH_LONG).show();
        }
    }

    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Storage Permission Needed");
        builder.setMessage("We need your permission to show you songs");
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermission();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_STORAGE);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.refresh:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }
                else{
                    Toast.makeText(getApplicationContext(),"We need your permission to show you songs", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.permission:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"STORAGE PERMISSION ALREADY GRANTED",Toast.LENGTH_LONG).show();
                }
                else{
                    permissionCheck();
                }
                break;
            case R.id.about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
