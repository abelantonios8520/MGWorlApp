package com.abelsalcedo.mgworlapp.providers;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProviderColaborador {

    private DatabaseReference mDatabase;
    private GeoFire mGeofire;

    public GeofireProviderColaborador (String referenceColaborador) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(referenceColaborador);
        mGeofire = new GeoFire(mDatabase);
    }

    public void saveLocation(String idCliente, LatLng latLng) {
        mGeofire.setLocation(idCliente, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeLocation(String idCliente) {
        mGeofire.removeLocation(idCliente);
    }

    public GeoQuery getActiveClientes(LatLng latLng, double radius) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

    public DatabaseReference getClienteLocation(String idCliente) {
        return mDatabase.child(idCliente).child("l");
    }

    public DatabaseReference isClienteWorking(String idCliente) {
        return FirebaseDatabase.getInstance().getReference().child("emprendedor_working").child(idCliente);
    }

}
