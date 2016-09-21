package proj.me.discovery.views;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import proj.me.discovery.R;
import proj.me.discovery.fests.DiscoveryCallback;

/**
 * Created by root on 19/12/15.
 */
public class FestivalSerch extends SearchView implements SearchView.OnQueryTextListener{

    private DiscoveryCallback discoveryCallback;
    public FestivalSerch(Context context) {
        super(context);
        setOnQueryTextListener(this);
    }

    public FestivalSerch(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnQueryTextListener(this);
    }

    public void setDiscoveryCallback(DiscoveryCallback discoveryCallback){
        this.discoveryCallback = discoveryCallback;
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete)findViewById(R.id.search_src_text);
        theTextArea.setHintTextColor(getResources().getColor(R.color.white_trans3));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(discoveryCallback != null)
            discoveryCallback.queryTextChange(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(discoveryCallback != null)
            discoveryCallback.queryTextChange(newText);
        return true;
    }
}
