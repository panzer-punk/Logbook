package madsoft.com.form.service;


import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
import org.jsoup.select.NodeTraversor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.SynchronousQueue;

import madsoft.com.form.Application.MyApplication;
import madsoft.com.form.DataBase.PageDao;
import madsoft.com.form.DataBase.entity.Page;
import madsoft.com.form.Network.WpApi.NetworkService;
import madsoft.com.form.Network.WpApi.WpApi;
import madsoft.com.form.R;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_8;

public class DownloadService extends Service {

    public static String URL_INTENT_KEY = "URL";
    public static String HOST_URL = "https://sanctumlogos.info/";
    public static String MODIFIED_KEY = "MODIFIED";



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

            File dir = getApplicationContext().getFilesDir();
            String path = dir.getAbsolutePath();
            if(!dir.exists()) {
                dir.mkdirs();
            }

            try {
                String murl = new String(msg.obj.toString().getBytes(),UTF_8);
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
                page.modified = "";//TODO передать modified из ArticleWp
                //TODO передать параметр category id
                page.path = f.getAbsolutePath();
                if(!f.exists()) {
                    FileUtils.writeStringToFile(f, mDoc.outerHtml(), "UTF-8");
                    servicePageDao.insert(page);
                }//TODO обновоить уже созданый файл
               Log.d("File path", f.getAbsolutePath());


            } catch (IOException e) {
                e.printStackTrace();
                stopSelf(msg.arg1);
            }

            stopSelf(msg.arg1);
        }
    }

    private void writeToFileFromUrl(String surl, String filename, String extension){
        try {
        URL url = new URL(surl);

        URLConnection ucon = url.openConnection();

        /*
         * Define InputStreams to read from the URLConnection.
         */
        InputStream is = ucon.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        /*
         * Read bytes to the Buffer until there is nothing more to read(-1).
         */
     /*  baf = new ByteArrayBuffer(5000);
        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }


         Convert the Bytes read to a String.
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baf.toByteArray());
        fos.flush();
        fos.close();

            FileOutputStream fos = new FileOutputStream(new File(Environment.getDownloadCacheDirectory(), filename+extension));

*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void writeToFile(String content){

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
