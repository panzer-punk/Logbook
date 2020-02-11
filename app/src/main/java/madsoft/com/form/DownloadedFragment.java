package madsoft.com.form;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Даниил on 27.09.2018.
 */

public class DownloadedFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private static  String LIST = "linkTextList";
    private CacheSystem cacheSystem;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> linkTextList;
    private ArrayAdapter<String> adapter;
    private ListView listView;



    static DownloadedFragment newInstance() {
        DownloadedFragment Fragment = new DownloadedFragment();
        return Fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded, null);

        cacheSystem = new CacheSystem(getActivity());

        listView = view.findViewById(R.id.d_list_view);

        linkTextList = new ArrayList<>();


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {

                String linkHref;
                String filename;

                try {

                        linkHref = null;
                        filename = lisView.getItemAtPosition(position).toString();


                    Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
                    intent.putExtra(Assets.CONTENT, linkHref);
                    intent.putExtra(Assets.FILENAME, filename);
                    startActivity(intent);
                }catch (Exception e){ Toast toast = Toast.makeText(getActivity(),
                        e.toString(),
                        Toast.LENGTH_SHORT);
                    toast.show(); }

            }
        };



        listView.setOnItemClickListener(itemClickListener);

        swipeRefreshLayout = view.findViewById(R.id.d_swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                download();

            }
        });

        adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item,
                R.id.pro_item,
                linkTextList);

        download();


        return view;
    }

    private void download(){

        swipeRefreshLayout.setRefreshing(true);
        new loadCahce().execute();

    }


    public class loadCahce extends AsyncTask<String, Void, Boolean> {
        @Override
        protected  Boolean doInBackground(String ... arg){

            linkTextList = cacheSystem.loadListCachedFiles();

            return true;

        }

        @Override
        protected void onPostExecute(Boolean downloaded){

            swipeRefreshLayout.setRefreshing(false);
            adapter.clear();
            adapter.addAll(linkTextList);
            listView.setAdapter(adapter);

        }

    }
}
