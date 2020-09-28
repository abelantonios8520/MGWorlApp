package com.abelsalcedo.mgworlapp.Fragments;

import com.abelsalcedo.mgworlapp.Notifications.MyResponse;
import com.abelsalcedo.mgworlapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
              "Content-Type:application/json",
              "Authorization:key=AAAALsRBy3g:APA91bFd7ftsQnUlm5yilLifDjcpXx3cpZGXv34u_E97Skdg3qtt9-VLkrQXTd4ci0HIsY-BjtcDfeUQpQrSuKzilMBuAHjF3ZN3YG_cUuhxY-0H-G8_iYEni_PiAqqejh52XqgzmJBz"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
