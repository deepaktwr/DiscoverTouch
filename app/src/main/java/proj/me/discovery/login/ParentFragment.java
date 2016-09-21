package proj.me.discovery.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

/**
 * Created by root on 23/12/15.
 */
public abstract class ParentFragment extends Fragment{
    ActivityCallback activityCallback;
    Context context;

    abstract void performAction();
    abstract void requestFailed(String errorMessage);
    void onBackNavigation(){
        activityCallback.callFragment(0);
    }
    void onRegistrationSuccess(){
        activityCallback.callFragment(0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCallback = (ActivityCallback)activity;
        context = activity;
    }
}
