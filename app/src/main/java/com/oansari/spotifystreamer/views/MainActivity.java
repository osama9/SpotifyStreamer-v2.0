package com.oansari.spotifystreamer.views;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.oansari.spotifystreamer.Helpers.DialogHelper;
import com.oansari.spotifystreamer.R;

import dialogs.PlayerDialogFragment;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class MainActivity extends Activity implements ArtistListFragment.OnArtistListFragmentInteractionListener, TopTracksFragment.OnTopTracksFragmentInteractionListener {

    private boolean mTwoPane;
    ArtistListFragment artistListFragment;
    String ARTISTS_LIST_TAG = "ArtistsListFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artistListFragment = ArtistListFragment.newInstance();
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {


            getFragmentManager().beginTransaction()
                    .add(R.id.fragment, artistListFragment, ARTISTS_LIST_TAG)
                    .addToBackStack(ARTISTS_LIST_TAG)
                    .commit();
        }
        else{
            FragmentManager fm = getFragmentManager();
            artistListFragment = (ArtistListFragment) fm.findFragmentByTag(ARTISTS_LIST_TAG);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.

//            ((ArtistListFragment)  getFragmentManager()
//                    .findFragmentById(R.id.fragment))
//                    .setActivateOnItemClick(true);
        }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if( keyCode== KeyEvent.KEYCODE_BACK)
        {

            ArtistListFragment artistListFragment = (ArtistListFragment)getFragmentManager().findFragmentByTag(ARTISTS_LIST_TAG);
            if (artistListFragment != null && artistListFragment.isVisible())
                finish();
        }

        return super.onKeyDown(keyCode, event);

    }
    @Override
    public void OnArtistListFragmentInteractionListener(Artist artist) {
        TopTracksFragment topTracksFragment = TopTracksFragment.newInstance(artist.id, artist.name);
        if(mTwoPane){
            getFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, topTracksFragment, "TopTracksFragment")
                    .commit();
        }else {
            getFragmentManager().beginTransaction()
                    .hide(artistListFragment)
                    .add(R.id.fragment, topTracksFragment, "TopTracksFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void OnTopTracksFragmentInteractionListener(Fragment topTracksFragment, Track track, int trackPosition) {
        if(mTwoPane)
            DialogHelper.launchPlayerDialog(topTracksFragment, mTwoPane, track, trackPosition);
        else {
            PlayerDialogFragment playerDialogFragment = PlayerDialogFragment.newInstance(mTwoPane, track, trackPosition);
            getFragmentManager().beginTransaction()
                    .hide(topTracksFragment)
                    .add(R.id.fragment, playerDialogFragment, "PlayerDialogFragment")
                    .addToBackStack(null)
                    .commit();

        }
    }


}
