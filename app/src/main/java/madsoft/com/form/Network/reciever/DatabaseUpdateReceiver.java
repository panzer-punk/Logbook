package madsoft.com.form.Network.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import madsoft.com.form.Fragment.DownloadedFragment;

public class DatabaseUpdateReceiver extends BroadcastReceiver {

    private DownloadedFragment downloadedFragment;

    public DatabaseUpdateReceiver(DownloadedFragment downloadedFragment) {
        this.downloadedFragment = downloadedFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       downloadedFragment.download();
    }
}
