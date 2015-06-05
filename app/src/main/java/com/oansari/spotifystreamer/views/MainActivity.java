package com.oansari.spotifystreamer.views;

import android.app.Activity;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {

    long idle_min = 4000; // 4 seconds after user stops typing
    long last_text_edit = 0;
    Handler h = new Handler();
    boolean already_queried = false;
    List<Artist> mArtists = new ArrayList<>();
    AtristListAdapter adapter;

    SpotifyApi mSpotifyApi;
    SpotifyService mSpotifyService;

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
        final Spotify spotify = new Spotify();

        mSpotifyApi = new SpotifyApi();
        //mSpotifyApi.setAccessToken("40b40bec089a4cd6947c626dcd3f5a08");

        mSpotifyService = mSpotifyApi.getService();


        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mlistView.setVisibility(View.INVISIBLE);

                }else {
                    mArtists.clear();
                    updateList();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mlistView.setVisibility(View.VISIBLE);
                }
                already_queried = false;
                //MainActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                last_text_edit = System.currentTimeMillis();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (System.currentTimeMillis() > (last_text_edit + idle_min - 500)) {
                            // user hasn't changed the EditText for longer than
                            // the min delay (with half second buffer window)
                            if (mFilterEditText.getText().toString().length() > 0) {
                                mProgressBar.setVisibility(View.VISIBLE);
                                new FetchSpotifyData().execute();

                                Log.d("Time Elapsed", "User stopped typing");  // your queries
                            }
                        }
                    }
                }, idle_min);
            }
        });



    }

    private void updateList() {
            adapter = new AtristListAdapter(this, mArtists);
            mlistView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
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

    private class FetchSpotifyData extends AsyncTask{

        @Override
        protected Object doInBackground(Object... objects) {
            mSpotifyService.searchArtists(mFilterEditText.getText().toString(), new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    mArtists = artistsPager.artists.items;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateList();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (mArtists.size() > 0)
                                mlistView.setVisibility(View.VISIBLE);
                            else

                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    //TODO Handle Failure
                }
            });
            return null;
        }
    }
}
