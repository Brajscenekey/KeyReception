package com.key.keyreception.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Ravi Birla on 02,April,2019
 */
public interface AnotherApi {

  /*  @GET("categoryList")
    Call<ResponseBody> categoryList();
    */

    @GET("user/getContent")
    Call<ResponseBody> getContent();

    @GET("user/getFaqList")
    Call<ResponseBody> getFaqList(@Header("authToken") String authToken);


    @GET("user/getHelpList")
    Call<ResponseBody> getHelpList(@Header("authToken") String authToken);
}
