package com.abelsalcedo.mgworlapp.providers;

import com.abelsalcedo.mgworlapp.Model.HistoryBooking;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HistoryBookingProvider {

    private DatabaseReference mDatabase;

    public HistoryBookingProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("HistoryBooking");
    }

    public Task<Void> create(HistoryBooking historyBooking) {
        return mDatabase.child(historyBooking.getIdHistoryBooking()).setValue(historyBooking);
    }

    public  Task<Void> updateCalificactionCliente(String idHistoryBooking, float calificacionCliente) {
        Map<String, Object> map = new HashMap<>();
        map.put("calificationCliente", calificacionCliente);
        return mDatabase.child(idHistoryBooking).updateChildren(map);
    }

    public  Task<Void> updateCalificactionColaborador(String idHistoryBooking, float calificacionColaborador) {
        Map<String, Object> map = new HashMap<>();
        map.put("calificationColaborador", calificacionColaborador);
        return mDatabase.child(idHistoryBooking).updateChildren(map);
    }

    public DatabaseReference getHistoryBooking(String idHistoryBooking) {
        return mDatabase.child(idHistoryBooking);
    }

}

