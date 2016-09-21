package proj.me.discovery.fests;

import android.view.View;
import android.view.ViewGroup;

import proj.me.discovery.Utils;

/**
 * Created by deepak on 27/7/16.
 */
public class DownFakeAction extends FakeActions{
    float leftRightMargin;
    float marginWidthEnter, marginWidthExit;

    float topMarginEnter, marginHeightEnter;
    float topMarginExit, marginHeightExit;

    ViewGroup.MarginLayoutParams paramsFakeEnter, paramsFakeExit;

    float widthIncreaseRatio;

    public DownFakeAction(View fakeViewEnter, View fakeViewExit, float leftRightMargin, float topMarginExit, ActivityCallback activityCallback){
        super(fakeViewEnter, fakeViewExit, false, activityCallback);
        this.fakeViewEnter = fakeViewEnter;
        this.fakeViewExit = fakeViewExit;
        this.leftRightMargin = leftRightMargin;
        this.topMarginExit = topMarginExit;

        float decreaseRatio = 1f / alphaChangeRatio;
        topMarginEnter = topMarginExit - decreaseRatio * heightChangeRatio;

        widthIncreaseRatio = leftRightMargin / decreaseRatio;

        paramsFakeEnter = (ViewGroup.MarginLayoutParams) fakeViewEnter.getLayoutParams();
        paramsFakeExit = (ViewGroup.MarginLayoutParams) fakeViewExit.getLayoutParams();
    }

    public void reset(){
        super.reset();
        marginHeightEnter = topMarginEnter;
        marginHeightExit = topMarginExit;

        marginWidthEnter = /*0*/leftRightMargin;
        marginWidthExit = leftRightMargin;
    }

    @Override
    protected void performFakeViewIn(boolean hasNextStep){
        lastFakeAction = FakeAction.ENTER;
        paramsFakeEnter.setMargins(Math.round(marginWidthEnter), Math.round(marginHeightEnter), Math.round(marginWidthEnter), 0);
        fakeViewEnter.setLayoutParams(paramsFakeEnter);
        fakeViewEnter.invalidate();

        paramsFakeExit.setMargins(Math.round(marginWidthExit), Math.round(marginHeightExit), Math.round(marginWidthExit), 0);
        fakeViewExit.setLayoutParams(paramsFakeExit);
        fakeViewExit.invalidate();

        if(alphaFakeExit < alphaChangeRatio) alphaFakeExit = 0f;
        else alphaFakeExit -= alphaChangeRatio;
        alphaFakeEnter += alphaChangeRatio;

        if(!hasNextStep || alphaFakeEnter < 0.6f)
            fakeViewEnter.setAlpha(alphaFakeEnter);
        if(!hasNextStep || alphaFakeExit < 0.6f)
            fakeViewExit.setAlpha(alphaFakeExit);
        else fakeViewExit.setAlpha(0.6f);

        marginHeightEnter += heightChangeRatio;
        marginHeightExit += heightChangeRatio;

        /*marginWidthEnter += widthIncreaseRatio;*/
        /*marginWidthExit += widthIncreaseRatio;*/

        if(alphaFakeExit == 0f){
            Utils.logError("edge at true down in");
            atEdgePoint = true;
        }
        notifyAlpha(true, ActFestDetails.ChangeState.DECREASE);
    }

    @Override
    protected void performFakeViewOut() {
        lastFakeAction = FakeAction.EXIT;
        paramsFakeEnter.setMargins(Math.round(marginWidthEnter), Math.round(marginHeightEnter), Math.round(marginWidthEnter), 0);
        fakeViewEnter.setLayoutParams(paramsFakeEnter);
        fakeViewEnter.invalidate();

        paramsFakeExit.setMargins(Math.round(marginWidthExit), Math.round(marginHeightExit), Math.round(marginWidthExit), 0);
        fakeViewExit.setLayoutParams(paramsFakeExit);
        fakeViewExit.invalidate();

        if(alphaFakeExit + alphaChangeRatio > 1) alphaFakeExit = 1f;
        else alphaFakeExit += alphaChangeRatio;
        alphaFakeEnter -= alphaChangeRatio;

        fakeViewEnter.setAlpha(alphaFakeEnter);
        fakeViewExit.setAlpha(alphaFakeExit);

        marginHeightEnter -= heightChangeRatio;
        marginHeightExit -= heightChangeRatio;

        /*marginWidthEnter -= widthIncreaseRatio;*/
        /*marginWidthExit -= widthIncreaseRatio;*/

        if(alphaFakeExit == 1f){
            Utils.logError("edge at true down out");
            atEdgePoint = true;
        }
        notifyAlpha(false, ActFestDetails.ChangeState.INCREASE);
    }
}
