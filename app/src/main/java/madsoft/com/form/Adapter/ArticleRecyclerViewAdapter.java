package madsoft.com.form.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter {
    public interface ArticleAdapterNextPageCallback{
        void onResponse();
        void onFailure();
    }
    public interface IntentCallback{

        void onShareArticle(ArticleWp article);

    }
    public ArticleViewHolder holder;
    private short pages = 1;
    private short curPage = 1;
    private NetworkService networkService = NetworkService.getInstance();
    private ArticleAdapterNextPageCallback callback;
    protected List<ArticleWp> list;
    protected IntentCallback intentCallback;
    private onClickListener onClickListener;

    public IntentCallback getIntentCallback() {
        return intentCallback;
    }

    public void setIntentCallback(IntentCallback intentCallback) {
        this.intentCallback = intentCallback;
    }

    public short getCurPage() {
        return curPage;
    }

    public boolean hasCallback(){return callback!=null;}

    public void setCallback(ArticleAdapterNextPageCallback callback) {
        this.callback = callback;
    }

    public void nextPage() {
        if(!hasNextPage()) return;
        networkService.getWpApi().getArticleWpCall(++curPage).enqueue(new Callback<List<ArticleWp>>() {
            @Override
            public void onResponse(Call<List<ArticleWp>> call, Response<List<ArticleWp>> response) {
                List<ArticleWp> list = response.body();
                appendList(list);
                if(callback != null)
                callback.onResponse();
            }

            @Override
            public void onFailure(Call<List<ArticleWp>> call, Throwable t) {
                curPage--;
                if(callback!=null)
            callback.onFailure();
            }
        });
    }



    public short getPages() {
        return pages;
    }

    public void setPages(short pages) {
        this.pages = pages;
    }

    public boolean hasNextPage() {
        return curPage < pages;
    }

    public  class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView titleTextView, descriptionTextView;
        public Button share, download;
        private onClickListener onClickListener;


        public ArticleViewHolder(final View itemView, onClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;
            imageView = itemView.findViewById(R.id.card_image);
            titleTextView = itemView.findViewById(R.id.card_title);
            descriptionTextView = itemView.findViewById(R.id.card_desc);
            share = itemView.findViewById(R.id.button_share);
            download = itemView.findViewById(R.id.button_download);
            itemView.setOnClickListener(this);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(intentCallback == null) return;

                    intentCallback.onShareArticle(list.get(getAdapterPosition()));

                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(ArticleWp article){

            titleTextView.setText(article.getTitle().getRendered());
            descriptionTextView.setText(article.getModified());
            Picasso.get().load(article.getJetpackFeaturedMediaUrl())
                    .into(imageView);


        }



        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface onClickListener{
        void onItemClick(int position);
    }

    public ArticleRecyclerViewAdapter(onClickListener onClickListener) {
        list =  new LinkedList<>();
        this.onClickListener = onClickListener;
    }

    public ArticleRecyclerViewAdapter(List<ArticleWp> list, onClickListener onClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
    }
    public ArticleRecyclerViewAdapter(onClickListener onClickListener, short pages) {
        this.onClickListener = onClickListener;
        this.pages = pages;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item, parent, false);
       return new ArticleViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        articleViewHolder.bind(list.get(position));
        holder = articleViewHolder;
    }



    @Override
    public int getItemCount()
    {return list.size(); }

    public void appendList(List<ArticleWp> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setItems(List <ArticleWp> linkedList){

        list.addAll(linkedList);
        notifyDataSetChanged();

    }

    public ArticleWp getItem(int position)
    {return list.get(position);}

    public void clear(){
        curPage = 1;
        list.clear();
        notifyDataSetChanged();

    }


}
