package info.logos.form.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import info.logos.form.DataBase.entity.Page;

@Database(entities = {Page.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PageDao pageDao();
}
