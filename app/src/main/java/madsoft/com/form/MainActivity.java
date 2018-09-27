package madsoft.com.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity{
    private static  String LIST = "linkTextList";
    private CacheSystem cacheSystem;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> linkTextList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Connector connector;
    private ConnectivityManager connectivityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connector = new Connector();

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        cacheSystem = new CacheSystem(this);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

               download();

            }
        });

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);


        Log.v("Bundle", "" + (savedInstanceState == null));

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {


               try {
                   //
                   Document doc = Jsoup.parse(Assets.LNKS.getLink((lisView.getItemAtPosition(position).toString())).outerHtml());
                   Element link = doc.select("a").first();
                   String linkHref = link.attr("href");
                   String filename = lisView.getItemAtPosition(position).toString();


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

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(itemClickListener);

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




    }

    private void download(){

        swipeRefreshLayout.setRefreshing(true);
        new DownloadTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_settings:

                 intent = new Intent(this, SettingsActivity.class);

                startActivity(intent);

                return true;
            case R.id.action_about:

                intent = new Intent(this, AboutActivity.class);

                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, Boolean>{
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

            if(!downloaded){
                if(cacheSystem.checkFile(Assets.ARRAYLIST)) {
                    linkTextList = cacheSystem.loadArrayList(Assets.ARRAYLIST);
                    listView.setAdapter(adapter);
                }
              Thread thread = new Thread(connectionChecker);
                thread.start();
            }else {
                listView.setAdapter(adapter);
                cacheSystem.write(linkTextList, Assets.ARRAYLIST);
                if(Assets.LNKS == null)
                Assets.LNKS = new LinksMap(links);
                swipeRefreshLayout.setRefreshing(false);
            }
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
            
            new DownloadTask().execute();
            Log.v("in checker", "leaving the checker");
        }
    };

}