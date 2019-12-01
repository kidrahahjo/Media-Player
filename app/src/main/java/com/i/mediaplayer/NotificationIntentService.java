package com.i.mediaplayer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.Toast;

public class NotificationIntentService extends IntentService {
    private musicInterface myMusicInt;
    private Context context = getApplicationContext();

    public NotificationIntentService() {
        super("notificationIntentService");
        try {
            myMusicInt = ((musicInterface) context);
        } catch (ClassCastException e) {
            Toast.makeText(context,"ERROR OCCURRED, PLEASE TRY AGAIN",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            switch (intent.getAction()) {
                case "left":
                    Handler leftHandler = new Handler(Looper.getMainLooper());
                    leftHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myMusicInt.playPrevSong();
                        }
                    });
                    break;
                case "right":
                    Handler rightHandler = new Handler(Looper.getMainLooper());
                    rightHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myMusicInt.playAndPause();
                        }
                    });
                    break;
                case "middle":
                    Handler middleHandler = new Handler(Looper.getMainLooper());
                    middleHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myMusicInt.playNextSong();
                        }
                    });
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_LONG).show();

            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_LONG).show();
        }
    }
}

