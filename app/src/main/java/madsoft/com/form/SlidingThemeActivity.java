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


        htmlTextView = (HtmlTextView) findViewById(R.id.activity_theme);

        connector = new Connector();


        if(savedInstanceState != null){


            downloaded = savedInstanceState.getBoolean(Assets.DOWNLOAD_STATUS);

            if(downloaded){

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);

            }


            title = savedInstanceState.getString(Assets.TITLE);

            this.setTitle(title);

            loaderInput = savedInstanceState.getString(Assets.TEXT);

            htmlTextView.setHtml(loaderInput);



            new SlidingThemeActivity.ParseTask().execute(href);
        }else {

            new SlidingThemeActivity.ParseTask().execute(href);

        }
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

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

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


   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super(savedInstanceState);
        savedInstanceState.putString(Assets.TEXT, loaderInput);
        savedInstanceState.putString(Assets.TITLE, title);
    }*/


    private void toastMaker(String text){

        Toast toast = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_SHORT);
        toast.show();

    }

}
