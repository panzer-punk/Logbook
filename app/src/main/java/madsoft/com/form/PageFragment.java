package madsoft.com.form;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.LinkedList;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment implements ArticleRecyclerViewAdapter.onClickListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    protected LinkedList<Article> articles;
    private RecyclerView recyclerView;
    private ArticleRecyclerViewAdapter articleRecyclerViewAdapter;



    static PageFragment newInstance() {
        PageFragment pageFragment = new PageFragment();
        return pageFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pages, null);



        articleRecyclerViewAdapter = new ArticleRecyclerViewAdapter(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        int orientation = getResources().getConfiguration().orientation;
        if ( orientation == Configuration.ORIENTATION_LANDSCAPE) {
           recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        recyclerView.setAdapter(articleRecyclerViewAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                download();

            }
        });


        download();

        return view;
    }

    private void download(){

        swipeRefreshLayout.setRefreshing(true);
        new DownloadTask().execute();

    }


    @Override
    public void onItemClick(int position) {
        Article article = articleRecyclerViewAdapter.getItem(position);


        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.putExtra(Article.LINK, article.link);
        intent.putExtra(Article.TITLE, article.title);
        startActivity(intent);

    }


    public class DownloadTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected  Boolean doInBackground(String ... arg){

            try {

                Document doc = Jsoup.connect(Assets.PATH).get();
                articles = Parser.articleAdapter(doc);

                return true;

            } catch (Exception exp) {
                exp.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean downloaded){



            if(downloaded) {
                articleRecyclerViewAdapter.clear();
                articleRecyclerViewAdapter.setItems(articles);
                swipeRefreshLayout.setRefreshing(false);

            }

        }

    }
}
