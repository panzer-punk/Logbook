package madsoft.com.form.Fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import madsoft.com.form.Adapter.RetrofitWpPaginationAdapter;

public class OnScrollNextPageListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private RetrofitWpPaginationAdapter paginationAdapter;

    public OnScrollNextPageListener(LinearLayoutManager linearLayoutManager, RetrofitWpPaginationAdapter adapter) {
        layoutManager =  linearLayoutManager;
        paginationAdapter = adapter;
    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();//смотрим сколько элементов на экране
        int totalItemCount = layoutManager.getItemCount();//сколько всего элементов
        int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

        if ( (visibleItemCount+firstVisibleItems) >= totalItemCount && paginationAdapter.hasNextPage()) {
            paginationAdapter.nextPage();
        }

    }

}
