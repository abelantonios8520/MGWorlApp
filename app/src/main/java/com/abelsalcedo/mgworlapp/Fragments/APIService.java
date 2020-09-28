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
                    "Authorization:key=AAAA-4cWvS8:APA91bFn_kkU177l_PFdq-OqkgB86AaJ-vNzCcFl7TOouUVNgRlXnL8NX-l5UPY_DHr3gaY3LjAGPb3owvTSU6sCAHW1zZ6SEid-HDF1qn7rdvNC1ZBapTugyYGZGLBJq8QzLNVi9f-p"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
