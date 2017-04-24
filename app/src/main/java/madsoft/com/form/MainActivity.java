package madsoft.com.form;

import android.app.Activity;
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

public class MainActivity extends Activity {
    private static  String LIST = "arrayList";
    public Elements links; // сохраняется в Assets
    public static ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> lisView, View view, int position, long id) {

                Log.d("list_item clicked", arrayList.get((int)id));
                Log.d("Links test", links.get(0).text());

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
           arrayList = savedInstanceState.getStringArrayList(LIST);
            listView.setAdapter(adapter);
            links = Assets.LINKS;
        }else
        new NewThread().execute();



    }

    public class NewThread extends AsyncTask<String, Void, String>{
        @Override
        protected  String doInBackground(String ... arg){
            Document doc;

            try{

                doc = Jsoup.connect("http://matankkep.ru/formul/").get();
                links = doc.select("a[href]");
                arrayList.clear();

                for( Element link : links)
                    if(link.toString().contains("formul") && link.toString().contains(".html"))
                    arrayList.add(link.text());

            }catch (Exception exp){}

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




}
