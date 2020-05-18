package madsoft.com.form.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Adapter.PageRecyclerViewAdapter;
import madsoft.com.form.Application.MyApplication;
import madsoft.com.form.Assets;
import madsoft.com.form.DataBase.PageDao;
import madsoft.com.form.DataBase.entity.Page;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.Category;
import madsoft.com.form.R;
import madsoft.com.form.service.DownloadService;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Даниил on 27.09.2018.
 */

public class DownloadedFragment extends Fragment implements Filterable, ArticleRecyclerViewAdapter.onClickListener, ArticleRecyclerViewAdapter.IntentCallback {

    private static DownloadedFragment instance;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PageDao downloadedPageDao;
    private PageRecyclerViewAdapter downloadsAdapter;
    private Category category;



    public static DownloadedFragment newInstance() {
        if(instance == null)
            instance = new DownloadedFragment();
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded, null);
        recyclerView = view.findViewById(R.id.recycler_view_downloaded);
        swipeRefreshLayout = view.findViewById(R.id.d_swipe_refresh);
        downloadsAdapter = new PageRecyclerViewAdapter(this, getString(R.string.action_delete));
        recyclerView.setAdapter(downloadsAdapter);
        downloadsAdapter.setIntentCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
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
        new loadCache().execute();

    }

    @Override
    public void applyFilter(Category category) {

    }

    @Override
    public void onItemClick(int position) {
       Page page = downloadsAdapter.getItem(position);
        Bundle themeActivityBundle = new Bundle();
        themeActivityBundle.putSerializable(DownloadService.BUNDLE_MESSAGE_KEY, downloadsAdapter.pageToArticle(page));
        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.setAction(" ");
        intent.putExtra(DownloadService.BUNDLE_KEY, themeActivityBundle);
        intent.putExtra(SlidingThemeActivity.READ_MODE, true);//TODO если true то работать с локальным файлом
        intent.putExtra(Assets.LINK, page.shareLink);
        startActivity(intent);
    }

    @Override
    public void onShareArticle(ArticleWp article) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getLink());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onDownloadArticle(ArticleWp article) {
//TODO удалять записи здесь!!!
    }


    public class loadCache extends AsyncTask<String, Void, Boolean> {
        List<Page> cachedPages;
        @Override
        protected  Boolean doInBackground(String ... arg){

            downloadedPageDao = MyApplication.getDatabase().pageDao();
            cachedPages = downloadedPageDao.getAll();
            if(cachedPages == null)
                return false;
            return true;

        }

        @Override
        protected void onPostExecute(Boolean downloaded){

            swipeRefreshLayout.setRefreshing(false);
            if(downloaded) {
                downloadsAdapter.clear();
                downloadsAdapter.appendList(cachedPages);
            }
            else
                Log.e("Error", "Couldn't load cache");


        }

    }
}
