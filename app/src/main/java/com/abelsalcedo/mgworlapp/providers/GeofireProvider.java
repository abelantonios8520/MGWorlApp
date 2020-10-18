package com.abelsalcedo.mgworlapp.providers;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDatabase;
    private GeoFire mGeofire;

    public GeofireProvider (String reference) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(reference);
        mGeofire = new GeoFire(mDatabase);
    }

    public void saveLocation(String idColaborador, LatLng latLng) {
        mGeofire.setLocation(idColaborador, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeLocation(String idColaborador) {
        mGeofire.removeLocation(idColaborador);
    }

    public GeoQuery getActiveColaboradores(LatLng latLng, double radius) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

//    ====================
    public GeoQuery getActiveClientes(LatLng latLng, double radius) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

    public DatabaseReference getClienteLocation(String idCliente) {
        return mDatabase.child(idCliente).child("l");
    }

    public DatabaseReference isClienteWorking(String idCliente) {
        return FirebaseDatabase.getInstance().getReference().child("cliente_working").child(idCliente);
    }
//    ================

    public DatabaseReference getColaboradorLocation(String idColaborador) {
        return mDatabase.child(idColaborador).child("l");
    }

    public DatabaseReference isColaboradorWorking(String idColaborador) {
        return FirebaseDatabase.getInstance().getReference().child("emprendedor_working").child(idColaborador);
    }

}
