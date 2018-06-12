package madsoft.com.form;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;


public class ThemeActivity extends extends SlidingActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_theme);

        href = Assets.THEME_PATH + getIntent().getStringExtra(Assets.CONTENT);

        context = getApplicationContext();

        htmlTextView = (HtmlTextView) findViewById(R.id.activity_theme);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

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



            new ParseTask().execute(href);
        }else {

            new ParseTask().execute(href);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_theme, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent(href);
        return super.onCreateOptionsMenu(menu);
    }

    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
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

                if (loaderInput != null) {


                    htmlTextView.setHtml(loaderInput,
                             new HtmlHttpImageGetter(htmlTextView));
                }

            }else{
                toastMaker("Страница будет загружена при подлкючении к сети.");
                Thread thread = new Thread(connectionChecker);
                thread.start();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString(Assets.TEXT, loaderInput);
        savedInstanceState.putString(Assets.TITLE, title);
    }

    private Runnable connectionChecker = new Runnable() {
        public void run() {
            while (!connector.isConnected(connectivityManager)){Log.v("in checker"," in while");}

            new ParseTask().execute(href);
            Log.v("in checker", "leaving the checker");
        }
    };

    private void toastMaker(String text){

        Toast toast = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_SHORT);
        toast.show();

    }

}
