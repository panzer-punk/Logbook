package madsoft.com.form.Fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mateware.snacky.Snacky;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.Category;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.R;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment implements ArticleRecyclerViewAdapter.onClickListener,
        ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback,
        ArticleRecyclerViewAdapter.IntentCallback,
        Filterable{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NetworkService networkService;
    protected ArticleRecyclerViewAdapter articleRecyclerViewAdapter;
    private RecyclerView.OnScrollListener onScrollListener;
    private static PageFragment instance;


    public static PageFragment newInstance() {
        if(instance == null)
            instance = new PageFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleRecyclerViewAdapter = new ArticleRecyclerViewAdapter(this);
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



        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                download();

            }
        });

        networkService = NetworkService.getInstance();
        onScrollListener = new RecyclerView.OnScrollListener() {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();//смотрим сколько элементов на экране
                int totalItemCount = layoutManager.getItemCount();//сколько всего элементов
                int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

                    if ( (visibleItemCount+firstVisibleItems) >= totalItemCount && articleRecyclerViewAdapter.hasNextPage()) {
                        articleRecyclerViewAdapter.nextPage();
                        nextPageSnack();

                    }

            }

        };
        recyclerView.addOnScrollListener(onScrollListener);
        articleRecyclerViewAdapter.setCallback(this);
        articleRecyclerViewAdapter.setIntentCallback(this);
        download();

        return view;
    }

    private void download(){

        swipeRefreshLayout.setRefreshing(true);
        networkService
                .getWpApi()
                .getArticleWpCall()
                .enqueue(articleRecyclerViewAdapter);


    }


    @Override
    public void onItemClick(int position) {
        ArticleWp article = articleRecyclerViewAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.setAction(" ");
        intent.putExtra(ArticleWp.LINK, article.getLink());
        intent.putExtra(ArticleWp.TITLE, article.getTitle().getRendered());
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
    }

    private void buildSnack(int id){

        Snacky.Builder builder = Snacky.builder()
                .setView(getView())
                .setMaxLines(2)
                .setTextSize(20)
                .setDuration(Snacky.LENGTH_SHORT);

        switch (id){

            case 1:
                 builder.setText(R.string.loadingArticles)
                 .build()
                 .show();
                 break;
            case 2:
                builder.setText(R.string.articlesLoadSuccess)
               .success()
                .show();
                break;
            case 3:
                builder.setText(R.string.articlesLoadFail)
                .error()
                .show();
                break;
                default:
                    builder.setText(R.string.app_name)
                    .build()
                    .show();

        }
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
    public void applyFilter(Category category) {
        swipeRefreshLayout.setRefreshing(true);
        networkService = NetworkService.getInstance();
        if(category != null)
        networkService
                .getWpApi()
                .getArticleWpCall(""+category.getId())
                .enqueue(articleRecyclerViewAdapter);
        else
            networkService
                    .getWpApi()
                    .getArticleWpCall()
                    .enqueue(articleRecyclerViewAdapter);
    }
}
