package madsoft.com.form.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Network.Objects.Category;
import madsoft.com.form.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesRecyclerViewAfapter extends RetrofitWpPaginationAdapter<Category>{
    public CategoriesViewHolder holder;
    public CategoriesRecyclerViewAfapter(ArticleRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    {
        list = new ArrayList<>(10);
    }
    public  class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView categoryText;
        private ArticleRecyclerViewAdapter.onClickListener onClickListener;


        public CategoriesViewHolder(final View itemView, ArticleRecyclerViewAdapter.onClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;
            categoryText = itemView.findViewById(R.id.category_text);
        }

        public void bind(Category category){
           categoryText.setText(category.getName());
           categoryText.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   click();
               }
           });
        }




        public void click() {
            onClickListener.onItemClick(getAdapterPosition());
        }

        @Override
        public void onClick(View v) {
            click();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new CategoriesRecyclerViewAfapter.CategoriesViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      CategoriesRecyclerViewAfapter.CategoriesViewHolder categoryViewHolder = (CategoriesRecyclerViewAfapter.CategoriesViewHolder) holder;
       categoryViewHolder.bind(list.get(position));
        this.holder = categoryViewHolder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Category getItem(int position)
    {return list.get(position);}
    @Override
    public void nextPage() {
        if(!hasNextPage()) return;
        networkService.getWpApi().getCategoriesWpCall(++curPage).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> list = response.body();
                appendList(list);
                if(callback != null)
                    callback.onResponse();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                curPage--;
                if(callback!=null)
                    callback.onFailure();
            }
        });
    }

    @Override
    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
        short pages = Short.parseShort(response.headers().get("X-WP-TotalPages"));
        clear();
        List<Category> list = response.body();
        setPages(pages);
        appendList(list);
        if(hasCallback())
            callback.onResponse();
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if(hasCallback())
            callback.onFailure();
    }
}
