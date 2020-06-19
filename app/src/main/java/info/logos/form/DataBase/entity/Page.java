package info.logos.form.DataBase.entity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import info.logos.form.Network.Objects.DataEntity;

@Entity
public class Page implements DataEntity {
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

    @Override
    public Integer getId() {
        return id;
    }

    @Nullable
    @Override
    public String getModified() {
        return null;
    }

    @Override
    public String getMediaUrl() {
        return imagePath;
    }

    @Override
    public List<Integer> getCategories() {
        return null;
    }

    @Override
    public String getTitleS() {
        return title;
    }

    @Override
    public String getUrl() {
        return path;
    }
}
