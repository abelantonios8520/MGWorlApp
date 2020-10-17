package com.abelsalcedo.mgworlapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.activities.colaborador.MapColaboradorActivity;
import com.abelsalcedo.mgworlapp.activities.cliente.PedidoActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button mButtonIAmCliente;
    Button mButtonIAmColab;

    SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        final SharedPreferences.Editor editor = mPref.edit();


        mButtonIAmCliente = findViewById(R.id.btnIAmCliente);
        mButtonIAmColab = findViewById(R.id.btnIAmColab);

        mButtonIAmCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("user", "cliente");
                editor.apply();
                goToSelectAuth();
            }
        });
        mButtonIAmColab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("user", "colaborador");
                editor.apply();
                goToSelectAuth();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String user = mPref.getString("user", "");
            if (user.equals("cliente")) {
                Intent intent = new Intent(MainActivity.this, PedidoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, MapColaboradorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    private void goToSelectAuth() {
        Intent intent = new Intent(MainActivity.this, SelectOptionAuthActivity.class);
        startActivity(intent);
    }
}

/* ---- Colores originales ----
<color name="colorPrimary">#673AB7</color>
<color name="colorPrimaryDark">#512DA8</color>
<color name="colorAccent">#E040FB</color>*/