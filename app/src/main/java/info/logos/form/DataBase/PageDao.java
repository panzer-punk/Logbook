package info.logos.form.DataBase;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import info.logos.form.DataBase.entity.Page;

@Dao
public interface PageDao {

    @Query("SELECT * FROM page")
    List<Page> getAll();

    @Query("SELECT * FROM page WHERE id LIKE (:pageId)")
    Page loadById(int pageId);

    @Query("SELECT path FROM page WHERE id LIKE :pageId")
    String getFilePath(int pageId);

    @Query("SELECT * FROM page  WHERE instr(categories,:category) > 0")
    List<Page> filterByCategory(int category);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Page page);

    @Delete
    void delete(Page page);



}
