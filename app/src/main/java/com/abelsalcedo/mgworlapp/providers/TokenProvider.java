package com.abelsalcedo.mgworlapp.providers;

import com.abelsalcedo.mgworlapp.Model.TokenMG;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class TokenProvider {

    DatabaseReference mDatabase;

    public TokenProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tokens");
    }

    public void create(final String idUser) {
        if (idUser == null) return;
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                TokenMG token = new TokenMG(instanceIdResult.getToken());
                mDatabase.child(idUser).setValue(token);
            }
        });
    }

    public DatabaseReference getTokenMG(String idUser) {
        return mDatabase.child(idUser);
    }
}


