package madsoft.com.form.Network.Objects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleWp {
    public static String LINK = "Link";
    public static String TITLE = "Title";
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
    @SerializedName("jetpack_featured_media_url")
    @Expose
    private String jetpackFeaturedMediaUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModified() {
        return modified;
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

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getJetpackFeaturedMediaUrl() {
        return jetpackFeaturedMediaUrl;
    }

    public void setJetpackFeaturedMediaUrl(String jetpackFeaturedMediaUrl) {
        this.jetpackFeaturedMediaUrl = jetpackFeaturedMediaUrl;
    }
}