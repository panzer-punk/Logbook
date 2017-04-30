package madsoft.com.form;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends Activity {
    private   String list = "arrayList";
    private  String path = "http://matankkep.ru/formul/";
    public Elements links; // сохраняется в Assets
    public  ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {


                Document doc = Jsoup.parse(links.get((int) id).outerHtml());
                Element link = doc.select("a").first();
                String linkHref = link.attr("href");

                Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
                intent.putExtra(Assets.CONTENT, linkHref);
                startActivity(intent);
            }
        };

        listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(itemClickListener);

        adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.pro_item,
                arrayList);
        if(savedInstanceState != null) {
           arrayList = savedInstanceState.getStringArrayList(list);
            listView.setAdapter(adapter);
            links = Assets.LINKS;
        }else
        new Loader().execute();



    }

    public class Loader extends AsyncTask<String, Void, String>{
        @Override
        protected  String doInBackground(String ... arg){

            try{



                Document doc = Jsoup.connect(path).get();
                links = doc.select("a[href]");
                arrayList.clear();

                for( Element link : links)
                    if(link.toString().contains("formul") && link.toString().contains(".html"))
                        arrayList.add(link.text());

                Iterator<Element> iterator = links.iterator();

                while (iterator.hasNext()){

                    Element el = iterator.next();

                    if (!el.toString().contains(".html"))
                        iterator.remove();
                }

                Log.d("Size", "" + links.size());

            }catch (Exception exp){ exp.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String result){

            listView.setAdapter(adapter);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putStringArrayList(list, arrayList);
        Assets.LINKS = links;


    }




}
