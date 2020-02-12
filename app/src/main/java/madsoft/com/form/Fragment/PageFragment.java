package madsoft.com.form.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Network.Objects.Article;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Assets;
import madsoft.com.form.Network.Html.Parser;
import madsoft.com.form.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.LinkedList;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment implements ArticleRecyclerViewAdapter.onClickListener {

    final static String BITMAP = "BITMAP";

    private SwipeRefreshLayout swipeRefreshLayout;
    protected LinkedList<Article> articles;
    private RecyclerView recyclerView;
    private ArticleRecyclerViewAdapter articleRecyclerViewAdapter;



    public static PageFragment newInstance() {
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

       /* imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        Intent intent = new Intent(this, NewActivity.class);
        intent.putExtra("BitmapImage", bitmap);*/

        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.putExtra(Article.LINK, article.getLink());
        intent.putExtra(Article.TITLE, article.getTitle());
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
