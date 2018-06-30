package madsoft.com.form;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.klinker.android.sliding.SlidingActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class SlidingThemeActivity extends SlidingActivity {

    private String title = "";
    // private Context context = this;
    private Connector connector;
    private ConnectivityManager connectivityManager;

    private String href;
    private ImageLoader imageLoader;
    private String loaderInput;
    private boolean downloaded = false;
    private ShareActionProvider shareActionProvider;
    private HtmlTextView htmlTextView;
    private Context context;

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle(R.string.loading);

        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );

        setContent(R.layout.activity_theme);

        this.makeContent(savedInstanceState);


    }

    public void makeContent(Bundle savedInstanceState){
        href = Assets.THEME_PATH + getIntent().getStringExtra(Assets.CONTENT);


        htmlTextView = findViewById(R.id.activity_theme);

        connector = new Connector();


            new SlidingThemeActivity.ParseTask().execute(href);


    }

    private class ParseTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected  Boolean doInBackground(String ... arg) {




            try {

                Document document = Jsoup.connect(arg[0]).get();

                title = document.title();



                madsoft.com.form.Parser parser = new madsoft.com.form.Parser(document);

                loaderInput = parser.parseContent();

                parser = null;

                return true;


            } catch (Exception exp) {
                exp.printStackTrace();
                return false;
            }


        }




        @Override
        protected void onPostExecute(Boolean downloaded) {

            if (downloaded) {

                ProgressBar progressBar = findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);

                setTitle(title);

                if (loaderInput != null) {


                    htmlTextView.setHtml(loaderInput,
                            new HtmlHttpImageGetter(htmlTextView, null, false));
                }

            }else{
                toastMaker("Страница будет загружена при подлкючении к сети.");
            }
        }
    }


    private void toastMaker(String text){

        Toast toast = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_SHORT);
        toast.show();

    }

}
