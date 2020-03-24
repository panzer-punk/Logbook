package madsoft.com.form.Activity;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.evolve.backdroplibrary.BackdropContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import madsoft.com.form.Fragment.DownloadedFragment;
import madsoft.com.form.Fragment.PageFragment;
import madsoft.com.form.R;
import madsoft.com.form.Fragment.SearchFragment;

import android.view.MenuItem;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DownloadedFragment downloadedFragment;
    private BackdropContainer backdropContainer;
    private PageFragment pageFragment;
    private SearchFragment searchFragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pager = findViewById(R.id.pager);

        final MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);

       bottomNavigationView = findViewById(R.id.bottom_navigation);


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
                    case R.id.action_settings:
                        pager.setCurrentItem(3);
                        break;
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
                    case 3:
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        toolbar = findViewById(R.id.toolbar_main);
     //   setSupportActionBar(toolbar);
        backdropContainer = (BackdropContainer)findViewById(R.id.backdropcontainer);
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearOutSlowInInterpolator())
                .dropHeight(this.getResources().getDimensionPixelSize(R.dimen.sneek_height))
                .build();

        pager.addOnPageChangeListener(viewPagerMain_OnPageChangeListener);

        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        pager.setCurrentItem(1);


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

