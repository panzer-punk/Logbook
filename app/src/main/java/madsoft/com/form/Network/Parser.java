package madsoft.com.form.Network;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

import madsoft.com.form.Network.Objects.Article;

/**
 * Created by Даниил on 01.06.2017.
 */

public class Parser {

    private Document document;
    private char crunch = '"';

    Parser(Document document) {

        this.document = document;
    }

    public static LinkedList<Article> articleAdapter(Document document){

        Elements links = document.select("article");

        LinkedList<Article> articleLinkedList = new LinkedList<>();

        for (Element e : links){

        Article article = new Article(
                e.getElementsByClass("post-title").first().text(),
                e.getElementsByClass("shapely-category").first().text(),
                e.getElementsByTag("img").first().attr("src"),
                e.getElementsByTag("a").first().attr("href"),
                e.getElementsByTag("p").first().text());

        articleLinkedList.add(article);
        }

        return articleLinkedList;

    }

}


