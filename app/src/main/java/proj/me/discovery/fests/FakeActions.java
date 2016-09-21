package proj.me.discovery.fests;

import android.os.Handler;
import android.view.View;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

import proj.me.discovery.Utils;

/**
 * Created by deepak on 29/7/16.
 */
public abstract class FakeActions {

    protected View fakeViewEnter, fakeViewExit;
    protected float alphaFakeEnter, alphaFakeExit;
    protected boolean isUpEvent;
    protected boolean isAnimationRunning, atEdgePoint;
    protected FakeAction lastFakeAction;
    protected Handler fakeHandler;
    private MyRunnable myRunnable;
    ActivityCallback activityCallback;
    boolean shouldAccelarate;

    protected float alphaChangeRatio = 0.05f;
    protected float heightChangeRatio = 12;

    float accelarateAmount;

    public FakeActions(View fakeViewEnter, View fakeViewExit, boolean isUpEvent, ActivityCallback activityCallback){
        this.fakeViewEnter = fakeViewEnter;
        this.fakeViewExit = fakeViewExit;
        this.isUpEvent = isUpEvent;
        this.activityCallback = activityCallback;
        fakeHandler = new Handler();
        myRunnable = new MyRunnable(this);
    }
    protected abstract void performFakeViewIn(boolean hasNextStep);
    protected abstract void performFakeViewOut();

    float imageAlpha = 1f;

    void notifyAlpha(boolean isInEvent, ActFestDetails.ChangeState imageChangeState){
        if(isInEvent){
            //alphafake exit 1 - 0
            //alphafake enter 0 - 1

            //imageChangeState -- increase or decrease
        }else{
            //alphafake exit 0 - 1
            //alphafake enter 1 - 0

            //imageChangeState -- increase or decrease
        }
    }

    void completeAction(FakeAction fakeAction){
        shouldAccelarate = false;
        Utils.logError("alpha "+alphaFakeExit);
        if(atEdgePoint){
            fakeHandler.removeCallbacks(myRunnable);
            //make nested scroll visible
            activityCallback.performReset(fakeAction, true);
            reset();
        }else {
            switch (fakeAction){
                case ENTER:
                    //perform up anim
                    performFakeViewIn(false);
                    break;
                case EXIT:
                    //perform down anim
                    performFakeViewOut();
                    break;
            }
            fakeHandler.postDelayed(myRunnable, 10);
        }
    }

    public void stopAnimation(){
        fakeHandler.removeCallbacks(myRunnable);
        resetAccelaration();
    }

    double getRoundValue(double val){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(val));
    }

    void accelarateAction(float accelarateAmount){
        this.accelarateAmount = accelarateAmount;
        //1k to 10k accelaration
        if(!shouldAccelarate) {
            float accelaration = accelarateAmount / 10000f;
            accelaration = accelaration < 0.4f ? 0.4f : accelaration > 0.9f ? 0.9f : accelaration;
            accelaration += 0.4f;

            alphaChangeRatio *= accelaration * 2.9f;
            heightChangeRatio *= accelaration * 2.9f;
            Utils.logError("acce = "+accelaration+" acc am = "+accelarateAmount);
        }

        shouldAccelarate = true;




        boolean hasNextStep = accelarateAmount > 3000 && activityCallback.isValidEnterAction();
        if(atEdgePoint){
            fakeHandler.removeCallbacks(myRunnable);
            //make nested scroll visible
            reset();

            if(hasNextStep){
                isAnimationRunning = true;
                fakeViewEnter.bringToFront();

                fakeViewExit.setFocusable(false);
                fakeViewEnter.setFocusable(true);

                fakeViewExit.setVisibility(View.VISIBLE);
                fakeViewEnter.setVisibility(View.VISIBLE);

                activityCallback.performReset(lastFakeAction, false);

                accelarateAction(accelarateAmount - 2000f);


            } else activityCallback.performReset(lastFakeAction, true);
        }else {
            switch (lastFakeAction){
                case ENTER:
                    //perform up anim
                    performFakeViewIn(hasNextStep);
                    break;
                case EXIT:
                    //perform down anim
                    performFakeViewOut();
                    break;
            }
            if(alphaChangeRatio > 0.04f) {
                alphaChangeRatio /= 1.1f;
                heightChangeRatio /= 1.1f;
            }else{
                alphaChangeRatio = 0.04f;
                heightChangeRatio = 9.6f;
            }

            fakeHandler.postDelayed(myRunnable, 10);
        }
    }

    public void reset(){
        resetAccelaration();
        alphaFakeExit = 1f;
        alphaFakeEnter = 0f;

        fakeViewEnter.setVisibility(View.INVISIBLE);
        fakeViewExit.setVisibility(View.INVISIBLE);

        fakeViewExit.setAlpha(1f);
        fakeViewEnter.setAlpha(0f);

        isAnimationRunning = false;
        atEdgePoint = false;

        fakeHandler.removeCallbacks(myRunnable);
    }

    public void resetAccelaration(){
        alphaChangeRatio = 0.05f;
        heightChangeRatio = 12f;

        shouldAccelarate = false;
    }

    public boolean performFakeView(boolean isScrollUp){
        if(atEdgePoint){
            Utils.logError("at edge");
            fakeViewEnter.setVisibility(View.INVISIBLE);
            fakeViewExit.setVisibility(View.INVISIBLE);
            isAnimationRunning = false;
            //reset will be later
            activityCallback.performReset(lastFakeAction, true);
            return true;
        }else{
            Utils.logError("not at edge");
            isAnimationRunning = true;
            fakeViewEnter.bringToFront();

            fakeViewExit.setFocusable(false);
            fakeViewEnter.setFocusable(true);

            fakeViewExit.setVisibility(View.VISIBLE);
            fakeViewEnter.setVisibility(View.VISIBLE);
        }

        if(isUpEvent){
            if(isScrollUp){
                Utils.logError("up event in");
                performFakeViewIn(false);
            }
            else{
                Utils.logError("up event out");
                performFakeViewOut();
            }
        }else {
            if (isScrollUp){
                Utils.logError("down event out");
                performFakeViewOut();
            }
            else{
                Utils.logError("down event in");
                performFakeViewIn(false);
            }
        }

        return false;
    }

    public boolean isAnimationRunning(){
        return isAnimationRunning;
    }

    private static class MyRunnable implements Runnable{
        WeakReference<FakeActions> fakeActionWeakReference;
        MyRunnable(FakeActions fakeAction){
            fakeActionWeakReference = new WeakReference<>(fakeAction);
        }
        @Override
        public void run() {
            FakeActions fakeAction = fakeActionWeakReference.get();
            if(fakeAction == null) return;
            if(fakeAction.shouldAccelarate) fakeAction.accelarateAction(fakeAction.accelarateAmount);
            else fakeAction.completeAction(fakeAction.lastFakeAction);
        }
    }

    public float getExitAlpha(){
        return alphaFakeExit;
    }

    protected enum FakeAction{
        ENTER,
        EXIT
    }
}
