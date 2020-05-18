package madsoft.com.form.DataBase;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import madsoft.com.form.DataBase.entity.Page;

@Dao
public interface PageDao {

    @Query("SELECT * FROM page")
    List<Page> getAll();

    @Query("SELECT * FROM page WHERE id LIKE (:pageId)")
    Page loadById(int pageId);

    @Query("SELECT path FROM page WHERE id LIKE :pageId")
    String getFilePath(int pageId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Page page);

    @Delete
    void delete(Page page);



}
