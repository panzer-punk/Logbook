package madsoft.com.form.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;


import madsoft.com.form.Application.MyApplication;
import madsoft.com.form.DataBase.PageDao;
import madsoft.com.form.DataBase.entity.Page;
import madsoft.com.form.Fragment.DownloadedFragment;
import madsoft.com.form.Network.Objects.ArticleWp;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DownloadService extends Service {

  //  public static String URL_INTENT_KEY = "URL";
    public static String HOST_URL = "https://sanctumlogos.info/";
  //  public static String MODIFIED_KEY = "MODIFIED";
    public static String BUNDLE_KEY = "BUNDLE";
    public static String BUNDLE_MESSAGE_KEY = "BUNDLE_MESSAGE";


   // private Queue<String> downloadUrls;
    private ServiceHandler downloadHandler;
    private Looper mServiceLooper;


    private final class ServiceHandler extends Handler {
      //  private DownloadManager mDownloadManager;
        private PageDao servicePageDao;
        private Document mDoc;
        {
         //   mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            servicePageDao = MyApplication.getDatabase().pageDao();

        }
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            ArticleWp currentArticle = (ArticleWp) msg.obj;

            File dir = getApplicationContext().getFilesDir();
            String path = dir.getAbsolutePath();
            if(!dir.exists()) {
                dir.mkdirs();
            }

            try {
                String murl = new String(currentArticle.getLink().getBytes(),UTF_8);
                Log.i("URL", murl);

                mDoc = Jsoup.connect(murl + "?d=android")
                        .get();
                Element head = mDoc.head();
                Elements styles = head.getElementsByTag("link");
                for(Element s : styles){

                    if(s.attr("rel").equals("stylesheet")
                            && s.attr("href").contains(HOST_URL)){
                    s.attr("href", s.attr("id") + ".css");
                    }else
                        s.remove();

                }
                Elements scripts = head.getElementsByTag("script");
                scripts.remove();

                final File f = new File(dir.getAbsolutePath() + "/"+ mDoc.title() + ".html");
                Page page = new Page();
                page.modified = currentArticle.getModified();
                page.id = currentArticle.getId();
                page.path = f.getAbsolutePath();
                page.shareLink = currentArticle.getLink();
                page.title = currentArticle.getTitle().getRendered();
                page.imagePath = currentArticle.getJetpackFeaturedMediaUrl();//TODO передать путь к картинке локальной!
               // if(!f.exists()) {
                    FileUtils.writeStringToFile(f, mDoc.outerHtml(), "UTF-8");
                    servicePageDao.insert(page);
                    Intent updateCacheListIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_MESSAGE_KEY, page);
                    updateCacheListIntent.putExtra(BUNDLE_KEY, bundle);
                    updateCacheListIntent.setAction(DownloadedFragment.RECEIVER_ACTION);
                    updateCacheListIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(updateCacheListIntent);
              //  }//TODO обновоить уже созданый файл
               Log.d("File path", f.getAbsolutePath());


            } catch (IOException e) {
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
        Bundle serviceBundle = intent.getBundleExtra(BUNDLE_KEY);
        ArticleWp articleWp = (ArticleWp) serviceBundle.get(BUNDLE_MESSAGE_KEY);
       // String url = intent.getStringExtra(URL_INTENT_KEY);
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = downloadHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = articleWp;
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
