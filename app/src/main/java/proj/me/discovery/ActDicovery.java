package proj.me.discovery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import proj.me.discovery.fests.ActFestDetails;
import proj.me.discovery.fests.ActFestivalDetail;
import proj.me.discovery.fests.FragFestivals;
import proj.me.discovery.login.ActLaunch;

public class ActDicovery extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCallback, AppBarLayout.OnOffsetChangedListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ImageView headerImage;
    private float atImageAlphaPx;

    public static float imageHeight;

    private float currentOffsetAlpha;

    private boolean isSearching;

    private int[] whiteTrans;

    private FragFestivals fragFestivals;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        imageHeight = (float)displayMetrics.widthPixels * 1.2f;

        setContentView(R.layout.activity_act_dicovery);

        preferences = getSharedPreferences("user", MODE_PRIVATE);

        ((TextView)findViewById(R.id.user_name)).setText(preferences.getString("userName", "User"));

        fragFestivals = (FragFestivals)getFragmentManager().findFragmentById(R.id.fest_frag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.app_bar_collapse);
        collapsingToolbarLayout.setTitle("Festival");

        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_parent);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        headerImage = (ImageView)findViewById(R.id.header);

        whiteTrans = getResources().getIntArray(R.array.trans_whites);

        final float appBarPx = 192f*(displayMetrics.densityDpi / 160f);

        atImageAlphaPx = appBarPx/1.7f;

        appBarLayout.addOnOffsetChangedListener(this);

        makePalletSetting();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void makePalletSetting(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.globe);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setBackgroundColor(palette.getDarkMutedColor(R.attr.colorPrimary));
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_dicovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogin", false);
            editor.apply();

            final Intent i = new Intent(ActDicovery.this, ActLaunch.class);
            startActivity(i);
            finish();

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void isFirstItemVisible(boolean isVisible) {
    }

    @Override
    public void performingSearch(boolean isSearching) {
        this.isSearching = isSearching;
        changeTitleAlpha();
    }

    @Override
    public void callDetailsActivity(Bundle bundle) {
        Intent intent = new Intent(this, ActFestDetails.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 10);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        Utils.logMessage("offset = "+i);
        currentOffsetAlpha = 1f - ((float) (i * -1) / atImageAlphaPx);
        Utils.logMessage("at image alpha px = "+atImageAlphaPx);
        currentOffsetAlpha = currentOffsetAlpha < 0 ? 0 : currentOffsetAlpha;
        changeTitleAlpha();
    }

    private void changeTitleAlpha(){
        Utils.logMessage("current alpha = "+currentOffsetAlpha);
        float alphaVal = 0f;
        if(isSearching){
            alphaVal = currentOffsetAlpha>0.5f?0.5f:currentOffsetAlpha;
            //set appropriate alpha
            float val = currentOffsetAlpha*10f;
            Utils.logMessage("white val = "+val);
            collapsingToolbarLayout.setCollapsedTitleTextColor(whiteTrans[(int) val]);
            collapsingToolbarLayout.setExpandedTitleColor(whiteTrans[(int) val]);

        }else{
            alphaVal = currentOffsetAlpha + 0.08f;
            Utils.logMessage("white val = "+"at length");
            collapsingToolbarLayout.setCollapsedTitleTextColor(whiteTrans[whiteTrans.length-1]);
            collapsingToolbarLayout.setExpandedTitleColor(whiteTrans[whiteTrans.length-1]);
        }
        Utils.logMessage("alpha = "+alphaVal+"\n");
        headerImage.setAlpha(alphaVal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == 10){
            fragFestivals.changeBeanAtPosition(data.getExtras());
        }
    }
}
