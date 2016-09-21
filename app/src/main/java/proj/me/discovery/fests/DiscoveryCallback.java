package proj.me.discovery.fests;

/**
 * Created by root on 18/12/15.
 */
public interface DiscoveryCallback {
    void performTransition(BeanFestivals beanFestivals, int position);

    void queryTextChange(String text);
}
