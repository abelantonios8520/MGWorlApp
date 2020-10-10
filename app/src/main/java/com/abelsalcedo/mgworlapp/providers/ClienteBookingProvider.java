package com.abelsalcedo.mgworlapp.providers;

import com.abelsalcedo.mgworlapp.Model.ClienteBooking;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClienteBookingProvider {

    private DatabaseReference mDatabase;

    public ClienteBookingProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ClienteBooking");
    }

    public Task<Void> create(ClienteBooking clienteBooking) {
        return mDatabase.child(clienteBooking.getIdCliente()).setValue(clienteBooking);
    }

    public Task<Void> updateStatus(String idClienteBooking, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return mDatabase.child(idClienteBooking).updateChildren(map);
    }

    public Task<Void> updateIdHistoryBooking(String idClienteBooking) {
        String idPush = mDatabase.push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("idHistoryBooking", idPush);
        return mDatabase.child(idClienteBooking).updateChildren(map);
    }

    public DatabaseReference getStatus(String idClienteBooking) {
        return mDatabase.child(idClienteBooking).child("status");
    }

    public DatabaseReference getClienteBooking(String idClienteBooking) {
        return mDatabase.child(idClienteBooking);
    }

    public Task<Void> delete(String idClienteBooking) {
        return mDatabase.child(idClienteBooking).removeValue();
    }
}
