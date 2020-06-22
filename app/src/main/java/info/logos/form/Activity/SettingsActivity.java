package info.logos.form.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;
import info.logos.form.Assets;
import info.logos.form.R;
import info.logos.form.service.DownloadService;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setTitle(R.string.title_activity_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        btnAbout = findViewById(R.id.button_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SlidingThemeActivity.class);
                intent.setAction(" ");
                 intent.putExtra(Assets.LINK, "https://sanctumlogos.info/o-proekte");
                 intent.putExtra(Assets.TITLE, "О проекте");
                 startActivity(intent);
            }
        });
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}