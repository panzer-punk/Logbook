package madsoft.com.form;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> linkTextList;
    private ArrayAdapter<String> adapter;
    private ListView listView;



    static PageFragment newInstance() {
        PageFragment pageFragment = new PageFragment();
        return pageFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pages, null);

        listView = view.findViewById(R.id.list_view);

        linkTextList = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {

                String linkHref = "";
                String filename = "";

                try {

                    if(Assets.LNKS != null) {
                        Document doc = Jsoup.parse(Assets.LNKS.getLink((lisView.getItemAtPosition(position).toString())).outerHtml());
                        Element link = doc.select("a").first();
                        linkHref = link.attr("href");
                        filename = lisView.getItemAtPosition(position).toString();
                    }

                    Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
                    intent.putExtra(Assets.CONTENT, linkHref);
                    intent.putExtra(Assets.FILENAME, filename);
                    startActivity(intent);
                }catch (Exception e){ e.printStackTrace();}

            }
        };

        listView.setOnItemClickListener(itemClickListener);





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

        adapter.clear();

        swipeRefreshLayout.setRefreshing(true);
        new DownloadTask().execute();

    }

    private void setAdapter( ArrayAdapter<String> adapter){

        listView.setAdapter(adapter);

    }


    public class DownloadTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected  Boolean doInBackground(String ... arg){

            try {

                Document doc = Jsoup.connect(Assets.PATH).get();
                links = doc.select("a[href]");
                linkTextList.clear();

                for (Element link : links)
                    if (link.toString().contains("formul") && link.toString().contains(".html"))
                        linkTextList.add(link.text());

                Iterator<Element> iterator = links.iterator();

                while (iterator.hasNext()) {

                    Element el = iterator.next();

                    if (!el.toString().contains(".html"))
                        iterator.remove();
                }


                return true;

            } catch (Exception exp) {
                exp.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean downloaded){



            if(downloaded) {
                    Assets.LNKS = new LinksMap(links);

                adapter.addAll(linkTextList);
                setAdapter(adapter);

                swipeRefreshLayout.setRefreshing(false);

            }

        }

    }
}
