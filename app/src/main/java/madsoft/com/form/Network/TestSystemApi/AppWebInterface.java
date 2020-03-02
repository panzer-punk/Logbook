package madsoft.com.form.Network.TestSystemApi;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class AppWebInterface {

    Context mContext;


    AppWebInterface(Context c) {
        mContext = c;
    }


    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

}
