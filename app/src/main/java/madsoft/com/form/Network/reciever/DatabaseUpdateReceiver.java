package madsoft.com.form.Network.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import madsoft.com.form.DataBase.entity.Page;
import madsoft.com.form.Fragment.DownloadedFragment;
import madsoft.com.form.service.DownloadService;

public class DatabaseUpdateReceiver extends BroadcastReceiver {

    private DownloadedFragment downloadedFragment;

    public DatabaseUpdateReceiver(DownloadedFragment downloadedFragment) {
        this.downloadedFragment = downloadedFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Page newPage = (Page) intent.getBundleExtra(DownloadService.BUNDLE_KEY).getSerializable(DownloadService.BUNDLE_MESSAGE_KEY);
       downloadedFragment.insertPage(newPage);
    }
}
