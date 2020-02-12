package madsoft.com.form.Network.WpApi;

import java.util.List;

import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.ArticleWpListItem;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WpApi {

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
     Call<List<ArticleWp>> getArticleWpCall();

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url&categories={categories}")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCall(String categories);

    @GET("search?_fields=id,title,url&search={search}")
    Call<List<ArticleWpListItem>> searchArticleWpCall(String search);

    @GET("search?_fields=id,title,url&search={search}&categories={categories}")
    Call<List<ArticleWpListItem>> searchArticleWpCall(String search, String categories);

}
