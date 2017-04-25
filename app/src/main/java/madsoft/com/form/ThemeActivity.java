package madsoft.com.form;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThemeActivity extends Activity {

    private String text;
    private String title = "";
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_theme);


        if(savedInstanceState != null){

            text = savedInstanceState.getString(Assets.TEXT);

            title = savedInstanceState.getString(Assets.TITLE);

            TextView textView = (TextView) findViewById(R.id.text_content);

            textView.setText(text);

            textView = (TextView) findViewById(R.id.title);

            textView.setText(title);

        }else {

            content = getIntent().getStringExtra(Assets.CONTENT);

            new NewThread().execute();

        }
    }


    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected  String doInBackground(String ... arg){


            try{


                Document document = Jsoup.connect(Assets.THEME_PATH + content).get();

                title = document.title();

                Elements elements = document.select("p");


                for (Element element : elements)
                    text += element.text() + "\n";








            }catch (Exception exp){ exp.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String result){


            TextView titleView = (TextView) findViewById(R.id.title);

            titleView.setText(title);

            TextView textView = (TextView) findViewById(R.id.text_content);

            textView.setText(text);





        }

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString(Assets.TEXT, text);

        savedInstanceState.putString(Assets.TITLE, title);

    }


}
