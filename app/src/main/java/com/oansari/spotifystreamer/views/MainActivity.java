package com.oansari.spotifystreamer.views;

import android.app.Activity;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

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


public class MainActivity extends Activity implements ArtistListFragment.OnFragmentInteractionListener, TopTracksFragment.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArtistListFragment artistListFragment = new ArtistListFragment();
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, artistListFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onFragmentInteraction(int position) {
        TopTracksFragment topTracksFragment = TopTracksFragment.newInstance("","");
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, topTracksFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
