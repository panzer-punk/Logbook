package madsoft.com.form.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Даниил on 27.09.2018.
 */

public class PageFragment extends Fragment implements ArticleRecyclerViewAdapter.onClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NetworkService networkService;
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

        networkService = NetworkService.getInstance();

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
                        List<ArticleWp> list = response.body();

                        swipeRefreshLayout.setRefreshing(false);
                        articleRecyclerViewAdapter.clear();
                        articleRecyclerViewAdapter.setItems(list);
                    }

                    @Override
                    public void onFailure(Call<List<ArticleWp>> call, Throwable t) {

                    }
                });

    }


    @Override
    public void onItemClick(int position) {
        ArticleWp article = articleRecyclerViewAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.putExtra(ArticleWp.LINK, article.getLink());
        intent.putExtra(ArticleWp.TITLE, article.getTitle().getRendered());
        startActivity(intent);

    }

}
