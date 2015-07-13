package com.oansari.spotifystreamer.helpers;

import android.app.Fragment;

import com.oansari.spotifystreamer.dialogs.ErrorDialogFragment;
import com.oansari.spotifystreamer.dialogs.PlayerDialogFragment;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Osama on 6/13/2015.
 */
public class DialogHelper {

    public static void alertUserAboutError(Fragment fragment) {
        ErrorDialogFragment dialog = new ErrorDialogFragment();
        dialog.show(fragment.getFragmentManager(),"error_dialog" );
    }

    public static void launchPlayerDialog(Fragment fragment, boolean twoPane, Track track, int trackPosition, Tracks tracks){
        PlayerDialogFragment dialog = PlayerDialogFragment.newInstance(twoPane, track, trackPosition, tracks);
        dialog.show(fragment.getFragmentManager(),"player_dialog" );
    }


}
