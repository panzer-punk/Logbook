package madsoft.com.form.DataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Page {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "imagePath")
    public String imagePath;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "shareLink")
    public String shareLink;


    @ColumnInfo(name = "modified")
    public String modified;
}
