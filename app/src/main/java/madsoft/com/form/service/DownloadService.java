package madsoft.com.form.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import madsoft.com.form.Activity.SlidingThemeActivity;
import madsoft.com.form.Application.MyApplication;
import madsoft.com.form.DataBase.PageDao;
import madsoft.com.form.DataBase.converter.CategoriesConverter;
import madsoft.com.form.DataBase.entity.Page;
import madsoft.com.form.Fragment.DownloadedFragment;
import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.DataEntity;
import madsoft.com.form.R;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Calendar.getInstance;

public class DownloadService extends Service {

    private static final String CHANNEL_ID = "download_1";
    //  public static String URL_INTENT_KEY = "URL";
    public static String HOST_URL = "https://sanctumlogos.info/";
  //  public static String MODIFIED_KEY = "MODIFIED";
    public static String BUNDLE_KEY = "BUNDLE";
    public static String BUNDLE_MESSAGE_KEY = "BUNDLE_MESSAGE";


   // private Queue<String> downloadUrls;
    private ServiceHandler downloadHandler;
    private CategoriesConverter categoriesConverter;
    private Looper mServiceLooper;
    private  SimpleDateFormat dateFormat;


    private final class ServiceHandler extends Handler {
      //  private DownloadManager mDownloadManager;
        private PageDao servicePageDao;
        private Document mDoc;
        {
         //   mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            servicePageDao = MyApplication.getDatabase().pageDao();
            categoriesConverter = new CategoriesConverter();

        }
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            DataEntity currentArticle = (DataEntity) msg.obj;

            File dir = getApplicationContext().getFilesDir();
            String path = dir.getAbsolutePath();
            if(!dir.exists()) {
                dir.mkdirs();
            }

            try {
                String murl = new String(currentArticle.getUrl().getBytes(),UTF_8);
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

                if(currentArticle.getModified() != null) {
                    page.modified = currentArticle.getModified();
                }else {
                     dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String currrentDate = dateFormat.format(getInstance().getTime());
                    page.modified = currrentDate;
                }

                page.id = currentArticle.getId();
                page.path = f.getAbsolutePath();
                page.shareLink = currentArticle.getUrl();
                page.title = currentArticle.getTitleS();
                page.imagePath = currentArticle.getMediaUrl();//TODO передать путь к картинке локальной!

                if(currentArticle.getCategories() != null) {
                    page.categories = categoriesConverter.fromCategories(currentArticle.getCategories());
                } else {
                    page.categories = "0";
                }
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
                    throwNotificationDownloaded(page);
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
        Bundle serviceBundle = intent.getBundleExtra(BUNDLE_KEY);
        DataEntity article = (DataEntity) serviceBundle.get(BUNDLE_MESSAGE_KEY);
        Toast.makeText(this, "Загружаю " + article.getTitleS(), Toast.LENGTH_SHORT).show();//TODO загружаю как строковый ресурс
       // String url = intent.getStringExtra(URL_INTENT_KEY);
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = downloadHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = article;
        downloadHandler.sendMessage(msg);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

    }

    private void throwNotificationDownloaded(DataEntity page){

        Intent notifyIntent = new Intent(getApplicationContext(), SlidingThemeActivity.class);
        notifyIntent.setAction("default");
        Bundle notifyBundle = new Bundle();
        notifyBundle.putSerializable(BUNDLE_MESSAGE_KEY, page);
        notifyIntent.putExtra(BUNDLE_KEY, notifyBundle);
        notifyIntent.putExtra(SlidingThemeActivity.READ_MODE, true);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Страница: " + page.getTitleS() + " успешно загружена")
                .setContentText("Страницу можно найти на вкладке загрузок")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        builder.setContentIntent(notifyPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1,builder.build());

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
