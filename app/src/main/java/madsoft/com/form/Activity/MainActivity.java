package madsoft.com.form.Activity;


import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.evolve.backdroplibrary.BackdropContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import madsoft.com.form.Adapter.ArticleRecyclerViewAdapter;
import madsoft.com.form.Adapter.CategoriesRecyclerViewAdapter;
import madsoft.com.form.Fragment.DownloadedFragment;
import madsoft.com.form.Fragment.Filterable;
import madsoft.com.form.Fragment.OnScrollNextPageListener;
import madsoft.com.form.Fragment.PageFragment;
import madsoft.com.form.Network.Objects.Category;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.Network.reciever.NetworkConnectionReceiver;
import madsoft.com.form.R;
import madsoft.com.form.Fragment.SearchFragment;
import madsoft.com.form.service.DownloadService;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements ArticleRecyclerViewAdapter.onClickListener, NetworkConnectionReceiver.Updatable{
    private Toolbar toolbar;
    private DownloadedFragment downloadedFragment;
    private BackdropContainer backdropContainer;
    private PageFragment pageFragment;
    public Category category;
    private SearchFragment searchFragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;
    private NetworkService networkService;
    private RecyclerView categoriesRecyclerView;
    private FloatingActionButton fab;
    private CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter;
    private  MyPagerAdapter pagerAdapter;
    private    NetworkConnectionReceiver receiver;
    private OnScrollNextPageListener onScrollListener;
    public static final int WRITE_FILE_PERMISSION = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_FILE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //  Intent intent = new Intent(getActivity(), DownloadService.class);
                    //  intent.putExtra(DownloadService.URL_INTENT_KEY, url);
                    //      getActivity().startService(intent);
                    Log.d("P", "granted");
                  pageFragment = (PageFragment) pagerAdapter.getItem(pager.getCurrentItem());
                  pageFragment.downloadArticle();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        receiver = new NetworkConnectionReceiver(this);
        if(savedInstanceState != null){
            category = (Category) savedInstanceState.getSerializable(Category.BUNDLE_KEY);
        }else
            category = null;

        setContentView(R.layout.activity_main);

        networkService = NetworkService.getInstance();

        fab = findViewById(R.id.floating_action_button);
        if(category != null)
            fab.show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(-1);
            }
        });

        pager = findViewById(R.id.pager);

         pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);



       bottomNavigationView = findViewById(R.id.bottom_navigation);
       categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(this);
        View categoriesLayout = findViewById(R.id.categories_layout);
        categoriesRecyclerView = categoriesLayout.findViewById(R.id.categories_recyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesRecyclerView.setAdapter(categoriesRecyclerViewAdapter);

        onScrollListener =
                new OnScrollNextPageListener((LinearLayoutManager) categoriesRecyclerView.getLayoutManager(),
                        categoriesRecyclerViewAdapter, this);
        categoriesRecyclerView.addOnScrollListener(onScrollListener);

        BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationView_OnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_downloaded:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.action_main:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.action_search:
                        pager.setCurrentItem(2);
                        break;
                  //  case R.id.action_settings:
               //         pager.setCurrentItem(3);
                //        break;
                }

                return true;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationView_OnNavigationItemSelectedListener);

        ViewPager.OnPageChangeListener viewPagerMain_OnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);

                        break;
                    case 1:
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
                        break;
                 //   case 3:
                //        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                }

                    Filterable f = (Filterable) pagerAdapter.getItem(position);
                    if(f.getCategory() != category)
                    f.applyFilter(category);
                
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        toolbar = findViewById(R.id.toolbar_main);
     //   setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        backdropContainer = (BackdropContainer)findViewById(R.id.backdropcontainer);
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearOutSlowInInterpolator())
                .dropHeight(this.getResources().getDimensionPixelSize(R.dimen.sneek_height))
                .build();

        pager.addOnPageChangeListener(viewPagerMain_OnPageChangeListener);

        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        pager.setCurrentItem(1);
      loadCategories();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void loadCategories(){
        networkService
                .getWpApi()
                .getCategoriesWpCall()
                .enqueue(categoriesRecyclerViewAdapter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Category.BUNDLE_KEY, category);
    }

    @Override
    public void onItemClick(int position) {

        if(position > 0) {
            backdropContainer.closeBackview();
            fab.show();
            category = categoriesRecyclerViewAdapter.getItem(position);
        }else {
            fab.hide();
            category = null;
            toolbar.setTitle(R.string.app_name);
        }
        Filterable filterable = (Filterable) pagerAdapter.getItem(pager.getCurrentItem());
        filterable.applyFilter(category);
        if(category != null)
        toolbar.setTitle(category.getName());


    }

    public void checkConnection(NetworkConnectionReceiver.Updatable updatable){
        if(!receiver.isOrderedBroadcast())
          registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

          receiver.subscribe(updatable);
    }

    @Override
    public void onNetworkConnection() {

        Toast.makeText(this, R.string.loading, Toast.LENGTH_LONG).show();
        loadCategories();
        unregisterReceiver(receiver);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return DownloadedFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return PageFragment.newInstance();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return SearchFragment.newInstance();
                default:
                    return null;
            }
        }

    }
}

