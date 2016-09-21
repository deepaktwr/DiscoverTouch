package proj.me.discovery.fests;


import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import proj.me.discovery.ActivityCallback;
import proj.me.discovery.R;
import proj.me.discovery.services.Fest;
import proj.me.discovery.services.FestResponse;
import proj.me.discovery.services.RetrofitImpl;
import proj.me.discovery.views.CustomTextView;
import proj.me.discovery.views.FestivalSerch;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 18/12/15.
 */
public class FragFestivals extends Fragment implements DiscoveryCallback{

    private RecyclerView festList;
    private ArrayList<BeanFestivals> beanFestivalsList;
    private LinearLayoutManager linearLayoutManager;
    private FestsAdapter festsAdapter;

    private ActivityCallback activityCallback;
    private Context context;
    private LinearLayout loaderLayout;
    private RetrofitImpl retrofitImpl;
    private CustomTextView customTextView;

    private ProgressBar progressBar;
    private String loadingText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loadingText = "wait\nwhile we fetch events";
        if(savedInstanceState != null && savedInstanceState.containsKey("fests")) {
            beanFestivalsList = savedInstanceState.getParcelableArrayList("fests");
            apiRunnigStatus = savedInstanceState.getInt("apiRunnigStatus");
            switch(apiRunnigStatus){
                case 2:
                    loadingText = "Reload";
                    break;
                default:
                    //do nothing
                    break;
            }
        }
        else{
            beanFestivalsList = new ArrayList<>();
            //makeBeanList();
        }

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://services-node12345js.rhcloud.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        retrofitImpl = adapter.create(RetrofitImpl.class);


        festsAdapter = new FestsAdapter(beanFestivalsList, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_fests, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("fests", beanFestivalsList);
        outState.putInt("apiRunnigStatus", apiRunnigStatus);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //for setting retaining state of fragment
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderLayout = (LinearLayout)view.findViewById(R.id.loader_layout);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_fest);
        customTextView = (CustomTextView)view.findViewById(R.id.reload_data);
        festList = (RecyclerView)view.findViewById(R.id.fest_list);
        festList.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        festList.setLayoutManager(linearLayoutManager);

        festList.setAdapter(festsAdapter);
        customTextView.setText(loadingText);
        if(apiRunnigStatus == 2) {
            loaderLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            customTextView.setVisibility(View.VISIBLE);
        }else if(beanFestivalsList.size() == 0)
            fetchFestivals();
        else
            loaderLayout.setVisibility(View.INVISIBLE);


            festList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    activityCallback.isFirstItemVisible(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                }
            });

        customTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customTextView.getText().toString().equalsIgnoreCase("reload")) {
                    fetchFestivals();
                }
            }
        });
    }
    private int apiRunnigStatus;

    private void fetchFestivals(){
        loaderLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        customTextView.setVisibility(View.VISIBLE);
        customTextView.setText("wait\nwhile we fetch events");
        retrofitImpl.getAllFestivals(new Callback<FestResponse>() {
            @Override
            public void success(FestResponse festResponse, Response response) {
                apiRunnigStatus = 1;
                loaderLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                customTextView.setVisibility(View.INVISIBLE);
                for(Fest fest :festResponse.getFests().getFests()){
                    BeanFestivals beanFestivals = new BeanFestivals();
                    beanFestivals.setFestName(fest.getFestName());
                    beanFestivals.setFestPlace(fest.getFestPlace());
                    beanFestivals.setImageLink(""+fest.getFestImage());
                    beanFestivals.setLikeCount(Integer.parseInt(fest.getFestLikeCount()));
                    beanFestivals.setDoesLike(Integer.parseInt(fest.getFestIsLiked()) == 1);
                    beanFestivalsList.add(beanFestivals);
                }
                festsAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                apiRunnigStatus = 2;
                loaderLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                customTextView.setText("Reload");
                customTextView.setVisibility(View.VISIBLE);
            }
        });
    }

/*    private void makeBeanList(){
        BeanFestivals beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Low Bond");
        beanFestivals.setFestPlace("Sydney, Australia");
        beanFestivals.setImageDrawableId(R.drawable.fest1);
        beanFestivals.setLikeCount(10);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Holly War");
        beanFestivals.setFestPlace("Bath, England");
        beanFestivals.setImageDrawableId(R.drawable.fest2);
        beanFestivals.setLikeCount(10000);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Bad Land");
        beanFestivals.setFestPlace("Austin, United States");
        beanFestivals.setImageDrawableId(R.drawable.fest3);
        beanFestivals.setLikeCount(30);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Roll Out");
        beanFestivals.setFestPlace("Bern, Switzerland");
        beanFestivals.setImageDrawableId(R.drawable.fest4);
        beanFestivals.setLikeCount(4);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Coming Soon");
        beanFestivals.setFestPlace("Manaus, Brazil");
        beanFestivals.setImageDrawableId(R.drawable.fest5);
        beanFestivals.setLikeCount(35009);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Hard Home");
        beanFestivals.setFestPlace("venice, Italy");
        beanFestivals.setImageDrawableId(R.drawable.fest6);
        beanFestivals.setLikeCount(1000);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Baned Flore");
        beanFestivals.setFestPlace("Chiba, Japan");
        beanFestivals.setImageDrawableId(R.drawable.fest7);
        beanFestivals.setLikeCount(23456);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Dark Angle");
        beanFestivals.setFestPlace("Durban, South Africa");
        beanFestivals.setImageDrawableId(R.drawable.fest8);
        beanFestivals.setLikeCount(200);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Low Sound");
        beanFestivals.setFestPlace("Dresden, Germany");
        beanFestivals.setImageDrawableId(R.drawable.fest9);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Complete Trash");
        beanFestivals.setFestPlace("Makati, Philippines");
        beanFestivals.setImageDrawableId(R.drawable.fest10);
        beanFestivals.setLikeCount(97);
        beanFestivalsList.add(beanFestivals);

        beanFestivals = new BeanFestivals();
        beanFestivals.setFestName("Neutron");
        beanFestivals.setFestPlace("Kazan, Russia");
        beanFestivals.setImageDrawableId(R.drawable.fest11);
        beanFestivals.setLikeCount(15);
        beanFestivalsList.add(beanFestivals);
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        activityCallback = (ActivityCallback)activity;
        context = activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.festival_search, menu);

        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenu = menu.findItem(R.id.fest_search);
        FestivalSerch searchView = (FestivalSerch) searchMenu.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(((Activity) context).getComponentName()));

        MenuItemCompat.setOnActionExpandListener(searchMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                activityCallback.performingSearch(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                activityCallback.performingSearch(false);
                return true;
            }
        });
        searchView.setDiscoveryCallback(this);
    }

    public void changeBeanAtPosition(Bundle bundle){
        BeanFestivals beanFestivals = bundle.getParcelable("bean_modified");
        int position = bundle.getInt("pos");
        beanFestivalsList.set(position, beanFestivals);
        festsAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void performTransition(BeanFestivals beanFestivals, int position) {
        Bundle bundle = new Bundle();
        //bundle.putParcelable("bean_detail", beanFestivals);
        bundle.putParcelableArrayList("bean_detail_list", beanFestivalsList);
        bundle.putInt("pos", position);
        activityCallback.callDetailsActivity(bundle);
    }

    @Override
    public void queryTextChange(String text) {
        Log.e("s", text);
    }
}
