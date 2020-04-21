package madsoft.com.form.Network.WpApi;

import java.util.List;

import madsoft.com.form.Network.Objects.ArticleWp;
import madsoft.com.form.Network.Objects.ArticleWpListItem;
import madsoft.com.form.Network.Objects.Category;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WpApi {//TODO сделать общий метод, а не разные, например getArticleWpCall(category, page) и использовать его

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
     Call<List<ArticleWp>> getArticleWpCall();

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCallWithCount(@Query("per_page") int itemCount);

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCall(@Query("page") short page);

    @GET("posts?_fields=id,modified,title,link,jetpack_featured_media_url")//Поля для отбора см. Wordpress REST Api
    Call<List<ArticleWp>> getArticleWpCall(@Query("categories") String categories);

    @GET("categories?_fileds=id,name")//Поля для отбора см. Wordpress REST Api
    Call<List<Category>> getCategoriesWpCall();

    @GET("categories?_fileds=id,name")//Поля для отбора см. Wordpress REST Api
    Call<List<Category>> getCategoriesWpCall(@Query("page") short page);

    @GET("search?_fields=id,title,url")
    Call<List<ArticleWpListItem>> searchArticleWpCall(@Query("search") String search);

    @GET("search?_fields=id,title,url")
    Call<List<ArticleWpListItem>> searchArticleWpCall(@Query("search") String search,@Query("categories") String categories);

    @GET("search?_fields=id,title,url")
    Call<List<ArticleWpListItem>> searchArticleWpCall(@Query("search") String search,@Query("page") int page);

}
