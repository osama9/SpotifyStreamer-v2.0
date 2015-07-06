package com.oansari.spotifystreamer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oansari.spotifystreamer.Constants;
import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.models.ParcelableTrack;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Osama on 7/2/2015.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String LOG_TAG = "ForegroundService";
    public static MediaPlayer mMediaPlayer;
    private Track mTrack;
    private Tracks mTracks;
    private int mCurrentPosition;
    private Context mContext;
    private int mTrackPosition;
    public MediaPlayerService(Context context){
        mContext = context;
    }

    public MediaPlayerService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentPosition = 0;

    }

    public void initMediaPlayer(){
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }


    public void setTrack(Track track){
        mTrack = track;
    }


    public void play(){
        Uri previewStream = Uri.parse(mTrack.preview_url);
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(getApplicationContext(), previewStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
    }

    public void release(){mMediaPlayer.release();}
    public void stop(){mMediaPlayer.stop();}
    public void prepareAsync(){mMediaPlayer.prepareAsync();}
    public void pause(){
        mMediaPlayer.pause();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaPlayer = new MediaPlayer();


        if(intent != null){
            Type type = new TypeToken<Tracks>(){}.getType();
            mTracks = new Gson().fromJson(intent.getStringExtra("TRACK_LIST"), type);
            mTrackPosition = intent.getIntExtra("TRACK_POSITION", 0);
            mTrack = mTracks.tracks.get(mTrackPosition);
        }
        initMediaPlayer();
        play();
        if(intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, MediaPlayerService.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Intent previustIntent = new Intent(this, MediaPlayerService.class);
            previustIntent.setAction(Constants.ACTION.PREV_ACTION);
            PendingIntent pPreviusIntent = PendingIntent.getService(this, 0, previustIntent, 0);

            Intent playIntent = new Intent(this, MediaPlayerService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);

            Intent nextIntent = new Intent(this, MediaPlayerService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
            PendingIntent pNextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_today);


            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Media Player")
                    .setTicker("Media Player")
                    .setContentText("My Media Player")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false)
                    )
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_previous, "Previus", pPreviusIntent)
                    .addAction(android.R.drawable.ic_media_play, "Play", pPlayIntent)
                    .addAction(android.R.drawable.ic_media_next, "Next", pNextIntent).build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
        }
        else if(intent.getAction().equals(Constants.ACTION.PREV_ACTION)){
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Log.i(LOG_TAG, "Clicked Play");
            if(mMediaPlayer.isPlaying())
                pause();
            else
                play();
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, "Clicked Next");
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;
    }

//    @Override
//    public boolean onUnbind(Intent intent) {
//        //mMediaPlayer.stop();
//        //mMediaPlayer.release();
//        return false;
//    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.ACTION.PREPARED_ACTION);
        sendBroadcast(broadcastIntent);
        mMediaPlayer.start();
    }
}
