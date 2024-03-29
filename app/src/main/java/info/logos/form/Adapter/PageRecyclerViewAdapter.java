package info.logos.form.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.logos.form.DataBase.entity.Page;
import info.logos.form.R;
import retrofit2.Call;
import retrofit2.Response;

public class PageRecyclerViewAdapter extends RetrofitWpPaginationAdapter <Page>{

    private ArticleRecyclerViewAdapter.onClickListener onClickListener;
    private String deleteBtnText;

    {
        list = new ArrayList<>();
    }

    public PageRecyclerViewAdapter(ArticleRecyclerViewAdapter.onClickListener onClickListener, String deleteBtnText) {
        this.onClickListener = onClickListener;
        this.deleteBtnText = deleteBtnText;
    }

    public void insertPage(Page newPage) {

           list.add(newPage);
            notifyDataSetChanged();


    }

    public class PageRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView titleTextView, descriptionTextView;
        public Button share, download;


        public PageRecyclerViewHolder(@NonNull View itemView, final ArticleRecyclerViewAdapter.onClickListener listener) {
            super(itemView);
            onClickListener = listener;
            imageView = itemView.findViewById(R.id.card_image);
            titleTextView = itemView.findViewById(R.id.card_title);
            descriptionTextView = itemView.findViewById(R.id.card_desc);
            share = itemView.findViewById(R.id.button_share);
            download = itemView.findViewById(R.id.button_download);
            download.setText(deleteBtnText);
            download.setTextColor(Color.RED);
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
                   intentCallback.onDownloadArticle(list.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(getAdapterPosition());
        }

        public void bind(Page page){

            titleTextView.setText(page.title);
            descriptionTextView.setText(page.modified);
            imageView.setImageResource(R.drawable.placeholder);//TODO сохранять изображения в кэш



        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new PageRecyclerViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PageRecyclerViewHolder viewHolder = (PageRecyclerViewHolder) holder;
        viewHolder.bind(list.get(position));
    }

    public Page getItem(int id){//TODO добавить в RETROFIT PAGINATION ADAPTER
        return list.get(id);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onResponse(Call<List<Page>> call, Response<List<Page>> response) {

    }

    public void remove(Page page){
        list.remove(page);
        notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<Page>> call, Throwable t) {

    }

}
