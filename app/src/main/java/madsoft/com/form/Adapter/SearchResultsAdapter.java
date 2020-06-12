package madsoft.com.form.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Network.Objects.ArticleWpListItem;
import madsoft.com.form.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsAdapter extends RetrofitWpPaginationAdapter<ArticleWpListItem> {
    private boolean nextPageReadyFlag = true;
    ArticleRecyclerViewAdapter.onClickListener adapterOnClickListener;
    String query;
    SearchResultsViewHolder searchViewHolder;

    public SearchResultsAdapter(ArticleRecyclerViewAdapter.onClickListener adapterOnClickListener) {
        this.adapterOnClickListener = adapterOnClickListener;
        list = new ArrayList<>(10);
        query = new String();
    }

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       ArticleRecyclerViewAdapter.onClickListener searchResultsOnClickListener;
        public TextView title;

       public SearchResultsViewHolder(@NonNull View itemView, ArticleRecyclerViewAdapter.onClickListener onClickListener ) {
           super(itemView);
           searchResultsOnClickListener = onClickListener;
           title = itemView.findViewById(R.id.card_title_search);
           itemView.setOnClickListener(this);
       }

       public void bind(ArticleWpListItem articleWpListItem){
           title.setText(articleWpListItem.getTitle());
       }

       @Override
       public void onClick(View v) {
           searchResultsOnClickListener.onItemClick(getAdapterPosition());
       }
   }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.card_list_item, parent, false);
        return new SearchResultsViewHolder(view, adapterOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchResultsViewHolder articleViewHolder = (SearchResultsViewHolder) holder;
        articleViewHolder.bind(list.get(position));
        searchViewHolder = articleViewHolder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onResponse(Call<List<ArticleWpListItem>> call, Response<List<ArticleWpListItem>> response) {
        short pages = Short.parseShort(response.headers().get("X-WP-TotalPages"));
        clear();
        List<ArticleWpListItem> list = response.body();
        setPages(pages);
        appendList(list);
        if(hasCallback())
            callback.onResponse();
    }
    public ArticleWpListItem getItem(int position)
    {return list.get(position);}

    @Override
    public void nextPage() {
        if(!hasNextPage() || !nextPageReadyFlag) return;
        nextPageReadyFlag = false;

        networkService.getWpApi().searchArticleWpCall(query,++curPage).enqueue(new Callback<List<ArticleWpListItem>>() {
            @Override
            public void onResponse(Call<List<ArticleWpListItem>> call, Response<List<ArticleWpListItem>> response) {
                List<ArticleWpListItem> list = response.body();
                appendList(list);
                nextPageReadyFlag = true;
                if(callback != null)
                    callback.onResponse();
            }

            @Override
            public void onFailure(Call<List<ArticleWpListItem>> call, Throwable t) {
                curPage--;
                nextPageReadyFlag = true;
                if(callback!=null)
                    callback.onFailure();
            }
        });
    }

    @Override
    public void onFailure(Call<List<ArticleWpListItem>> call, Throwable t) {

    }
}
