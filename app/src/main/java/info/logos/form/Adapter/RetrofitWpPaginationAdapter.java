package info.logos.form.Adapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import info.logos.form.Network.WpApi.NetworkService;
import retrofit2.Callback;

public abstract class RetrofitWpPaginationAdapter <Type> extends RecyclerView.Adapter implements Callback<List<Type>> {

    protected short pages = 1;
    protected short curPage = 1;
    protected NetworkService networkService = NetworkService.getInstance();
    protected ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback callback;
    protected List<Type> list;
    protected ArticleRecyclerViewAdapter.IntentCallback intentCallback;
    protected ArticleRecyclerViewAdapter.onClickListener onClickListener;

    public void nextPage(){
        if(hasNextPage())
            curPage++;
    }

    public boolean hasCallback(){return callback!=null;}

    public void setPages(short pages) {
        this.pages = pages;
    }

    public short getPages() {
        return pages;
    }

    public short getCurPage() {
        return curPage;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback getCallback() {
        return callback;
    }

    public List<Type> getList() {
        return list;
    }

    public ArticleRecyclerViewAdapter.IntentCallback getIntentCallback() {
        return intentCallback;
    }

    public ArticleRecyclerViewAdapter.onClickListener getOnClickListener() {
        return onClickListener;
    }

    public void appendList(List<Type> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean hasNextPage() {
        return curPage < pages;
    }

    public void clear(){
        curPage = 1;
        list.clear();
        notifyDataSetChanged();

    }
    public void setNetworkService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void setCallback(ArticleRecyclerViewAdapter.ArticleAdapterNextPageCallback callback) {
        this.callback = callback;
    }

    public void setIntentCallback(ArticleRecyclerViewAdapter.IntentCallback intentCallback) {
        this.intentCallback = intentCallback;
    }

    public void setOnClickListener(ArticleRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
