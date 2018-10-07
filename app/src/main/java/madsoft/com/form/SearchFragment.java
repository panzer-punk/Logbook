package madsoft.com.form;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Даниил on 27.09.2018.
 */

public class SearchFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private static String LIST = "linkTextList";
    private CacheSystem cacheSystem;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> linkTextList;
    private ArrayAdapter<String> adapter;
    private ListView listView;


    static SearchFragment newInstance() {
        SearchFragment Fragment = new SearchFragment();
        return Fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        return view;
    }

}
