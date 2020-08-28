package info.logos.form.Activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import info.logos.form.Application.MyApplication;
import info.logos.form.Assets;
import info.logos.form.DataBase.PageDao;
import info.logos.form.DataBase.entity.Page;
import info.logos.form.Fragment.QuizFragment;
import info.logos.form.Fragment.WebViewFragment;
import info.logos.form.Network.Objects.ArticleWp;
import info.logos.form.Network.Objects.DataEntity;
import info.logos.form.R;
import info.logos.form.service.DownloadService;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static info.logos.form.Activity.MainActivity.WRITE_FILE_PERMISSION;


public class SlidingThemeActivity extends AppCompatActivity{

    public static String READ_MODE = "READ_MODE";


    private WebViewFragment articleFragment;
    private String href, shareLink;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private String filename;
    private TextView title;
    private TextView content;
    private DataEntity article;
    private UpdateArticleInCache updateArticleInCache;
    private boolean readMode, titleSet;
    private SharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        makeContent(savedInstanceState);
    }


    public void makeContent(Bundle savedInstanceState) {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(DownloadService.BUNDLE_KEY);
        if(bundle != null) {
            article = (DataEntity) bundle.get(DownloadService.BUNDLE_MESSAGE_KEY);
        }
        String action = intent.getAction();
        switch (action){

            case Intent.ACTION_VIEW:

                href = intent.getData().toString();
                filename = getString(R.string.loading);// intent.getData().getPath().replace(getString(R.string.deeplink_prefix), " ");//TODO кэширование страниц которые открыли через браузер
                titleSet = false;
                break;
                default:
                    titleSet = true;
                    if(article != null) {
                        filename = article.getTitleS();
                        href = article.getUrl();
                    }else {
                        filename = intent.getStringExtra(Assets.TITLE);
                        href = intent.getStringExtra(Assets.LINK);
                    }
                    readMode = intent.getBooleanExtra(READ_MODE, false);
                    shareLink = intent.getStringExtra(Assets.LINK);
        }





        setTitle(filename);

        articleFragment = new WebViewFragment(this, href, readMode);
        replaceFragment(articleFragment);


        title = findViewById(R.id.diaog_title);
        content = findViewById(R.id.diaog_text);


        toolbar = findViewById(R.id.toolbar_theme);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            toolbar.setTitleTextColor(Color.BLACK);
            toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        }

        progressBar = findViewById(R.id.theme_progressbar);

        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(appSharedPreferences.getBoolean("auto_download", false))
            download();

       if(article != null) {
           updateArticleInCache = new UpdateArticleInCache();
           final int id = article.getId();
           updateArticleInCache.execute(id, -1);
       }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_theme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            case R.id.action_download:
                if(!readMode && !getTitle().toString().equals(getString(R.string.loading)))
               askWritePermission();
                return true;
            case R.id.action_update:
                articleFragment.update();
                Toast.makeText(this, R.string.theme_update_action_toast, Toast.LENGTH_SHORT)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void download() {//TODO Загружка файла через Service

        if (article == null){
        //    Toast.makeText(this, R.string.download_error_not_supported, Toast.LENGTH_SHORT)
      //              .show();
            Page page = new Page();
            page.id = -1;
            page.categories = "0";
            page.modified = " ";
            page.shareLink = href;
            page.imagePath = " ";
            page.title = getTitle().toString().replace(" — Logos", " ");
            page.path = href;
            article = page;
         //   return;
    }
        Bundle serviceBundle = new Bundle();
        serviceBundle.putSerializable(DownloadService.BUNDLE_MESSAGE_KEY, article);
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY, serviceBundle);
        startService(intent);

    }

    private void askWritePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_FILE_PERMISSION);
        }else
            download();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MainActivity.WRITE_FILE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Intent intent = new Intent(getActivity(), DownloadService.class);
                    //  intent.putExtra(DownloadService.URL_INTENT_KEY, url);
                    //      getActivity().startService(intent);
                    download();
                   // pageFragment = (PageFragment) pagerAdapter.getItem(pager.getCurrentItem());
                  //  pageFragment.downloadArticle();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        if(shareLink == null)
        sendIntent.putExtra(Intent.EXTRA_TEXT, href);
        else
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink);

        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (articleFragment.canGoBack()) {
                        articleFragment.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public void getTest(String toast) {
        QuizFragment.display(getSupportFragmentManager(), toast);
    }

    public boolean isTitleSet()
    {return titleSet;}

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

    private void  replaceFragment (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadFinished() {
        progressBar.setVisibility(View.GONE);
    }

    public void updateUrl(String url) {

        href = url;

    }


    class UpdateArticleInCache extends AsyncTask<Integer, String, Integer>{
        private final int updateCode = 1,
                loadCode = 0,
                errorCode = -1,
                upToDateCode = 2,
                noToastCode = 3;

        @Override
        protected Integer doInBackground(Integer... id) {
            if(readMode) {
                return noToastCode;
            }
            PageDao dao = MyApplication.getDatabase().pageDao();
            Page page = dao.loadById(id[0]);
            if (page == null){
                if(id[1] > 0) {
                    return loadCode;
                }
                return noToastCode;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String snewDate = article.getModified();
            if(snewDate == null)
                return noToastCode;
            snewDate = snewDate.replace('T', ' ');
            String soldDate = page.modified.replace('T', ' ');
            try {
                Date newDate = dateFormat.parse(snewDate);
                Date oldDate = dateFormat.parse(soldDate);
                if(newDate.compareTo(oldDate) > 0){
                    return updateCode;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return errorCode;
            }



            return upToDateCode;
        }


        @Override
        protected void onPostExecute(Integer code) {

            switch (code){
                case loadCode:
                    download();
                    break;
                case updateCode:
                    Toast.makeText(getApplicationContext(), R.string.cache_update, Toast.LENGTH_SHORT)
                            .show();
                    download();
                    break;
                case errorCode:
                    Toast.makeText(getApplicationContext(), R.string.error_load, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case upToDateCode:
                   /* Toast.makeText(getApplicationContext(), R.string.cache_uptodate, Toast.LENGTH_SHORT)
                            .show();*/
                    break;
                case noToastCode:
                    break;
            }
        }
    }

}


