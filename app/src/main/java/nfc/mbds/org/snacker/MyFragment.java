package nfc.mbds.org.snacker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by user on 11/09/17.
 */

public abstract class MyFragment extends Fragment {

    public void show(boolean shouldBeShown) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if(shouldBeShown) {
            ft.show(this).commit();
            Log.d(this.getClass().getSimpleName()," should be shown");
        } else {
            ft.hide(this).commit();
        }

    }
}
