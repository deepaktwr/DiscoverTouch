package proj.me.discovery.fests;

/**
 * Created by deepak on 31/7/16.
 */
public interface ActivityCallback {
    void performReset(FakeActions.FakeAction fakeAction, boolean shouldOriginalVisible);
    void invalidate();
    boolean isValidEnterAction();
    //void changeImageAlpha(boolean shouldChangeImage, boolean toNextLevel);
}
