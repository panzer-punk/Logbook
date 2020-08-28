package info.logos.form.Network.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import info.logos.form.DataBase.entity.Page;
import info.logos.form.Fragment.DownloadedFragment;
import info.logos.form.service.DownloadService;

public class DatabaseUpdateReceiver extends BroadcastReceiver {

    private DownloadedFragment downloadedFragment;

    public DatabaseUpdateReceiver(DownloadedFragment downloadedFragment) {
        this.downloadedFragment = downloadedFragment;
    }

    public DatabaseUpdateReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Page newPage = (Page) intent.getBundleExtra(DownloadService.BUNDLE_KEY).getSerializable(DownloadService.BUNDLE_MESSAGE_KEY);
       downloadedFragment.insertPage(newPage);
    }
}
