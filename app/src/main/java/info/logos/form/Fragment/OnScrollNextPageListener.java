package info.logos.form.Fragment;

import android.app.Activity;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.logos.form.Adapter.RetrofitWpPaginationAdapter;
import info.logos.form.R;

public class OnScrollNextPageListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private RetrofitWpPaginationAdapter paginationAdapter;
    private Activity activity;

    public OnScrollNextPageListener(LinearLayoutManager linearLayoutManager,
                                    RetrofitWpPaginationAdapter adapter,
                                    Activity activity) {
        layoutManager =  linearLayoutManager;
        paginationAdapter = adapter;
        this.activity = activity;
    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();//смотрим сколько элементов на экране
        int totalItemCount = layoutManager.getItemCount();//сколько всего элементов
        int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

        short curPage = paginationAdapter.getCurPage();

        if ( (visibleItemCount+firstVisibleItems) >= totalItemCount && paginationAdapter.hasNextPage()) {
            paginationAdapter.nextPage();
            if(curPage < paginationAdapter.getCurPage()){
                Snackbar snackbar =
                        Snackbar.make(activity.findViewById(R.id.bottom_navigation).getRootView(),
                                activity.getResources().getText(R.string.nextpage_loading),
                                Snackbar.LENGTH_SHORT);
                snackbar.setAnchorView(activity.findViewById(R.id.bottom_navigation));
                snackbar.show();
            }
        }

    }

}
