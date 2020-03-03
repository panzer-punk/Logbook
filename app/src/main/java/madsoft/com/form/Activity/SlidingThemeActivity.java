package madsoft.com.form.Activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import madsoft.com.form.FileSystem.CacheSystem;
import madsoft.com.form.Fragment.QuizFragment;
import madsoft.com.form.Network.Html.Connector;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.R;

import android.view.LayoutInflater;
import android.view.View;

import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


public class SlidingThemeActivity extends AppCompatActivity {

    private Connector connector;

    private String href;
    private Toolbar toolbar;
    private CacheSystem cacheSystem;
    private String filename;
    private WebView webView;
    private TextView title;
    private TextView content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        makeContent(savedInstanceState);
    }


    public void makeContent(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String action = intent.getAction();
        switch (action){

            case Intent.ACTION_VIEW:

                href = intent.getData().toString();
                filename = intent.getData().getPath().replace(getString(R.string.deeplink_prefix), " ");
                break;
                default:
                    filename = intent.getStringExtra(ArticleWp.TITLE);
                    href = intent.getStringExtra(ArticleWp.LINK);
        }



        cacheSystem = new CacheSystem(this);

        setTitle(filename);

        webView = findViewById(R.id.activity_theme);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.addJavascriptInterface(this, "Android");
        webView.loadUrl(href + "?d=android");

        title = findViewById(R.id.diaog_title);
        content = findViewById(R.id.diaog_text);


        toolbar = findViewById(R.id.toolbar_theme);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        connector = new Connector();


    }


    @JavascriptInterface
    public void getTest(String toast) {
        QuizFragment.display(getSupportFragmentManager(), toast);
    }


    public void dialogMaker(String titleS, String contentS) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_term, null);
        title = dialogView.findViewById(R.id.diaog_title);
        content = dialogView.findViewById(R.id.diaog_title);
        title.setText(titleS);
        content.setText(contentS);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();



    }


}



