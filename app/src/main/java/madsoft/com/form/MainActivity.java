package madsoft.com.form;


import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    /*  private static  String LIST = "linkTextList";
      private CacheSystem cacheSystem;

      private SwipeRefreshLayout swipeRefreshLayout;
      public Elements links; // сохраняется в Assets
      public static ArrayList<String> linkTextList;
      private ArrayAdapter<String> adapter;
      private ListView listView;
      private Connector connector;
      private ConnectivityManager connectivityManager;*/
    private Toolbar toolbar;
    private DownloadedFragment downloadedFragment;
    private PageFragment pageFragment;
    private SearchFragment searchFragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pager = findViewById(R.id.pager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);

        //  connector = new Connector();

        //  connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //   listView = findViewById(R.id.list_view);

        //   cacheSystem = new CacheSystem(this);


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
        setSupportActionBar(toolbar);


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



      //  Log.v("Bundle", "" + (savedInstanceState == null));

      /*  AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {

                String linkHref;
                String filename;

               try {

                   if(Assets.LNKS != null) {
                       Document doc = Jsoup.parse(Assets.LNKS.getLink((lisView.getItemAtPosition(position).toString())).outerHtml());
                       Element link = doc.select("a").first();
                       linkHref = link.attr("href");
                       filename = lisView.getItemAtPosition(position).toString();
                   }else {

                       linkHref = null;
                       filename = lisView.getItemAtPosition(position).toString();


                   }

                    Intent intent = new Intent(MainActivity.this, SlidingThemeActivity.class);
                    intent.putExtra(Assets.CONTENT, linkHref);
                    intent.putExtra(Assets.FILENAME, filename);
                    startActivity(intent);
               }catch (Exception e){ Toast toast = Toast.makeText(getApplicationContext(),
                       e.toString(),
                       Toast.LENGTH_SHORT);
                   toast.show(); }

            }
        };



        listView.setOnItemClickListener(itemClickListener);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                download();

            }
        });


        if(savedInstanceState != null)

            linkTextList = savedInstanceState.getStringArrayList(LIST);

        else
                linkTextList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item,
                R.id.pro_item,
                linkTextList);


        if(savedInstanceState != null) {

            listView.setAdapter(adapter);
        }else {
            download();
        }


*/



  /*  private void download(){

        swipeRefreshLayout.setRefreshing(true);
        new loadCahce().execute();

    }*/



  //  @Override
 /*   public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_settings:



                return true;
            case R.id.action_downloaded:



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class loadCahce extends AsyncTask<String, Void, Boolean>{
        @Override
        protected  Boolean doInBackground(String ... arg){



                try {

                    Document doc = Jsoup.connect(Assets.PATH).get();
                    links = doc.select("a[href]");
                    linkTextList.clear();

                    for (Element link : links)
                   if (link.toString().contains("formul") && link.toString().contains(".html"))
                            linkTextList.add(link.text());

                    Iterator<Element> iterator = links.iterator();

                    while (iterator.hasNext()) {

                        Element el = iterator.next();

                        if (!el.toString().contains(".html"))
                            iterator.remove();
                    }


                    return true;

                } catch (Exception exp) {
                    exp.printStackTrace();
                    return false;
                }
        }

        @Override
        protected void onPostExecute(Boolean downloaded){

            swipeRefreshLayout.setRefreshing(false);

            if(!downloaded)
                    linkTextList = cacheSystem.loadListCachedFiles();

            else{
                if(Assets.LNKS == null)
                Assets.LNKS = new LinksMap(links);
            }

            adapter.clear();
            adapter.addAll(linkTextList);
            listView.setAdapter(adapter);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putStringArrayList(LIST, linkTextList);



    }



    private Runnable connectionChecker = new Runnable() {
        public void run() {



            while (!connector.isConnected(connectivityManager))
            {Log.v("in checker", "waitin' for connection");}

            new loadCahce().execute();
            Log.v("in checker", "leaving the checker");
        }
    };
*/
