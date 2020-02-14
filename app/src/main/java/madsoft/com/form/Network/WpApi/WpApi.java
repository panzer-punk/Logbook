package madsoft.com.form.Network.WpApi;

import java.util.List;

import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.ArticleWpListItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WpApi {

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
     Call<List<ArticleWp>> getArticleWpCall();

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCallWithCount(@Query("per_page") int itemCount);

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCall(@Query("page") short page);

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCall(@Query("categories") String categories);

    @GET("search?_fields=id,title,url&search={search}")
    Call<List<ArticleWpListItem>> searchArticleWpCall(String search);

    @GET("search?_fields=id,title,url")
    Call<List<ArticleWpListItem>> searchArticleWpCall(@Query("search") String search,@Query("categories") String categories);

}
