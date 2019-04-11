package com.key.keyreception.Interface;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mindiii on 13/11/18.
 */

public interface Reginterface {


    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("email") String email,
                             @Field("password") String password,
                             @Field("userType") String userType,
                             @Field("deviceType") String deviceType,
                             @Field("firebaseToken") String firebaseToken
    );

    @POST("forgotPassword")
    @FormUrlEncoded
    Call<ResponseBody> forgot(@Field("email") String email);

    @Multipart
    @POST("updateProfile")
    Call<ResponseBody> updateDoc(@Header("authToken") String authToken,
                                 @Part("securityNumber") RequestBody securityNumber,
                                 @Part("billingAddress") RequestBody billingAddress,
                                 @Part("billingLatitude") RequestBody billingLatitude,
                                 @Part("billingLongitude") RequestBody billingLongitude,
                                 @Part("paymentType") RequestBody paymentType,
                                 @Part MultipartBody.Part profileImage);


    @Multipart
    @POST("updateProfile")
    Call<ResponseBody> updatepayment(@Header("authToken") String authToken,@Header("Content-Type") String ContentType,
                                     @Part("billingAddress") RequestBody billingAddress,
                                     @Part("billingLatitude") RequestBody billingLatitude,
                                     @Part("billingLongitude") RequestBody billingLongitude,
                                     @Part("paymentType") RequestBody paymentType
    );

    @FormUrlEncoded
    @POST("updateStatus")
    Call<ResponseBody> acceptReject(@Header("authToken") String authToken,
                                    @Header("Content-Type") String ContentType,
                                    @Field("jobId") String jobId,
                                    @Field("requestId") String requestId,
                                    @Field("jobCreaterId") String jobCreaterId,
                                    @Field("status") String status
    );


    @Multipart
    @POST("updateLocation")
    Call<ResponseBody> updateLocation(@Header("authToken") String authToken,
                                      @Part RequestBody data);


    /*@POST("updateLocation")
    @Multipart
    Call<ResponseBody> updateLocation(@Header("authToken") String authToken,
                                      @Header("Content-Type") String ContentType,
                                    @Part("address") RequestBody jobId,
                                    @Part("latitude") RequestBody requestId,
                                    @Part("longitude") RequestBody jobCreaterId);*/


    @GET("userInfo")
    Call<ResponseBody> userInfo(@Header("authToken") String authToken);

    @POST("RequestList")
    @FormUrlEncoded
    Call<ResponseBody> RequestList(@Header("authToken") String authToken,

                                   @Field("page") String page,
                                   @Field("limit") String limit
    );

    @POST("RequestDetail")
    @FormUrlEncoded
    Call<ResponseBody> RequestDetail(@Header("authToken") String authToken,
                                     @Header("Content-Type") String ContentType,
                                     @Field("requestId") String requestId
    );


    @GET("propertyList")
    Call<ResponseBody> propertyList(@Header("authToken") String authToken);

    @Multipart
    @POST("registration")
    Call<ResponseBody> Signup(

            @Part("fullName") RequestBody fullName,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("deviceType") RequestBody deviceType,
            @Part("userType") RequestBody userType,
            @Part("categoryId") RequestBody categoryId,
            @Part("firebaseToken") RequestBody firebaseToken,
            @Part MultipartBody.Part profileImage
    );

    @Multipart
    @POST("updateProfile")
    Call<ResponseBody> updateProfile(

            @Header("authToken") String authToken,
            @Part("fullName") RequestBody fullName,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part profileImage
    );


    @POST("changePassword")
    @FormUrlEncoded
    Call<ResponseBody> changePassword(@Header("authToken") String authToken, @Field("oldPassword") String oldPassword,
                                      @Field("newPassword") String newPassword,
                                      @Field("confirmPassword") String confirmPassword
    );


    @POST("createJob")
    @FormUrlEncoded
    Call<ResponseBody> createJob(@Header("authToken") String authToken,
                                 @Field("propertyId") String propetyId,
                                 @Field("bedroom") String bedroom,
                                 @Field("bathroom") String bathroom,
                                 @Field("address") String address,
                                 @Field("latitude") String latitude,
                                 @Field("longitude") String longitude,
                                 @Field("price") String price,
                                 @Field("propertySize") String propertySize,
                                 @Field("serviceDate") String serviceDate,
                                 @Field("checkIn") String checkIn,
                                 @Field("checkOut") String checkOut,
                                 @Field("categoryId") String categoryId,
                                 @Field("description") String description,
                                 @Field("propertyName") String propertyName
    );

    @POST("jobList")
    @FormUrlEncoded
    Call<ResponseBody> jobList(@Header("authToken") String authToken,
                               @Field("type") String type,
                               @Field("limit") String limit,
                               @Field("status") String status,
                               @Field("search") String search
    );

    @POST("jobDetail")
    @FormUrlEncoded
    Call<ResponseBody> jobDetail(@Header("authToken") String authToken,
                                 @Field("jobId") String jobId
    );
}


