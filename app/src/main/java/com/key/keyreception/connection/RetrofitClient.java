package com.key.keyreception.connection;

import com.key.keyreception.Interface.AnotherApi;
import com.key.keyreception.Interface.Reginterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    http://3.17.192.198:8042/api/
    private static final String Base_Url = "http://3.17.192.198:8042/api/"; //"http://keyreception.us-east-1.elasticbeanstalk.com/api/";
//    private static final String Base_Url = "http://3.17.192.198/"; //"http://keyreception.us-east-1.elasticbeanstalk.com/api/";
    private static final String Base_Url1 =  "http://3.17.192.198:8042/api/"; // "http://keyreception.us-east-1.elasticbeanstalk.com/api/";
    private static RetrofitClient minstance;
    public static final String BASE_URL = "https://giglooker.com/dev/livloud_admin/api_v1/";
    private Retrofit retrofit,retrofit1,retrofit2;

    private RetrofitClient() {

      /*  OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
*/



        retrofit = new Retrofit.Builder()
                .baseUrl(Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();


        retrofit1 = new Retrofit.Builder()
                .baseUrl(Base_Url1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit2 = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (minstance == null) {
            minstance = new RetrofitClient();
        }
        return minstance;
    }

    public Reginterface getApi() {
        return retrofit.create(Reginterface.class);
    }
    public AnotherApi getAnotherApi() {
        return retrofit2.create(AnotherApi.class);
   }

}
