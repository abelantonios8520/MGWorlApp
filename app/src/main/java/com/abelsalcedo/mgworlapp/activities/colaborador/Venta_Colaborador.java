package com.abelsalcedo.mgworlapp.activities.colaborador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.providers.AuthProvider;
import com.abelsalcedo.mgworlapp.providers.ColaboradorProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Venta_Colaborador extends AppCompatActivity {


    private Button mEnviar;
    private TextView mTextViewName;
    private ColaboradorProvider mColaboradorProvider;
    private AuthProvider mAuthProvider;
    private ProgressDialog mProgressDialog;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_colaborador);

        mColaboradorProvider = new ColaboradorProvider();
        mAuthProvider = new AuthProvider();
        mTextViewName = findViewById(R.id.Colaborador);
        mEnviar = findViewById(R.id.btnEnviar);

        getNameColaboradorVenta();

        mEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Venta_Colaborador.this, MapColaboradorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getNameColaboradorVenta() {

        mColaboradorProvider.getColaborador(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    mTextViewName.setText(name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}