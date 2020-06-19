package info.logos.form.Network.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.Nullable;

public class ArticleWpListItem implements DataEntity{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getId() {
        return id;
    }

    @Nullable
    @Override
    public String getModified() {
        return null;
    }

    @Nullable
    @Override
    public String getMediaUrl() {
        return null;
    }

    @Override
    public List<Integer> getCategories() {
        return null;
    }

    @Override
    public String getTitleS() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
