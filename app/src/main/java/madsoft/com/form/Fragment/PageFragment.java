package madsoft.com.form.Fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mateware.snacky.Snacky;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment implements ArticleRecyclerViewAdapter.onClickListener, ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback, ArticleRecyclerViewAdapter.IntentCallback {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NetworkService networkService;
    private ArticleRecyclerViewAdapter articleRecyclerViewAdapter;
    private RecyclerView.OnScrollListener onScrollListener;



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
                .enqueue(new Callback<List<ArticleWp>>() {
                    @Override
                    public void onResponse(Call<List<ArticleWp>> call, Response<List<ArticleWp>> response) {
                        short pages = Short.parseShort(response.headers().get("X-WP-TotalPages"));
                        List<ArticleWp> list = response.body();
                        swipeRefreshLayout.setRefreshing(false);
                        articleRecyclerViewAdapter.clear();
                        articleRecyclerViewAdapter.setPages(pages);
                        articleRecyclerViewAdapter.setItems(list);
                    }

                    @Override
                    public void onFailure(Call<List<ArticleWp>> call, Throwable t) {
                        buildSnack(3);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });


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
        buildSnack(2);
    }

    @Override
    public void onFailure() {
        //проблемы при обновлнии адаптера
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

}
