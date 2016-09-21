package proj.me.discovery.login;

/**
 * Created by root on 23/12/15.
 */
public interface FragmentCallback {
    void performAction();
    void requestFailed(String errorMessage);
    void onBackNavigation();
}
