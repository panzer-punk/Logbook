package madsoft.com.form.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter {
    public ArticleViewHolder holder;
    public static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView titleTextView, descriptionTextView;
        private onClickListener onClickListener;


        public ArticleViewHolder(View itemView, onClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;
            imageView = itemView.findViewById(R.id.card_image);
            titleTextView = itemView.findViewById(R.id.card_title);
            descriptionTextView = itemView.findViewById(R.id.card_desc);
            itemView.setOnClickListener(this);

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
    List<ArticleWp> list;
    private onClickListener onClickListener;

    public ArticleRecyclerViewAdapter(onClickListener onClickListener) {
        list =  new LinkedList<>();
        this.onClickListener = onClickListener;
    }

    public ArticleRecyclerViewAdapter(List<ArticleWp> list, onClickListener onClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
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
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List <ArticleWp> linkedList){

        list.addAll(linkedList);
        notifyDataSetChanged();

    }

    public ArticleWp getItem(int position)
    {return list.get(position);}

    public void clear(){

        list.clear();
        notifyDataSetChanged();

    }


}
