package madsoft.com.form.Activity;


import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import madsoft.com.form.FileSystem.CacheSystem;
import madsoft.com.form.Network.Html.Connector;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.R;

import android.view.LayoutInflater;
import android.view.View;

import android.webkit.WebView;
import android.widget.TextView;


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

        filename = getIntent().getStringExtra(ArticleWp.TITLE);
        href = getIntent().getStringExtra(ArticleWp.LINK);


        cacheSystem = new CacheSystem(this);

        setTitle(filename);

        webView = findViewById(R.id.activity_theme);
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(href + "?d=android");

        title = findViewById(R.id.diaog_title);
        content = findViewById(R.id.diaog_text);


        toolbar = findViewById(R.id.toolbar_theme);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        connector = new Connector();


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


