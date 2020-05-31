package madsoft.com.form.Application;

import android.app.Application;
import android.content.res.Configuration;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import madsoft.com.form.DataBase.AppDatabase;

public class MyApplication extends Application {

    private static AppDatabase database;

    public static AppDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
         final Migration MIGRATION_1_2 = new Migration(2, 3) {
            @Override
            public void migrate(final SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE page ADD COLUMN categories TEXT");
            }
        };
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "logos")
                .enableMultiInstanceInvalidation()
                .addMigrations(MIGRATION_1_2)
                .build();
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
