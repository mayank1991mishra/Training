package com.lexnarro.network;


import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.response.SoapResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Saurabh on 4/27/2017.
 */

public interface ApiInterface {
    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("login.asmx")
    Call<SoapResponseEnvelope> login(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("registration.asmx")
    Call<SoapResponseEnvelope> signup(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("dashboard.asmx")
    Call<SoapResponseEnvelope> dashboard(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("forgotpassword.asmx")
    Call<SoapResponseEnvelope> forgotPassword(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("training.asmx")
    Call<SoapResponseEnvelope> training(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("updateprofile.asmx")
    Call<SoapResponseEnvelope> updateProfile(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("usertransaction.asmx")
    Call<SoapResponseEnvelope> getTransaction(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("usertransaction.asmx")
    Call<SoapResponseEnvelope> postTransaction(@Body SoapEnvelop body);


    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("states.asmx")
    Call<SoapResponseEnvelope> getState(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("planMaster.asmx")
    Call<SoapResponseEnvelope> getGetPlans(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("DownloadAndEmailService.asmx")
    Call<SoapResponseEnvelope> downloadAndEmail(@Body SoapEnvelop body);

    @Headers({"Content-Type: text/xml", "Accept-Charset: utf-8"})
    @POST("carryover.asmx")
    Call<SoapResponseEnvelope> carryOver(@Body SoapEnvelop body);
}
