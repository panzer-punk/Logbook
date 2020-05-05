package madsoft.com.form.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.R;
/**/

public class WebViewFragment extends Fragment implements AppWebClientCallback {
    @Override
    public void update(String query) {

        webView.loadUrl(query+"?d=android");

    }


    private WebView webView;
    private SlidingThemeActivity parent;
    private String url;
    private AppWebClient fragmentWebClient;

    public WebViewFragment(SlidingThemeActivity parent, String url) {
        this.parent = parent;
        this.url = url;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentWebClient = new AppWebClient(this);
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

    public boolean canGoBack(){
        return webView.canGoBack();
    }

    public void goBack(){
        webView.goBack();
    }


}
class AppWebClient extends WebViewClient{
    public static String WEB_CLIENT_BASE_URL = "https://rpspn.000webhostapp.com/posts";
    private String query;
    private AppWebClientCallback clientCallback;

    public AppWebClient(AppWebClientCallback clientCallback) {
    this.clientCallback = clientCallback;
    }

    private void handleLink(String url){
       /* if( url.contains(WEB_CLIENT_BASE_URL) ){
            if(url.contains("/category/")){
                url = url.replace(WEB_CLIENT_BASE_URL, "");
                query = url.replace("/category/", "").replace("-", " ");
                clientCallback.setCategoryFragment(query);
            }else if(url.contains("/author/")){
                url = url.replace(WEB_CLIENT_BASE_URL, "");
                query = url.replace("/author/", "").replace("-", " ");
                clientCallback.setAuthorFragment(query);
            }
        }*/
        clientCallback.update(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView  view, String  url){
        handleLink(url);
        return true;
    }

}
interface AppWebClientCallback{
    public void update(String query);
}
