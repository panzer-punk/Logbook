package madsoft.com.form.DataBase.entity;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Page implements Serializable {
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

    @ColumnInfo(name = "categories")
    public String categories;

    @ColumnInfo(name = "modified")
    public String modified;
}
