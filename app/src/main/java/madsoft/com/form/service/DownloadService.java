package madsoft.com.form.service;


import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.Network.WpApi.WpApi;
import madsoft.com.form.R;

public class DownloadService extends Service {

    public static String URL_INTENT_KEY = "URL";



   // private Queue<String> downloadUrls;
    private ServiceHandler downloadHandler;
    private Looper mServiceLooper;


    private final class ServiceHandler extends Handler {
        private DownloadManager mDownloadManager;
        private Document mDoc;
        {
            mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        }
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            try {
                mDoc = Jsoup.connect(msg.obj.toString()).get();
                Element head = mDoc.head();
                Elements styles = head.getElementsByTag("link");
                for(Element s : styles){

                    if(s.attr("rel").equals("stylesheet")
                            && s.attr("href").contains(NetworkService.BASE_URL)){

                       DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s.attr("src")))
                               .setTitle(s.attr("id"))
                               .setDescription(getString(R.string.loading))
                               .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                               .setDestinationUri(Uri.fromFile(new File(mDoc.title() + "/" + s.attr("id") + ".css")))
                               .setAllowedOverMetered(true)
                               .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

                    s.attr("href", s.attr("id") + ".css");

                    mDownloadManager.enqueue(request);
                    mDownloadManager.wait();
                    }else
                        s.remove();

                }
                Elements scripts = head.getElementsByTag("script");
                for (Element s : scripts) {

                    if (s.attr("type").equals("text/javascript")
                            && s.attr("src").contains(NetworkService.BASE_URL)) {

               /* DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s.attr("src")))
                        .setTitle(s.attr("id"))
                        .setDescription(getString(R.string.loading))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationUri(Uri.fromFile(new File(s.attr("id") + ".css")))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network*/

                       // s.attr("src", s.attr("id") + ".js");
                        s.remove();

                    } else
                        s.remove();

                }

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(msg.obj.toString()))
                        .setTitle(mDoc.title())
                        .setDescription(getString(R.string.loading))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationUri(Uri.fromFile(new File(mDoc.title() + "/" + "index.html")))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
            } catch (IOException e) {
                e.printStackTrace();
                stopSelf(msg.arg1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopSelf(msg.arg1);
            }

            stopSelf(msg.arg1);
        }
    }

    public void onCreate() {
       // downloadUrls = new SynchronousQueue<>();
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        downloadHandler = new ServiceHandler(mServiceLooper);
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        String url = intent.getStringExtra(URL_INTENT_KEY);
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = downloadHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = url;
        downloadHandler.sendMessage(msg);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
