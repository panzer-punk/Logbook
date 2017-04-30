package madsoft.com.form;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThemeActivity extends Activity {

    private String text;
    private String title = "";
    private Context context = this;
    private String imageLink;

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

            new Parser().execute(getIntent().getStringExtra(Assets.CONTENT));

        }
    }


    public class Parser extends AsyncTask<String, Void, String> {
        @Override
        protected  String doInBackground(String ... arg){

            StringBuilder builder;

            try{


                Document document = Jsoup.connect(Assets.THEME_PATH + arg[0]).get();

                title = document.title();

                Elements elements = document.getAllElements();






                builder = new StringBuilder();

                for(Element e : elements) {

                    switch (e.tag().toString()) {

                        case "p":

                            if (!(e.hasClass("hidden-lg hidden-md col-sm-12 col-xs-12")
                                    || e.hasClass("text-center text-main-css")
                                    || e.hasClass("text-muted pull-right")))
                                if (!e.text().isEmpty())
                                   builder.append(e.text()).append("\n");




                            break;
                        case "ul":
                            if (!(e.hasClass("nav navbar-nav")
                                    || e.hasClass("nav navbar-nav navbar-right")
                                    || e.hasClass("breadcrumb")
                                    || e.hasClass("list-unstyled list-inline pull-left")))
                                if (!e.text().isEmpty())
                                    builder.append(e.text()).append("\n");

                            break;

                        case "img":
                            if (!e.hasAttr("style")) {
                                if (e.attr("src").contains(Assets.ROOT)) {
                                    imageLink = e.attr("src");


                                }else
                                    imageLink = Assets.ROOT + e.attr("src");

                            }

                                Log.d("Image", imageLink);
                            break;

                    }

                }

                text = builder.toString();






            }catch (Exception exp){ exp.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String result){



            int c = 0;





            TextView titleView = (TextView) findViewById(R.id.title);

            titleView.setText(title);

            TextView textView = (TextView) findViewById(R.id.text_content);

            textView.setText(text);


            ImageView imageView = (ImageView) findViewById(R. id.image);
            ImageLoader imageLoader;
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage(imageLink, imageView);
            




        }

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString(Assets.TEXT, text);

        savedInstanceState.putString(Assets.TITLE, title);

    }


}
