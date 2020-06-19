package info.logos.form.Network.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkConnectionReceiver extends BroadcastReceiver {
    private List<Updatable> observers;

    {
        observers = new ArrayList<>();
    }

    public NetworkConnectionReceiver(Updatable updatable) {
        observers.add(updatable);
    }

    public interface Updatable{
        void onNetworkConnection();
    }

    public void subscribe(Updatable updatable){
        if(!observers.contains(updatable))
        observers.add(updatable);
    }

    public void unsubscribe(Updatable updatable){
        observers.remove(updatable);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

            for(Updatable u : observers)
                u.onNetworkConnection();

        }
    }
}
