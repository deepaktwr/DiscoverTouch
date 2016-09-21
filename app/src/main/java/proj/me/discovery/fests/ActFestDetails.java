package proj.me.discovery.fests;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.ArrayList;

import proj.me.discovery.R;
import proj.me.discovery.Utils;
import proj.me.discovery.views.CustomTextView;

/**
 * Created by root on 19/12/15.
 */
public class ActFestDetails extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnTouchListener, ActivityCallback{
    private ImageView headerImage1, headerImage2;

    private RelativeLayout cardDetailParent;
    private Toolbar toolbar;
    private float cardTopMargin;
    private float densityDpi, atImageAlphaPx=1f;
    private ArrayList<BeanFestivals> beanFestivalsArrayList;
    private final static String path = "http://services-node12345js.rhcloud.com/uploads/";
    private int heightPixels;
    private int toolbarHeight;
    private NestedScrollView nestedScrollView;
    float currentOffsetAlpha;

    private View fakeViewEnter, fakeViewExit, currentCard;

    private float leftRightMargin;
    private float fakeViewMarginTopUp, fakeViewMarginTopDown;
    private float imageHeight;

    private FakeActions upFakeAction, downFakeAction;
    private View baseView;
    private DecimalFormat df;

    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        df = new DecimalFormat("#.#");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        heightPixels = displayMetrics.heightPixels;
        setContentView(R.layout.fest_details);
        baseView = findViewById(R.id.base_view);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll);
        nestedScrollView.setSmoothScrollingEnabled(true);

        currentCard = findViewById(R.id.current_card);
        fakeViewEnter = findViewById(R.id.fake_detail_card);
        fakeViewExit = findViewById(R.id.fake_detail_card_holder);

        densityDpi = displayMetrics.densityDpi;
        //app:behavior_overlapTop
        cardTopMargin = 64f*(densityDpi / 160f);
        leftRightMargin = 20f *(densityDpi / 160f);

        Bundle bundle = getIntent().getExtras();

        beanFestivalsArrayList = bundle.getParcelableArrayList("bean_detail_list");
        //total toolbar height
        imageHeight = (float)displayMetrics.widthPixels * 1.2f;
        initialize((float)displayMetrics.widthPixels * 1.2f);
        baseView.setOnTouchListener(this);
        nestedScrollView.setOnTouchListener(this);

        currentPosition = 0;

        initializeCurrentCard(currentPosition);
        initializeExitCard(currentPosition);
        initializeEnterCard(currentPosition);
    }

    void initializeCurrentCard(int currentPosition){
        if(currentPosition >= beanFestivalsArrayList.size() || currentPosition < 0)return;
        BeanFestivals beanFestivals = beanFestivalsArrayList.get(currentPosition);
        CustomTextView cardFestName = (CustomTextView)currentCard.findViewById(R.id.card_fest_name);
        CustomTextView cardTextDetailText = (CustomTextView)currentCard.findViewById(R.id.card_fest_detail_text);
        cardFestName.setText(beanFestivals.getFestName()+" "+currentPosition);
        String eventText = getResources().getString(R.string.event);
        String festPlace = beanFestivals.getFestPlace();
        cardTextDetailText.setText(String.format(eventText, beanFestivals.getFestName(),
                festPlace.substring(0, festPlace.indexOf(",")), festPlace.substring(festPlace.indexOf(",") + 2)));

        final ImageView like =(ImageView)currentCard.findViewById(R.id.like);
        final CustomTextView likeCount = (CustomTextView)currentCard.findViewById(R.id.like_count);

        if(beanFestivals.isDoesLike())
            like.setImageResource(R.drawable.fill_heart);
        else
            like.setImageResource(R.drawable.heart3);
        int count = beanFestivals.getLikeCount();
        float k = (float)count/1000f;
        likeCount.setText(k >= 1f ?df.format(k)+"k":""+count);
    }

    void initializeExitCard(int currentPosition){
        View fakeExit;
        switch(collapsingState){
            case EXPENDED:
                fakeExit = fakeViewExit;
                break;
            case COLLAPSED:
                fakeExit = fakeViewEnter;
                break;
            default:
                return;
        }

        if(currentPosition >= beanFestivalsArrayList.size() || currentPosition < 0)return;
        BeanFestivals beanFestivals = beanFestivalsArrayList.get(currentPosition);
        CustomTextView cardFestName = (CustomTextView)fakeExit.findViewById(R.id.card_fest_name);
        CustomTextView cardTextDetailText = (CustomTextView)fakeExit.findViewById(R.id.card_fest_detail_text);
        cardFestName.setText(beanFestivals.getFestName()+" "+currentPosition);
        String eventText = getResources().getString(R.string.event);
        String festPlace = beanFestivals.getFestPlace();
        cardTextDetailText.setText(String.format(eventText, beanFestivals.getFestName(),
                festPlace.substring(0, festPlace.indexOf(",")), festPlace.substring(festPlace.indexOf(",") + 2)));

        final ImageView like =(ImageView)fakeExit.findViewById(R.id.like);
        final CustomTextView likeCount = (CustomTextView)fakeExit.findViewById(R.id.like_count);

        if(beanFestivals.isDoesLike())
            like.setImageResource(R.drawable.fill_heart);
        else
            like.setImageResource(R.drawable.heart3);
        int count = beanFestivals.getLikeCount();
        float k = (float)count/1000f;
        likeCount.setText(k >= 1f ?df.format(k)+"k":""+count);
    }

    void initializeEnterCard(int currentPosition){
        View fakeEnter;
        int position = currentPosition;
        switch(collapsingState){
            case COLLAPSED:
                position += 1;
                fakeEnter = fakeViewExit;
                break;
            case EXPENDED:
                position -= 1;
                fakeEnter = fakeViewEnter;
                break;
            default:
                return;
        }
        if(position >= beanFestivalsArrayList.size() || position < 0){
            fakeEnter.setVisibility(View.INVISIBLE);
            return;
        }/*else fakeEnter.setVisibility(View.VISIBLE);*/

        BeanFestivals beanFestivals = beanFestivalsArrayList.get(position);
        CustomTextView cardFestName = (CustomTextView)fakeEnter.findViewById(R.id.card_fest_name);
        CustomTextView cardTextDetailText = (CustomTextView)fakeEnter.findViewById(R.id.card_fest_detail_text);
        cardFestName.setText(beanFestivals.getFestName()+" "+position);
        String eventText = getResources().getString(R.string.event);
        String festPlace = beanFestivals.getFestPlace();
        cardTextDetailText.setText(String.format(eventText, beanFestivals.getFestName(),
                festPlace.substring(0, festPlace.indexOf(",")), festPlace.substring(festPlace.indexOf(",") + 2)));

        final ImageView like =(ImageView)fakeEnter.findViewById(R.id.like);
        final CustomTextView likeCount = (CustomTextView)fakeEnter.findViewById(R.id.like_count);

        if(beanFestivals.isDoesLike())
            like.setImageResource(R.drawable.fill_heart);
        else
            like.setImageResource(R.drawable.heart3);
        int count = beanFestivals.getLikeCount();
        float k = (float)count/1000f;
        likeCount.setText(k >= 1f ?df.format(k)+"k":""+count);
    }

    private void initialize(float imageHeight){
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_parent);
        cardDetailParent = (RelativeLayout)findViewById(R.id.card_detail_parent);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.app_bar_collapse);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        observeHeight();

        appBarLayout.addOnOffsetChangedListener(this);
        ViewGroup.LayoutParams layoutParams = appBarLayout.getLayoutParams();
        layoutParams.height = Math.round(imageHeight);
        appBarLayout.setLayoutParams(layoutParams);

        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle("");
        headerImage1 = (ImageView)findViewById(R.id.header_1);
        headerImage2 = (ImageView)findViewById(R.id.header_2);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        loadImage();
    }

    private void observeHeight(){
        final ViewTreeObserver obs = cardDetailParent.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
                        layoutParams.height = heightPixels - cardDetailParent.getHeight() + Math.round(cardTopMargin);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.requestLayout();
                        toolbarHeight = layoutParams.height;
                        fakeViewMarginTopUp = heightPixels - cardDetailParent.getHeight() - Math.round(cardTopMargin) + 10;
                        fakeViewMarginTopDown = imageHeight - cardTopMargin;

                        final float appBarPx = toolbarHeight * (densityDpi / 160f);
                        atImageAlphaPx = appBarPx / 1.7f;

                        upFakeAction = new UpFakeAction(fakeViewEnter, fakeViewExit, leftRightMargin, fakeViewMarginTopUp, ActFestDetails.this);
                        upFakeAction.reset();

                        downFakeAction = new DownFakeAction(fakeViewEnter, fakeViewExit, leftRightMargin, fakeViewMarginTopDown, ActFestDetails.this);
                        downFakeAction.reset();
                    }
                });
                cardDetailParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_dicovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
        return;
    }

    CollapsingState collapsingState = CollapsingState.EXPENDED;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        currentOffsetAlpha = 1f - (float) (i * -1) / atImageAlphaPx;
        currentOffsetAlpha = currentOffsetAlpha < 0 ? 0 : currentOffsetAlpha;
        headerImage1.setAlpha(currentOffsetAlpha - 0.04f);
        headerImage2.setAlpha(currentOffsetAlpha - 0.04f);
        if(appBarLayout.getTotalScrollRange() == Math.abs(i)){
            collapsingState = CollapsingState.COLLAPSED;
            initializeEnterCard(currentPosition);
            initializeExitCard(currentPosition);
        } else if(i == 0){
            collapsingState = CollapsingState.EXPENDED;
            initializeEnterCard(currentPosition);
            initializeExitCard(currentPosition);
        } else collapsingState = CollapsingState.IDEAL;
    }

    private float oldY;
    private float lastYVelocity;
    private VelocityTracker velocityTracker;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(view.getId()){
            case R.id.base_view:
                return baseViewTouch(view, motionEvent);
            case R.id.nested_scroll:
                return nestScrollTouch(view, motionEvent);
        }
        return false;
    }

    boolean baseViewTouch(View view, MotionEvent motionEvent){
        if(!upFakeAction.isAnimationRunning() && !downFakeAction.isAnimationRunning()) {
            nestedScrollView.dispatchTouchEvent(motionEvent);
            return false;
        }
        performAction(motionEvent);
        return true;
    }


    boolean nestScrollTouch(View view, MotionEvent motionEvent){
        if(!upFakeAction.isAnimationRunning() && !downFakeAction.isAnimationRunning()) view.onTouchEvent(motionEvent);
        performAction(motionEvent);
        return true;
    }

    void performAction(MotionEvent motionEvent){
        int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldY = motionEvent.getY();
                if(velocityTracker == null) velocityTracker = VelocityTracker.obtain();
                else velocityTracker.clear();
                velocityTracker.addMovement(motionEvent);
                lastYVelocity = 0f;
                if(upFakeAction.isAnimationRunning()) upFakeAction.stopAnimation();
                if(downFakeAction.isAnimationRunning()) downFakeAction.stopAnimation();
                /*upFakeAction.reset();
                downFakeAction.reset();*/
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                lastYVelocity = 0f;
                velocityTracker = null;
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.recycle();
                velocityTracker = null;
                /*nestedScrollView.setVisibility(View.VISIBLE);
                upFakeAction.reset();
                downFakeAction.reset();*/

                if((currentPosition == 0 && collapsingState == CollapsingState.EXPENDED)
                        || (currentPosition == beanFestivalsArrayList.size() - 1 && collapsingState == CollapsingState.COLLAPSED)) {
                    if (upFakeAction.isAnimationRunning()) {
                        upFakeAction.completeAction(FakeActions.FakeAction.EXIT);
                    } else if (downFakeAction.isAnimationRunning()) {
                        downFakeAction.completeAction(FakeActions.FakeAction.EXIT);
                    }
                } else{
                    if (upFakeAction.isAnimationRunning()) {
                        if (Math.abs(lastYVelocity) < 800) {
                            upFakeAction.completeAction(upFakeAction.getExitAlpha() < 0.5
                                    ? FakeActions.FakeAction.ENTER
                                    : FakeActions.FakeAction.EXIT);
                        } else {
                            upFakeAction.accelarateAction(Math.abs(lastYVelocity));
                        }
                    } else if (downFakeAction.isAnimationRunning()) {
                        if (Math.abs(lastYVelocity) < 800) {
                            downFakeAction.completeAction(downFakeAction.getExitAlpha() < 0.5
                                    ? FakeActions.FakeAction.ENTER
                                    : FakeActions.FakeAction.EXIT);
                        } else {
                            downFakeAction.accelarateAction(Math.abs(lastYVelocity));
                        }
                    }
                }
                oldY = 0;
                lastYVelocity = 0f;
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(oldY - motionEvent.getY()) < 10) break;

                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                lastYVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);

                switch(collapsingState){
                    case COLLAPSED:
                        if(currentPosition == beanFestivalsArrayList.size() - 1 && oldY > motionEvent.getY() && upFakeAction.getExitAlpha() < 0.3f){
                            oldY = motionEvent.getY();
                            break;
                        }
                        if(upFakeAction.performFakeView(oldY > motionEvent.getY())){
                            nestedScrollView.setVisibility(View.VISIBLE);
                            upFakeAction.reset();
                        }else if(nestedScrollView.getVisibility() == View.VISIBLE) nestedScrollView.setVisibility(View.INVISIBLE);
                        break;
                    case EXPENDED:
                        if(currentPosition == 0 && oldY < motionEvent.getY() && downFakeAction.getExitAlpha() < 0.3f){
                            oldY = motionEvent.getY();
                            break;
                        }
                        if(downFakeAction.performFakeView(oldY > motionEvent.getY())){
                            nestedScrollView.setVisibility(View.VISIBLE);
                            downFakeAction.reset();
                        }else if(nestedScrollView.getVisibility() == View.VISIBLE) nestedScrollView.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        nestedScrollView.setVisibility(View.VISIBLE);
                        upFakeAction.reset();
                        downFakeAction.reset();
                        break;
                }

                oldY = motionEvent.getY();
                break;
        }
    }

    @Override
    public void performReset(FakeActions.FakeAction fakeAction, boolean shouldOriginalVisible) {
        if(shouldOriginalVisible) nestedScrollView.setVisibility(View.VISIBLE);
        if(fakeAction != FakeActions.FakeAction.ENTER) return;
        switch(collapsingState){
            case COLLAPSED:
                if(currentPosition < beanFestivalsArrayList.size() - 1){
                    currentPosition++;
                    loadImage();
                }
                break;
            case EXPENDED:
                if(currentPosition > 0){
                    currentPosition--;
                    loadImage();
                }
                break;
        }

        initializeCurrentCard(currentPosition);
        initializeExitCard(currentPosition);
        initializeEnterCard(currentPosition);
    }

    @Override
    public void invalidate() {
        nestedScrollView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean isValidEnterAction() {
        return (currentPosition > 1 && collapsingState == CollapsingState.EXPENDED) || (currentPosition < beanFestivalsArrayList.size() - 2 && collapsingState == CollapsingState.COLLAPSED);
    }

    enum CollapsingState{
        COLLAPSED,
        EXPENDED,
        IDEAL
    }

    enum ChangeState{
        IDEAL,
        INCREASE,
        DECREASE
    }

    void loadImage(){
        Picasso.with(this)
                .load(path + beanFestivalsArrayList.get(currentPosition).getImageLink())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        headerImage.setBackground(new BitmapDrawable(ActFestDetails.this.getResources(), bitmap));
                    }
                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {

                    }
                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {

                    }
                });
    }
}
