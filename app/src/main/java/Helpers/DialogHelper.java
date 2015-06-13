package Helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

import dialogs.ErrorDialogFragment;

/**
 * Created by Osama on 6/13/2015.
 */
public class DialogHelper {

    public static void alertUserAboutError(Fragment fragment) {
        ErrorDialogFragment dialog = new ErrorDialogFragment();
        dialog.show(fragment.getFragmentManager(),"error_dialog" );
    }


}
