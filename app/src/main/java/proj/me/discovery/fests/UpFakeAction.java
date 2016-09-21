package proj.me.discovery.fests;

import android.view.View;
import android.view.ViewGroup;

import proj.me.discovery.Utils;

/**
 * Created by deepak on 27/7/16.
 */
public class UpFakeAction extends FakeActions{
    float leftRightMargin;
    float marginWidthExit, marginWidthEnter;

    float topMarginExit, marginHeightExit;
    float topMarginEnter, marginHeightEnter;

    ViewGroup.MarginLayoutParams paramsFakeExit, paramsFakeEnter;

    float widthIncreaseRatio;

    public UpFakeAction(View fakeViewEnter, View fakeViewExit, float leftRightMargin, float topMarginExit, ActivityCallback activityCallback){
        super(fakeViewExit, fakeViewEnter, true, activityCallback);
        this.leftRightMargin = leftRightMargin;
        this.topMarginExit = topMarginExit;

        float decreaseRatio = 1f / alphaChangeRatio;
        topMarginEnter = topMarginExit + decreaseRatio * heightChangeRatio;


        widthIncreaseRatio = leftRightMargin / decreaseRatio;

        paramsFakeExit = (ViewGroup.MarginLayoutParams) fakeViewExit.getLayoutParams();
        paramsFakeEnter = (ViewGroup.MarginLayoutParams) fakeViewEnter.getLayoutParams();
    }

    @Override
    public void reset(){
        super.reset();
        marginHeightExit = topMarginExit;
        marginHeightEnter = topMarginEnter;

        marginWidthExit = leftRightMargin;
        marginWidthEnter = /*2 **/ leftRightMargin;
    }

    @Override
    protected void performFakeViewIn(boolean hasNextStep){
        lastFakeAction = FakeAction.ENTER;
        paramsFakeExit.setMargins(Math.round(marginWidthExit), Math.round(marginHeightExit), Math.round(marginWidthExit), 0);
        fakeViewExit.setLayoutParams(paramsFakeExit);
        fakeViewExit.invalidate();

        paramsFakeEnter.setMargins(Math.round(marginWidthEnter), Math.round(marginHeightEnter), Math.round(marginWidthEnter), 0);
        fakeViewEnter.setLayoutParams(paramsFakeEnter);
        fakeViewEnter.invalidate();

        if(alphaFakeExit < alphaChangeRatio) alphaFakeExit = 0f;
        else alphaFakeExit -= alphaChangeRatio;
        alphaFakeEnter += alphaChangeRatio;

        if(!hasNextStep || alphaFakeExit < 0.6f)
            fakeViewExit.setAlpha(alphaFakeExit);
        else fakeViewExit.setAlpha(0.6f);
        if(!hasNextStep || alphaFakeEnter < 0.6f)
            fakeViewEnter.setAlpha(alphaFakeEnter);

        marginHeightExit -= heightChangeRatio;
        marginHeightEnter -= heightChangeRatio;

        /*marginWidthExit -= widthIncreaseRatio;*/
        /*marginWidthEnter -= widthIncreaseRatio;*/
        if(alphaFakeExit == 0f){
            Utils.logError("edge at true up in");
            atEdgePoint = true;
        }
        notifyAlpha(true, ActFestDetails.ChangeState.INCREASE);
    }

    @Override
    protected void performFakeViewOut() {
        lastFakeAction = FakeAction.EXIT;
        paramsFakeExit.setMargins(Math.round(marginWidthExit), Math.round(marginHeightExit), Math.round(marginWidthExit), 0);
        fakeViewExit.setLayoutParams(paramsFakeExit);
        fakeViewExit.invalidate();

        paramsFakeEnter.setMargins(Math.round(marginWidthEnter), Math.round(marginHeightEnter), Math.round(marginWidthEnter), 0);
        fakeViewEnter.setLayoutParams(paramsFakeEnter);
        fakeViewEnter.invalidate();

        if(alphaFakeExit + alphaChangeRatio > 1) alphaFakeExit = 1f;
        else alphaFakeExit += alphaChangeRatio;
        alphaFakeEnter -= alphaChangeRatio;

        fakeViewExit.setAlpha(alphaFakeExit);
        fakeViewEnter.setAlpha(alphaFakeEnter);

        marginHeightExit += heightChangeRatio;
        marginHeightEnter += heightChangeRatio;

        /*marginWidthExit += widthIncreaseRatio;*/
        /*marginWidthEnter += widthIncreaseRatio;*/

        if(alphaFakeExit == 1f){
            Utils.logError("edge at true up out");
            atEdgePoint = true;
        }
        notifyAlpha(false, ActFestDetails.ChangeState.DECREASE);
    }
}
