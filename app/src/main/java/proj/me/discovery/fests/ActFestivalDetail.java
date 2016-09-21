package proj.me.discovery.fests;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;

import proj.me.discovery.ActDicovery;
import proj.me.discovery.R;
import proj.me.discovery.views.CustomTextView;

/**
 * Created by root on 19/12/15.
 */
public class ActFestivalDetail extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ImageView headerImage;

    public static float imageHeight;

    private RelativeLayout cardDetailParent;
    private Toolbar toolbar;

    private float cardTopMargin;

    private float currentOffsetAlpha, densityDpi, atImageAlphaPx=1f;
    private DecimalFormat df;

    private BeanFestivals beanFestivals;

    private boolean isBeanModified;
    private int position;
    private Animator likedAnim;

    private final static String path = "http://services-node12345js.rhcloud.com/uploads/";
    private int heightPixels;
    private int toolbarHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            toolbarHeight = savedInstanceState.getInt("toolbarHeight");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        imageHeight = (float)displayMetrics.widthPixels * 1.2f;
        heightPixels = displayMetrics.heightPixels;
        setContentView(R.layout.fest_details);

        densityDpi = displayMetrics.densityDpi;
        cardTopMargin = 64f*(densityDpi / 160f);
        df = new DecimalFormat("#.#");
        Bundle bundle = getIntent().getExtras();

        beanFestivals = bundle.getParcelable("bean_detail");
        position = bundle.getInt("pos");

        initialize();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateCard();
            }
        }, 10);

    }

    private void initialize(){

        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_parent);
        cardDetailParent = (RelativeLayout)findViewById(R.id.card_detail_parent);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.app_bar_collapse);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fillCardDetails();

        if(toolbarHeight != 0){
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            params.height = toolbarHeight;
            toolbar.setLayoutParams(params);
            toolbar.requestLayout();

            final float appBarPx = toolbarHeight * (densityDpi / 160f);
            atImageAlphaPx = appBarPx / 1.7f;
        }else
            observeHeight();


        appBarLayout.addOnOffsetChangedListener(this);
        ViewGroup.LayoutParams layoutParams = appBarLayout.getLayoutParams();
        layoutParams.height = (int)imageHeight;
        appBarLayout.setLayoutParams(layoutParams);


        setSupportActionBar(toolbar);


        collapsingToolbarLayout.setTitle("");

        headerImage = (ImageView)findViewById(R.id.header);


        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);




        //headerImage.setBackground(getResources().getDrawable(beanFestivals.getImageDrawableId()));
        /*Picasso.with(this)
                .load(path+beanFestivals.getImageLink())
                .placeholder(R.drawable.placeholder)
                .into(headerImage);*/

        Picasso.with(this)
                .load(path + beanFestivals.getImageLink())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        headerImage.setBackground(new BitmapDrawable(ActFestivalDetail.this.getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {

                    }
                });


    }

    private void animateCard(){
        Animator cardAnimator = AnimatorInflater.loadAnimator(this, R.anim.card_anim);
        cardAnimator.setTarget(findViewById(R.id.card_view_detail));
        cardAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.card_view_detail).setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                likedAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        cardAnimator.start();
    }

    private void fillCardDetails(){
        likedAnim = AnimatorInflater.loadAnimator(this, R.anim.rotate_liked);

        CustomTextView cardFestName = (CustomTextView)findViewById(R.id.card_fest_name);
        CustomTextView cardTextDetailText = (CustomTextView)findViewById(R.id.card_fest_detail_text);

        cardFestName.setText(beanFestivals.getFestName());
        String eventText = getResources().getString(R.string.event);
        String festPlace = beanFestivals.getFestPlace();
        cardTextDetailText.setText(String.format(eventText, beanFestivals.getFestName(),
                festPlace.substring(0, festPlace.indexOf(",")), festPlace.substring(festPlace.indexOf(",") + 2)));

        final ImageView like =(ImageView)findViewById(R.id.like);
        likedAnim.setTarget(like);
        final CustomTextView likeCount = (CustomTextView)findViewById(R.id.like_count);

        final Animator likeAnim = AnimatorInflater.loadAnimator(this, R.anim.rotate_like);

        if(beanFestivals.isDoesLike())
            like.setImageResource(R.drawable.fill_heart);
        else
            like.setImageResource(R.drawable.heart3);

        int count = beanFestivals.getLikeCount();
        float k = (float)count/1000f;
        likeCount.setText(k >= 1f ?df.format(k)+"k":""+count);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beanFestivals.isDoesLike())
                    likedAnim.start();
                else {
                    beanFestivals.setDoesLike(true);
                    likeAnim.start();
                }
            }
        });



        likeAnim.setTarget(like);

        likeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isBeanModified = true;
                like.setImageResource(R.drawable.fill_heart);
                int count = beanFestivals.getLikeCount()+1;
                float k = (float)count/1000f;
                beanFestivals.setLikeCount(count);
                likeCount.setText(k>=1f?df.format(k)+"k":""+count);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

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
                        layoutParams.height = heightPixels - cardDetailParent.getHeight() + (int) (cardTopMargin * 1.2f);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.requestLayout();
                        toolbarHeight = layoutParams.height;

                        final float appBarPx = toolbarHeight * (densityDpi / 160f);
                        atImageAlphaPx = appBarPx / 1.7f;
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
        if(item.getItemId() != R.id.action_settings){
            if(!isBeanModified){
                setResult(RESULT_CANCELED);
                finish();
                return true;
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("bean_modified", beanFestivals);
            bundle.putInt("pos", position);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(!isBeanModified){
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean_modified", beanFestivals);
        bundle.putInt("pos", position);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("toolbarHeight", toolbarHeight);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        currentOffsetAlpha = 1f - (float) (i * -1) / atImageAlphaPx;
        currentOffsetAlpha = currentOffsetAlpha < 0 ? 0 : currentOffsetAlpha;
        headerImage.setAlpha(currentOffsetAlpha - 0.04f);
    }
}
