package madsoft.com.form;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.LinkedList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ThemeActivity extends Activity {

    private String text;
    private String title = "";
   // private Context context = this;
    private String imageLink;
    private String href;
    private ImageLoader imageLoader;
    private LinkedList<String> loaderInput;
    private boolean downloaded = false;
    private ShareActionProvider shareActionProvider;
    private StringBuilder builder;
    private LinearLayout linearLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_theme);

        href = Assets.THEME_PATH + getIntent().getStringExtra(Assets.CONTENT);

        context = getApplicationContext();

       imageLoader = ImageLoader.getInstance();

       imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        linearLayout = (LinearLayout) findViewById(R.id.activity_theme);



        if(savedInstanceState != null){


            downloaded = savedInstanceState.getBoolean(Assets.DOWNLOAD_STATUS);

            if(downloaded){

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);

            }


            title = savedInstanceState.getString(Assets.TITLE);

            TextView textView;

            textView = (TextView) findViewById(R.id.title);

            textView.setText(title);

            href = savedInstanceState.getString(Assets.HREF);

          /* text = savedInstanceState.getString(Assets.TEXT);



            href = savedInstanceState.getString(Assets.HREF);

            imageLink = savedInstanceState.getString(Assets.IMAGE_LINK);//убрать из финальной версии

            TextView textView = (TextView) findViewById(R.id.text_content);

            textView.setText(text);

            textView = (TextView) findViewById(R.id.title);

            textView.setText(title);

            ImageView imageView = (ImageView) findViewById(R.id.image);

            ImageLoader imageLoader;
            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageLink, imageView);

            PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

            attacher.setZoomable(true);*/

            new Parser().execute(href);
        }else {

            new Parser().execute(href);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public class Parser extends AsyncTask<String, Void, String> {
        @Override
        protected  String doInBackground(String ... arg){



            try{

               // href = Assets.THEME_PATH + arg[0];

                Document document = Jsoup.connect(arg[0]).get();

                title = document.title();

                madsoft.com.form.Parser parser = new madsoft.com.form.Parser(document);

                loaderInput = parser.parseConten();




            }catch (Exception exp){ exp.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String result){

            Boolean nextImage = false;

            String out = "";

            if(!downloaded) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);
            }

            for(String s : loaderInput) {

                if(s.contains("https") || s.contains("http")) {

                    if (!out.isEmpty()){
                        TextView textView = new TextView(context);

                        textView.setTextColor(Color.BLACK);

                        textView.setTextSize(18);

                        textView.setText(out);

                        linearLayout.addView(textView);

                        out = "";
                    }
                    ImageView imageView = new ImageView(context);

                    imageView.setImageResource(R.drawable.ic_share_black_18dp);

                    linearLayout.addView(imageView);

                    ImageLoader imageLoader;
                    imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(s, imageView);

                    PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

                    attacher.setZoomable(true);


                }else{

                    out += s;

                }

            }

            if(!out.isEmpty()){
                TextView textView = new TextView(context);

                textView.setTextColor(Color.BLACK);

                textView.setTextSize(18);

                textView.setText(out);

                linearLayout.addView(textView);
            }

            TextView titleView = (TextView) findViewById(R.id.title);

            titleView.setText(title);

            GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

            gridLayout.setVisibility(View.VISIBLE);
         /*



            TextView textView = (TextView) findViewById(R.id.text_content);

            textView.setText(text);



            ImageView imageView = (ImageView) findViewById(R. id.image);






            imageLoader.displayImage(imageLink, imageView);

            PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

            attacher.setZoomable(true);*/

            downloaded = true;




        }

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString(Assets.TEXT, text);

        savedInstanceState.putString(Assets.TITLE, title);

        savedInstanceState.putString(Assets.HREF, href);

        savedInstanceState.putString(Assets.IMAGE_LINK, imageLink);//убрать из финальной версии

        savedInstanceState.putBoolean(Assets.DOWNLOAD_STATUS,downloaded);



    }


}
