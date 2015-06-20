package com.oansari.spotifystreamer.Helpers;

import android.app.Fragment;

import dialogs.ErrorDialogFragment;
import dialogs.PlayerDialogFragment;

/**
 * Created by Osama on 6/13/2015.
 */
public class DialogHelper {

    public static void alertUserAboutError(Fragment fragment) {
        ErrorDialogFragment dialog = new ErrorDialogFragment();
        dialog.show(fragment.getFragmentManager(),"error_dialog" );
    }

    public static void launchPlayerDialog(Fragment fragment, boolean twoPane){
        PlayerDialogFragment dialog = PlayerDialogFragment.newInstance(twoPane);
        dialog.show(fragment.getFragmentManager(),"player_dialog" );
    }


}
