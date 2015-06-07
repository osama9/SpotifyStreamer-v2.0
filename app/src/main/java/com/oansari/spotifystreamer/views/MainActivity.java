package com.oansari.spotifystreamer.views;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.oansari.spotifystreamer.R;

import kaaes.spotify.webapi.android.models.Artist;

public class MainActivity extends Activity implements ArtistListFragment.OnArtistListFragmentInteractionListener, TopTracksFragment.OnTopTracksFragmentInteractionListener {

    ArtistListFragment artistListFragment;
    String ARTISTS_LIST_TAG = "ArtistsListFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistListFragment = ArtistListFragment.newInstance();
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment, artistListFragment, ARTISTS_LIST_TAG)
                .addToBackStack(ARTISTS_LIST_TAG)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //manageActionBar();
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void manageActionBar() {
        String str=getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-2).getName();
        Fragment fragment=getFragmentManager().findFragmentByTag(str);
        if(fragment.getClass().getName() == ArtistListFragment.newInstance().getClass().getName())
            getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        //manageActionBar();
        super.onBackPressed();


    }

    @Override
    public void OnArtistListFragmentInteractionListener(Artist artist) {
        TopTracksFragment topTracksFragment = TopTracksFragment.newInstance(artist.id);
        getFragmentManager().beginTransaction()
                .hide(artistListFragment)
                .add(R.id.fragment, topTracksFragment)
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
