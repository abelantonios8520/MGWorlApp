package com.abelsalcedo.mgworlapp.providers;

import com.abelsalcedo.mgworlapp.Model.Cliente;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClienteProvider {
    DatabaseReference mDatabase;

    public ClienteProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Clientes");
    }

    public Task<Void> create(Cliente cliente) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", cliente.getName());
        map.put("ape", cliente.getApe());
        map.put("telef", cliente.getTelf());
        map.put("email", cliente.getEmail());
        map.put("status", "offline");
        map.put("imageurl", "default");
        return mDatabase.child(cliente.getId()).setValue(map);
    }

    public Task<Void> update(Cliente cliente) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", cliente.getName());
        map.put("image", cliente.getImageURL());
        map.put("pedido", cliente.getPedido());
        return mDatabase.child(cliente.getId()).updateChildren(map);
    }

    public DatabaseReference getCliente(String idCliente) {
        return mDatabase.child(idCliente);
    }
}
