package madsoft.com.form;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Даниил on 29.06.2018.
 */

public class LinksMap {


    private Elements Values;

    public LinksMap(Elements values) {

        Values = values;

    }

    public Element getLink(String key){


       for( Element e : Values){

           if(e.text().equals(key))
               return e;

       }

        return null;

    }

}
