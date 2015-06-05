package com.oansari.spotifystreamer.views;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.Spotify;
import com.oansari.spotifystreamer.adapters.AtristListAdapter;
import com.oansari.spotifystreamer.models.Artist;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    long idle_min = 4000; // 4 seconds after user stops typing
    long last_text_edit = 0;
    Handler h = new Handler();
    boolean already_queried = false;

    AtristListAdapter adapter;

    @InjectView(R.id.listView)
    ListView mlistView;
    @InjectView(R.id.filterEditText)
    EditText mFilterEditText;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        mlistView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        Spotify spotify = new Spotify();

        List<Artist> artists = new ArrayList<Artist>();
        artists.add(new Artist("image.png", "Osama"));
        artists.add(new Artist("image.png", "Shooq"));
        artists.add(new Artist("image.png", "Dona"));
        adapter = new AtristListAdapter(this, artists);
        mlistView.setAdapter(adapter);

        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                already_queried = false;
                MainActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                last_text_edit = System.currentTimeMillis();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(System.currentTimeMillis() > (last_text_edit + idle_min - 500)){
                            // user hasn't changed the EditText for longer than
                            // the min delay (with half second buffer window)
                            if (!already_queried) { // don't do this stuff twice.
                                already_queried = true;
                                mProgressBar.setVisibility(View.VISIBLE);
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mProgressBar.setVisibility(View.INVISIBLE);
                                mlistView.setVisibility(View.VISIBLE);
                                Log.d("Time Elapsed", "User stopped typing");  // your queries
                            }
                        }
                    }
                }, idle_min);
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
