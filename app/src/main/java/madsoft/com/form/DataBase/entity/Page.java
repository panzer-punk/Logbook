package madsoft.com.form.DataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Page {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "modified")
    public String modified;
}
