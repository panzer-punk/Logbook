package madsoft.com.form.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mateware.snacky.Snacky;
import madsoft.com.form.Activity.MainActivity;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Assets;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.Category;
import madsoft.com.form.Network.Objects.DataEntity;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.Network.reciever.NetworkConnectionReceiver;
import madsoft.com.form.R;
import madsoft.com.form.service.DownloadService;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;

import static madsoft.com.form.Activity.MainActivity.WRITE_FILE_PERMISSION;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment implements ArticleRecyclerViewAdapter.onClickListener,
        ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback,
        ArticleRecyclerViewAdapter.IntentCallback,
        Filterable,
        NetworkConnectionReceiver.Updatable {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private boolean loadFlag = true;
    private Category category;
    private OnScrollNextPageListener nextPageListener;
    private NetworkService networkService;
    protected ArticleRecyclerViewAdapter articleRecyclerViewAdapter;
    private RecyclerView.OnScrollListener onScrollListener;
    private static PageFragment instance;
    private static ArticleWp downloadUrl;


    public PageFragment(boolean loadFlag) {
        this.loadFlag = loadFlag;
    }

    public PageFragment() {
    }

    public static PageFragment newInstance() {
        if(instance == null)
            instance = new PageFragment();
        return instance;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleRecyclerViewAdapter = new ArticleRecyclerViewAdapter(this);
        category = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pages, null);

        recyclerView = view.findViewById(R.id.recycler_view);
        int orientation = getResources().getConfiguration().orientation;
        if ( orientation == Configuration.ORIENTATION_LANDSCAPE) {
           recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        }

        recyclerView.setAdapter(articleRecyclerViewAdapter);
        onScrollListener = new OnScrollNextPageListener((LinearLayoutManager) recyclerView.getLayoutManager(), articleRecyclerViewAdapter);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                download();

            }
        });

        networkService = NetworkService.getInstance();
        recyclerView.addOnScrollListener(onScrollListener);
        articleRecyclerViewAdapter.setCallback(this);
        articleRecyclerViewAdapter.setIntentCallback(this);
        if(loadFlag)
        download();
        if(instance != null)
        instance = this;

        return view;
    }

    private void download(){

        swipeRefreshLayout.setRefreshing(true);
        MainActivity mActivity = (MainActivity) getActivity();
        if(category == null)
        networkService
                .getWpApi()
                .getArticleWpCall()
                .enqueue(articleRecyclerViewAdapter);
        else
            networkService
                    .getWpApi()
                    .getArticleWpCall(""+category.getId())
                    .enqueue(articleRecyclerViewAdapter);
    }


    @Override
    public void onItemClick(int position) {
        ArticleWp article = articleRecyclerViewAdapter.getItem(position);

        Bundle themeActivityBundle = new Bundle();
        themeActivityBundle.putSerializable(DownloadService.BUNDLE_MESSAGE_KEY, article);
        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.setAction(" ");
        intent.putExtra(DownloadService.BUNDLE_KEY, themeActivityBundle);
       // intent.putExtra(Assets.LINK, article.getLink());
      //  intent.putExtra(Assets.TITLE, article.getTitle().getRendered());
        startActivity(intent);

    }

    private void nextPageSnack(){
      buildSnack( 1);
    }
    @Override
    public void onResponse() {
       //загрузка успешно завершена
        stopRefreshLayout();
        buildSnack(2);
    }

    private void stopRefreshLayout(){
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onFailure() {
        //проблемы при обновлнии адаптера
        stopRefreshLayout();
        buildSnack(3);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.checkConnection(this);
      //  NetworkConnectionReceiver receiver = new NetworkConnectionReceiver((NetworkConnectionReceiver.Updatable) getActivity());
     //   getActivity().registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    private void buildSnack(int id){

       Snackbar snackbar;


        if(getView() == null)
            return;

       /* Snacky.Builder builder = Snacky.builder()
                .setView(getView())
                .setMaxLines(2)
                .setTextSize(20)
                .setDuration(Snacky.LENGTH_SHORT);*/

        switch (id){

            case 1:
                snackbar = Snackbar.make(getView(),R.string.loadingArticles,Snackbar.LENGTH_SHORT);
               //  builder.setText(R.string.loadingArticles)
               //  .build()
             //    .show();
                 break;
            case 2:
                snackbar = Snackbar.make(getView(),R.string.articlesLoadSuccess,Snackbar.LENGTH_SHORT);
             //   builder.setText(R.string.articlesLoadSuccess)
            //   .success()
            //    .show();
                break;
            case 3:
                snackbar = Snackbar.make(getView(),R.string.articlesLoadFail,Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(Color.RED);
             //   builder.setText(R.string.articlesLoadFail)
             //   .error()
           //     .show();
                break;
                default:
                    snackbar = Snackbar.make(getView(),R.string.app_name,Snackbar.LENGTH_SHORT);
             //       builder.setText(R.string.app_name)
              //      .build()
              //      .show();

        }
        snackbar.setAnchorView(getActivity().findViewById(R.id.bottom_navigation));
        snackbar.show();
    }

    @Override
    public void onShareArticle(DataEntity article) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getUrl());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void askWritePermission(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_FILE_PERMISSION);
        }else
            downloadArticle();

    }


    @Override
    public void onDownloadArticle(DataEntity articleWp) {
        downloadUrl = (ArticleWp) articleWp;
       askWritePermission();
    }

    public void downloadArticle(){
        Bundle serviceBundle = new Bundle();
        serviceBundle.putSerializable(DownloadService.BUNDLE_MESSAGE_KEY, downloadUrl);
          Intent intent = new Intent(getActivity(), DownloadService.class);
          intent.putExtra(DownloadService.BUNDLE_KEY, serviceBundle);
       //   intent.putExtra(DownloadService.URL_INTENT_KEY, downloadUrl.getLink());
       //   intent.putExtra(DownloadService.MODIFIED_KEY, downloadUrl.getModified());
          getActivity().startService(intent);
    }

    @Override
    public void applyFilter(Category category) {
        swipeRefreshLayout.setRefreshing(true);
        this.category = category;
        articleRecyclerViewAdapter.setArticlesCategory(category);
      download();
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void onNetworkConnection() {
        download();
    }
}
