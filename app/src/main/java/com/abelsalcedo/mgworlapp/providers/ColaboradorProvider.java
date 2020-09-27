package com.abelsalcedo.mgworlapp.providers;

import com.abelsalcedo.mgworlapp.Model.Colaborador;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ColaboradorProvider {
    DatabaseReference mDatabase;

    public ColaboradorProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Colaboradores");
    }

    public Task<Void> create(Colaborador colaborador) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", colaborador.getUsername());
        map.put("ape", colaborador.getApe());
        map.put("dni", colaborador.getDni());
        map.put("telef", colaborador.getTelf());
        map.put("email", colaborador.getEmail());
        return mDatabase.child(colaborador.getId()).setValue(colaborador);
    }

    public DatabaseReference getColaborador(String idColaborador) {
        return mDatabase.child(idColaborador);
    }

    public Task<Void> update(Colaborador colaborador) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", colaborador.getUsername());

        return mDatabase.child(colaborador.getId()).updateChildren(map);
    }
}
