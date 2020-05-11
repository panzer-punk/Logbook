package madsoft.com.form.Network.WpApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService instance;
    private static final String BASE_URL = "https://sanctumlogos.info/wp-json/wp/v2/";
    private Retrofit retrofit;

    private NetworkService(){

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static NetworkService getInstance(){
        if(instance == null)
            instance = new NetworkService();
        return instance;
    }

    public WpApi getWpApi()
    {return retrofit.create(WpApi.class);}
}
