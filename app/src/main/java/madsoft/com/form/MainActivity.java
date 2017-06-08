package madsoft.com.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends Activity {
    private static  String LIST = "arrayList";
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Connection connection;
    private ConnectivityManager connectivityManager;
    private boolean success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection = new Connection();

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);



        Log.v("Bundle", "" + (savedInstanceState == null));

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {


               try {
                   Document doc = Jsoup.parse(links.get((int) id).outerHtml());
                   Element link = doc.select("a").first();
                   String linkHref = link.attr("href");


                    Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
                    intent.putExtra(Assets.CONTENT, linkHref);
                    startActivity(intent);
               }catch (Exception e){ Toast toast = Toast.makeText(getApplicationContext(),
                       "Отсутсвует подключение к сети",
                       Toast.LENGTH_SHORT);
                   toast.show();}

            }
        };
       // 0x7f0b0056 0x7f0b0055
        listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(itemClickListener);

        if(savedInstanceState != null)
            arrayList = savedInstanceState.getStringArrayList(LIST);
            else
                arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.pro_item,
                arrayList);


        if(savedInstanceState != null) {

            listView.setAdapter(adapter);
            links = Assets.LINKS;
        }else {
            new NewThread().execute();
        }




    }

    public class NewThread extends AsyncTask<String, Void, String>{
        @Override
        protected  String doInBackground(String ... arg){

            if(connection.isConnected(connectivityManager)) {

                try {

                    Document doc = Jsoup.connect(Assets.PATH).get();
                    links = doc.select("a[href]");
                    arrayList.clear();

                    for (Element link : links)
                        if (link.toString().contains("formul") && link.toString().contains(".html"))
                            arrayList.add(link.text());

                    Iterator<Element> iterator = links.iterator();

                    while (iterator.hasNext()) {

                        Element el = iterator.next();

                        if (!el.toString().contains(".html"))
                            iterator.remove();
                    }

                    Log.d("Size", "" + links.size());

                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }else{
                success = false;
                Thread thread = new Thread(connectionChecker);
                thread.start();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){

            listView.setAdapter(adapter);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putStringArrayList(LIST, arrayList);
        Assets.LINKS = links;


    }

    private Runnable connectionChecker = new Runnable() {
        public void run() {
            while (!success){
                Log.v("in checker"," in while");
                if(connection.isConnected(connectivityManager)) {
                    new NewThread().execute();
                    success = true;
                    Log.v("in checker", "leaving the checker");
                }


            }
        }
    };

}