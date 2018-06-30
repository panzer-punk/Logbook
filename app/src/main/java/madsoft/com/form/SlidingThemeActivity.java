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

    private Connector connector;

    private String href;
    private CacheSystem cacheSystem;
    private String filename;
    private String loaderInput;
    private HtmlTextView htmlTextView;

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

        filename = getIntent().getStringExtra(Assets.FILENAME);

        cacheSystem = new CacheSystem(this);

        setTitle(filename);

        htmlTextView = findViewById(R.id.activity_theme);

        connector = new Connector();

        if(cacheSystem.checkFile(filename)) {
            htmlTextView.setHtml(cacheSystem.load(filename), new HtmlHttpImageGetter(htmlTextView, null, false));

            ProgressBar progressBar = findViewById(R.id.progressBar);

            progressBar.setVisibility(View.GONE);

        }
        else
            new SlidingThemeActivity.ParseTask().execute(href);


    }

    private class ParseTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected  Boolean doInBackground(String ... arg) {




            try {

                Document document = Jsoup.connect(arg[0]).get();

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

                if (loaderInput != null) {

                   // if(Assets.DOWNLOADFLAG) Сделать для этого кнопку на самой активности
                    cacheSystem.write(loaderInput, filename);

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
