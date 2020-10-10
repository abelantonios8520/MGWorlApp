package com.abelsalcedo.mgworlapp.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abelsalcedo.mgworlapp.activities.colaborador.MapColaboradorBookingActivity;
import com.abelsalcedo.mgworlapp.providers.AuthProvider;
import com.abelsalcedo.mgworlapp.providers.ClienteBookingProvider;
import com.abelsalcedo.mgworlapp.providers.GeofireProvider;

public class AcceptReceiver extends BroadcastReceiver {

    private ClienteBookingProvider mClienteBookingProvider;
    private GeofireProvider mGeofireProvider;
    private AuthProvider mAuthProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider("active_colaboradores");
        mGeofireProvider.removeLocation(mAuthProvider.getId());

        String idCliente = intent.getExtras().getString("idCliente");
        mClienteBookingProvider = new ClienteBookingProvider();
        mClienteBookingProvider.updateStatus(idCliente, "accept");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, MapColaboradorBookingActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        intent1.putExtra("idCliente", idCliente);
        context.startActivity(intent1);

    }

}
