package madsoft.com.form.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import madsoft.com.form.DataBase.entity.Page;

@Database(entities = {Page.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PageDao pageDao();
}
