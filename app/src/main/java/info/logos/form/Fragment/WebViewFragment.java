package info.logos.form.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import info.logos.form.Activity.SlidingThemeActivity;
import info.logos.form.R;
/**/

public class WebViewFragment extends Fragment implements AppWebClientCallback {
    private static final String READ_MODE_KEY = "READ_MODE";
    private static final String URL_KEY = "URL";
    loadLocalFile loader;
    private WebView webView;
    private boolean readMode;
    private SlidingThemeActivity parent;
    private String url;
    private AppWebClient fragmentWebClient;

    @Override
    public void update(String query) {

        if(!readMode)
        webView.loadUrl(query+"?d=android");
        else {

        loader = new loadLocalFile(webView);
        loader.execute(query);
          //  webView.loadUrl("file:///android_asset/test.html");
        }
    }

    @Override
    public void onLoadFinished() {
        parent.loadFinished();
    }

    @Override
    public void setTitle() {

        if(!parent.isTitleSet())
        parent.setTitle(webView.getTitle());

    }


    public WebViewFragment(){}

    public WebViewFragment(SlidingThemeActivity parent, String url, boolean localFile) {
        this.parent = parent;
        this.url = url;
        readMode = localFile;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentWebClient = new AppWebClient(this);
        if(savedInstanceState != null){
            parent = (SlidingThemeActivity) getActivity();
            readMode = savedInstanceState.getBoolean(READ_MODE_KEY);
            url = savedInstanceState.getString(URL_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_webview, null);
        webView = view.findViewById(R.id.activity_theme_webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.addJavascriptInterface(parent, "Android");
        webView.setWebViewClient(fragmentWebClient);
       update(url);
        return view;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL_KEY, url);
        outState.putBoolean(READ_MODE_KEY, readMode);
    }

    public boolean canGoBack(){
        return webView.canGoBack();
    }

    public void goBack(){
        webView.goBack();
    }


    public void update() {
        if(readMode)
            return;

        parent.updateUrl(url);
        webView.loadUrl(url+"?d=android");
    }
}
class AppWebClient extends WebViewClient{
    public static String WEB_CLIENT_BASE_URL = "https://rpspn.000webhostapp.com/posts";
    private String query;
    private AppWebClientCallback clientCallback;



    public AppWebClient(AppWebClientCallback clientCallback) {
    this.clientCallback = clientCallback;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // do your stuff here
        clientCallback.setTitle();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView  view, WebResourceRequest request){
        Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
        view.getContext().startActivity(intent);
        return true;
    }



}
class loadLocalFile extends AsyncTask<String, Integer, String>{
    private WebView webView;

    public loadLocalFile(WebView webView) {
        this.webView = webView;
    }

    @Override
    protected String doInBackground(String... strings){
        File f = new File(strings[0]);
        Document document = null;
        try {
            document = Jsoup.parse(f, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(document == null || document.outerHtml() == null){
            return "<h2>Невозможно отобразить файл, возможно, он был удален</h2>";
        }
        return document.outerHtml();
    }

    @Override
    protected void onPostExecute(String s) {
        webView.loadDataWithBaseURL("file:///android_asset/", s,"text/html", "UTF-8", null);
    }
}

interface AppWebClientCallback{
     void update(String query);
     void onLoadFinished();
     void setTitle();
}
