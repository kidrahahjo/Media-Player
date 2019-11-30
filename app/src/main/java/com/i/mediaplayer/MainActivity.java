package com.i.mediaplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements musicInterface{
    private SharedPrefConfig sharedPrefConfig;
    private static final int REQUEST_STORAGE = 123;
    private int length;
    private int position;
    private String[] pathList;
    private String[] songNameList;
    private String[] songArtList;
    private RecyclerView.Adapter musicAdapter;
    private RecyclerView songlist;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Music> musicArrayList;
    public MediaPlayer mediaPlayer = null;
    public String initialTime;
    public String maxTime;
    public boolean isPlaying = false;
    Toolbar toolbar;
    MenuItem playMenu;
    public static Context context;
    TextView max;
    TextView current;
    TextView nameTV;
    TextView artistTV;
    SeekBar seekBar;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        songlist = findViewById(R.id.songView);

        max = findViewById(R.id.maxTime);
        current = findViewById(R.id.currentTime);
        nameTV = findViewById(R.id.nameTV);
        artistTV = findViewById(R.id.artistTV);
        seekBar = findViewById(R.id.seekbar);

        layoutManager = new LinearLayoutManager(this);
        songlist.setLayoutManager(layoutManager);
        songlist.setHasFixedSize(true);


        sharedPrefConfig = new SharedPrefConfig(getApplicationContext());

        context = this.getApplicationContext();

        permissionCheck();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        musicAdapter = new MusicAdapter(this,musicArrayList);
        songlist.setAdapter(musicAdapter);
    }

    private void getSongs() {
        ContentResolver contentResolver = getContentResolver();
        Uri songURI = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songURI,null,null,null,null);
        length = songCursor.getCount();
        pathList = new String[length];
        songNameList = new String[length];
        songArtList = new String[length];
        Log.d("SongLength",length+"");
        if(songCursor!=null && songCursor.moveToFirst()){
            int count=0;
            int intPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int intSongName = songCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int intSongArt = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do{
                String path = songCursor.getString(intPath);
                String songName = songCursor.getString(intSongName);
                String songArt = songCursor.getString(intSongArt);
                Music music = new Music(songName,path,songArt);
                musicArrayList.add(music);
                songNameList[count] = songName;
                pathList[count] = path;
                songArtList[count]=songArt;
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
        playMenu = menu.findItem(R.id.playpause);
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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    permissionCheck();
                }
                else{
                    Toast.makeText(getApplicationContext(),"STORAGE PERMISSION ALREADY GRANTED",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.about:
                break;
            case  R.id.playpause:
                playAndPause();
                break;
            case R.id.next:
                playNextSong();
                break;
            case R.id.prev:
                playPrevSong();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void startMusic(int position){
        if(isPlaying){
            mediaPlayer.stop();
        }
        this.position = position;
        Uri u = Uri.parse(musicArrayList.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this,u);
        mediaPlayer.start();
        int maxTime = mediaPlayer.getDuration();
        String maxS = convertTime(maxTime);
        max.setText(maxS);
        setNames();
        seekBar.setMax((int)Math.round(maxTime/1000.0));
        isPlaying = true;
        updateMenu();

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = (int)Math.round(mediaPlayer.getCurrentPosition() / 1000.0);
                    seekBar.setProgress(mCurrentPosition);
                    current.setText(convertTime(mediaPlayer.getCurrentPosition()));
                    if(current.getText().toString().equals(max.getText().toString())){
                        playNextSong();
                    }
                }
                else {
                    playNextSong();
                }
                mHandler.postDelayed(this, 100);
            }
        });

    }


    public void updateMenu(){
        if(isPlaying){
            playMenu.setIcon(R.drawable.ic_pause_black_24dp);
        }
        else{
            playMenu.setIcon(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    public void playAndPause(){
        try {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playMenu.setIcon(R.drawable.ic_play_arrow_black_24dp);
            } else {
                mediaPlayer.start();
                playMenu.setIcon(R.drawable.ic_pause_black_24dp);
            }
        }catch (Exception e){
            position = 0;
            startMusic(position);
        }
    }

    public void playNextSong(){
        if(position>=length-1){
            position = 0;
        }else{
            position+=1;
        }
        if(isPlaying){
            mediaPlayer.stop();
        }
        Uri u = Uri.parse(musicArrayList.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this,u);
        mediaPlayer.start();
        setNames();
        int maxTime = mediaPlayer.getDuration();
        String maxS = convertTime(maxTime);
        max.setText(maxS);
        seekBar.setMax((int)Math.round(maxTime/1000.0));

        isPlaying = true;
        updateMenu();
    }

    public void playPrevSong(){
        if(position==0){
            position = length -1;
        }else{
            position-=1;
        }
        if(isPlaying){
            mediaPlayer.stop();
        }
        Uri u = Uri.parse(musicArrayList.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this,u);
        mediaPlayer.start();
        int maxTime = mediaPlayer.getDuration();
        String maxS = convertTime(maxTime);
        max.setText(maxS);
        seekBar.setMax((int)Math.round(maxTime/1000.0));
        setNames();
        isPlaying = true;
        updateMenu();
    }

    public String convertTime(int musicDuration){
        int minT = musicDuration/1000/60;
        int secT = (int)Math.round(musicDuration/1000.0)-((musicDuration/1000)/60)*60;
        String min = Integer.toString(minT);
        String sec = Integer.toString(secT);
        if(secT<10){
            sec = "0"+secT;
        }
        return min + ":" + sec;
    }

    public void setNames(){
        nameTV.setText(songNameList[position]);
        artistTV.setText(songArtList[position]);
    }
}
