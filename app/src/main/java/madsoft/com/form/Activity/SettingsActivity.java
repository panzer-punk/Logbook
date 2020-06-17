package madsoft.com.form.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import madsoft.com.form.Assets;
import madsoft.com.form.FileSystem.CacheSystem;
import madsoft.com.form.R;

public class SettingsActivity extends Activity {

    //private CacheSystem cacheSystem;

    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
      //  getActionBar().setHomeButtonEnabled(true);
      //  getActionBar().setTitle(R.string.text_settings);

      //  cacheSystem = new CacheSystem(this);

      //  Log.d("CACHESYS", String.valueOf(cacheSystem.checkFile(Assets.FLAG_D)));

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
