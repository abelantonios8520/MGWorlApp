package com.abelsalcedo.mgworlapp.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abelsalcedo.mgworlapp.providers.ClienteBookingProvider;

public class        CancelReceiver extends BroadcastReceiver {
    private ClienteBookingProvider mClienteBookingProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        String idCliente = intent.getExtras().getString("idCliente");
        mClienteBookingProvider = new ClienteBookingProvider();
        mClienteBookingProvider.updateStatus(idCliente, "cancel");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }
}