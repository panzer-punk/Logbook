package madsoft.com.form.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import madsoft.com.form.FileSystem.CacheSystem;
import madsoft.com.form.Network.Objects.Category;
import madsoft.com.form.R;

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

public class SearchFragment extends Fragment implements Filterable{

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private static String LIST = "linkTextList";
    private static SearchFragment instance;
    private CacheSystem cacheSystem;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> linkTextList;
    private ArrayAdapter<String> adapter;
    private ListView listView;


    public static SearchFragment newInstance() {
        if(instance == null)
            instance = new SearchFragment();
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        return view;
    }

    @Override
    public void applyFilter(Category category) {

    }
}
