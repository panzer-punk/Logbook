package madsoft.com.form;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Iterator;

public class ThemeActivity extends Activity {

    private String text;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        content = getIntent().getStringExtra(Assets.CONTENT);

      new NewThread().execute();

    }


    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected  String doInBackground(String ... arg){


           // Log.d("Link", Jsoup.parse(content).attr("abs:href"));

            try{


                Document document = Jsoup.connect(Assets.THEME_PATH + content).get();



                Element element= document.select("div").first();





                   text = document.body().text();






            }catch (Exception exp){ exp.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String result){

            TextView textView = (TextView) findViewById(R.id.text_content);

            textView.setText(text);



        }

    }
}
