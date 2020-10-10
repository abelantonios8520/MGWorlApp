package com.abelsalcedo.mgworlapp.providers;

import com.abelsalcedo.mgworlapp.Model.FCMBody;
import com.abelsalcedo.mgworlapp.retrofit.IFCMApi;
import com.abelsalcedo.mgworlapp.retrofit.RetrofitCliente;
import retrofit2.Call;

public class NotificationProvider {
    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call sendNotification(FCMBody body) {
        return RetrofitCliente.getClienteObject(url).create(IFCMApi.class).send(body);
    }

}
