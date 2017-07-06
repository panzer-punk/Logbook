package madsoft.com.form;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Даниил on 07.06.2017.
 */

public class Connector {

    public boolean isConnected(ConnectivityManager cm){

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connector", "close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
                return false;

            } catch (IOException e) {
                return false;
            }
        }

        return false;


    }

}
