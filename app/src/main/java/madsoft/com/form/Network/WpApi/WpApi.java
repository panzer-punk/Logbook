package madsoft.com.form.Network.WpApi;

import java.util.List;

import madsoft.com.form.Network.Objects.ArticleWp;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WpApi {

    @GET("posts?_fields=id,modified,title,link")//Поля для отбора см. Wordpress REST Api
    public Call<List<ArticleWp>> getArticleWpCall();

}
