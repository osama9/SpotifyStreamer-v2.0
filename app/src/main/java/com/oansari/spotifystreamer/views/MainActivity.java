package com.oansari.spotifystreamer.views;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.oansari.spotifystreamer.R;

import kaaes.spotify.webapi.android.models.Artist;

public class MainActivity extends Activity implements ArtistListFragment.OnArtistListFragmentInteractionListener, TopTracksFragment.OnTopTracksFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArtistListFragment artistListFragment = new ArtistListFragment();
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment, artistListFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void OnArtistListFragmentInteractionListener(Artist artist) {
        TopTracksFragment topTracksFragment = TopTracksFragment.newInstance(artist.id);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, topTracksFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnTopTracksFragmentInteractionListener(Uri uri) {

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
