package com.key.keyreception.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ravi Birla on 02,April,2019
 */
public interface AnotherApi {

    @GET("categoryList")
    Call<ResponseBody> categoryList();

}
