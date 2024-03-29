package info.logos.form.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.mateware.snacky.Snacky;
import info.logos.form.Activity.MainActivity;
import info.logos.form.Activity.SlidingThemeActivity;
import info.logos.form.Adapter.ArticleRecyclerViewAdapter;
import info.logos.form.Adapter.SearchResultsAdapter;
import info.logos.form.Network.Objects.ArticleWpListItem;
import info.logos.form.Network.Objects.Category;
import info.logos.form.Network.WpApi.NetworkService;
import info.logos.form.Network.reciever.NetworkConnectionReceiver;
import info.logos.form.R;
import info.logos.form.service.DownloadService;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Даниил on 27.09.2018.
 */

public class SearchFragment extends Fragment
        implements Filterable,
        ArticleRecyclerViewAdapter.onClickListener,
        ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback,
        NetworkConnectionReceiver.Updatable {

    private static SearchFragment instance;
    private RecyclerView searchRecyclerView;
    private String query;
    private EditText searchEditText;
    private OnScrollNextPageListener nextPageListener;
    private SearchResultsAdapter searchFragmentResultsAdapter;
    private SwipeRefreshLayout searchSwipeRefreshLayout;

    public static SearchFragment newInstance() {
        if(instance == null)
            instance = new SearchFragment();
        return instance;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFragmentResultsAdapter = new SearchResultsAdapter(this);
        searchFragmentResultsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        searchRecyclerView = view.findViewById(R.id.search_recycler_view);
        searchEditText = view.findViewById(R.id.search_field);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        searchRecyclerView.setAdapter(searchFragmentResultsAdapter);
        nextPageListener =
                new OnScrollNextPageListener((LinearLayoutManager) searchRecyclerView.getLayoutManager(),
                        searchFragmentResultsAdapter, getActivity());
        searchRecyclerView.addOnScrollListener(nextPageListener);
        searchSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_search);
        searchSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                search(searchEditText.getText().toString());

            }
        });
        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchEditText.clearFocus();
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    query = v.getText().toString();
                    search(query);
                    return true;
                }else
                return false;
            }
        });
        if(instance != null)
            instance = this;
        return view;
    }

    private void search(String query){
        if(!query.isEmpty()) {
            searchSwipeRefreshLayout.setRefreshing(true);
            NetworkService networkService = NetworkService.getInstance();
            networkService.getWpApi().searchArticleWpCall(query).enqueue(searchFragmentResultsAdapter);
            searchRecyclerView.setFocusable(true);
        }else
            searchSwipeRefreshLayout.setRefreshing(false);
        }

    @Override
    public void applyFilter(Category category) {//Обработать запрос невозможно из-за такого устройства WP API

      //  this.category = category;
      //  search(query);

    }

    @Override
    public Category getCategory() {
        return null;
    }


    @Override
    public void onItemClick(int position) {
        ArticleWpListItem listItem = searchFragmentResultsAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), SlidingThemeActivity.class);
        intent.setAction(" ");
        Bundle bundle = new Bundle();
        bundle.putSerializable(DownloadService.BUNDLE_MESSAGE_KEY, listItem);
        intent.putExtra(DownloadService.BUNDLE_KEY, bundle);
      //  intent.putExtra(Assets.LINK, listItem.getUrl());
     //   intent.putExtra(Assets.TITLE, listItem.getTitle());
        startActivity(intent);
    }

    @Override
    public void onResponse() {
        searchSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure() {
        searchSwipeRefreshLayout.setRefreshing(false);

        Snacky.builder()
                .setView(getView())
                .setMaxLines(2)
                .setTextSize(20)
                .setDuration(Snacky.LENGTH_SHORT)
                .setText(R.string.articlesLoadFail)
                .error()
                .show();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.checkConnection(this);
    }

    @Override
    public void onNetworkConnection() {
        search(searchEditText.getText().toString());
    }
}
