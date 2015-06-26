package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oansari.spotifystreamer.Helpers.DialogHelper;
import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.Spotify;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Osama on 6/19/2015.
 */
public class PlayerDialogFragment extends DialogFragment {

    @InjectView(R.id.artistNameTextView)
    TextView mArtistNameTextView;
    @InjectView(R.id.trackNameTextView)
    TextView mTrackNameTextView;
    @InjectView(R.id.AlbumArtImageView)
    ImageView mAlbumArtImageView;
    @InjectView(R.id.albumNameTextView)
    TextView mAlbumNameTextView;
    @InjectView(R.id.elapsedTimeTextView)
    TextView mElapsedTimeTextView;
    @InjectView(R.id.leftTimeTextView)
    TextView mLeftTimeTextView;
    @InjectView(R.id.playButton)
    ImageButton mPlayButton;
    @InjectView(R.id.seekBar)
    SeekBar mSeekBar;

    private double timeElapsed = 0, finalTime = 0;

    private int forwardTime = 2000, backwardTime = 2000;

    private Handler durationHandler = new Handler();


    private static Uri previewStream;

    private static Track mTrack;

    private static MediaPlayer mMediaPlayer;

    private static Context mContext;

    public static boolean mTwoPane;

    private int lastPosition = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog dialog = builder.create();
//        dialog.setContentView(R.layout.fragment_player);
       View  view = getActivity().getLayoutInflater().inflate(R.layout.fragment_player, null);
        ButterKnife.inject(this, view);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        prepareMediaPlayer();
        addEvents();
        feedInfo();
        builder.setView(view);
        Dialog dialog = builder.create();

        return dialog;
    }

    private void feedInfo() {
        mArtistNameTextView .setText(mTrack.artists.get(0).name);
        mTrackNameTextView.setText(mTrack.name);
        mAlbumNameTextView.setText(mTrack.album.name);
        Picasso.with(getActivity()).load(mTrack.album.images.get(0).url).into(mAlbumArtImageView);
        mMediaPlayer.prepareAsync();
        mPlayButton.setEnabled(false);
    }

    @Override
    public void onStop() {
        durationHandler.removeCallbacks(updateSeekBarTime);
        mMediaPlayer.release();
        super.onStop();

    }

    private void addEvents() {

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mSeekBar.setMax(mMediaPlayer.getDuration());
                mLeftTimeTextView.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long)  finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) mMediaPlayer.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mMediaPlayer.getDuration()))));
                mPlayButton.setEnabled(true);

                if (lastPosition > 0){
                    mMediaPlayer.seekTo(lastPosition);
                    play();
                    mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMediaPlayer.isPlaying()) {
                   pause();
                    mPlayButton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    play();
                    mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
                }
            }

        });
    }

    public static PlayerDialogFragment newInstance (boolean twoPane, Track track){
        mTwoPane = twoPane;
        mTrack = track;
        previewStream = Uri.parse(mTrack.preview_url);
        mMediaPlayer = new MediaPlayer();

        return new PlayerDialogFragment();

    }

    public void play(){
        mMediaPlayer.start();
        timeElapsed = mMediaPlayer.getCurrentPosition();
        mSeekBar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    private Runnable updateSeekBarTime = new Runnable() {
        @Override
        public void run() {
            //get current position
            timeElapsed = mMediaPlayer.getCurrentPosition();
            //set seekbar progress
            mSeekBar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            mElapsedTimeTextView.setText(
                    String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), (-1) * TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

            mLeftTimeTextView.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long)  (finalTime + timeRemaining)),
                    TimeUnit.MILLISECONDS.toSeconds((long) (mMediaPlayer.getDuration() + timeRemaining)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) (mMediaPlayer.getDuration() +timeRemaining) ))));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);

        }
    };

    // pause
    public void pause(){
        mMediaPlayer.pause();
    }

    // go forward at forwardTime seconds
    public void forward(View view) {
        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed + forwardTime) >  0) {
            timeElapsed = timeElapsed - backwardTime;
            //seek to the exact second of the track
            mMediaPlayer.seekTo((int) timeElapsed);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getActivity().getApplicationContext();



    }

    private void  prepareMediaPlayer(){
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }catch (Exception e){
            mMediaPlayer = new MediaPlayer();
        }
        try {
            mMediaPlayer.setDataSource(mContext, previewStream);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putDouble("position", timeElapsed);
        //pause();
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            lastPosition = (int)savedInstanceState.getDouble("position");
        }

        if(mTwoPane){
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        prepareMediaPlayer();
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.inject(this, view);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addEvents();
        feedInfo();




        return view;
    }

    @Override
    public void onPause() {
        mMediaPlayer.release();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        int dialogWidth = 800;
        int dialogHeight = 1100;

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }


}