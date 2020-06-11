package madsoft.com.form.Network.Objects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;
import madsoft.com.form.DataBase.entity.Page;

public class ArticleWp implements DataEntity{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("categories")
    @Expose
    private List<Integer> categories = null;
    @SerializedName("jetpack_featured_media_url")
    @Expose
    private String jetpackFeaturedMediaUrl;

    public ArticleWp(){}
    public ArticleWp(Page page) {

        id = page.id;
        modified = page.modified;
        link = page.path;
        title = new Title();
        title.setRendered(page.title);
        jetpackFeaturedMediaUrl = page.imagePath;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModified() {
        return modified;
    }

    @Nullable
    @Override
    public String getMediaUrl() {
        return jetpackFeaturedMediaUrl;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    @Override
    public String getTitleS() {
        return title.getRendered();
    }

    @Override
    public String getUrl() {
        return getLink();
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getJetpackFeaturedMediaUrl() {
        return jetpackFeaturedMediaUrl;
    }

    public void setJetpackFeaturedMediaUrl(String jetpackFeaturedMediaUrl) {
        this.jetpackFeaturedMediaUrl = jetpackFeaturedMediaUrl;
    }
    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

}