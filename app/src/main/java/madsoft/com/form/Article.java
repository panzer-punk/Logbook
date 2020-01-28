package madsoft.com.form;

import org.jsoup.nodes.Element;

import java.io.Serializable;

public class Article implements Serializable {

    public static String LINK = "Link";
    public static String TITLE = "Title";

    protected String title, category, imageLink, link, description;


    public Article(String title, String category, String imageLink, String link, String description) {
        this.title = title;
        this.category = category;
        this.imageLink = imageLink;
        this.description = description;
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString(){
        return title + " " + category + " " + imageLink + " " + link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
