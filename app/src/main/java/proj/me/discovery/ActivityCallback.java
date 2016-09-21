package proj.me.discovery;

import android.os.Bundle;

import proj.me.discovery.fests.BeanFestivals;

/**
 * Created by root on 19/12/15.
 */
public interface ActivityCallback {
    void isFirstItemVisible(boolean isVisible);
    void performingSearch(boolean isSearching);
    void callDetailsActivity(Bundle bundle);
}
