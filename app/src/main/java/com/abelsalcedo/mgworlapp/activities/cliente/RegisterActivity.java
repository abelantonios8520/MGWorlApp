package com.abelsalcedo.mgworlapp.activities.cliente;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.Utils;
import com.abelsalcedo.mgworlapp.activities.MainActivity;
import com.abelsalcedo.mgworlapp.providers.AuthProvider;
import com.abelsalcedo.mgworlapp.providers.ClienteProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements LifecycleObserver {
    EditText username, userape, usernumber, email, password;
    TextView register_tv, msg_reg_tv;
    Button btn_register;
    Typeface MR, MRR;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        userape = findViewById(R.id.userApe);
        usernumber = findViewById(R.id.userNumber);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        register_tv = findViewById(R.id.register_tv);
        msg_reg_tv = findViewById(R.id.msg_reg_tv);

        msg_reg_tv.setTypeface(MRR);
        username.setTypeface(MRR);
        userape.setTypeface(MRR);
        usernumber.setTypeface(MRR);
        email.setTypeface(MRR);
        password.setTypeface(MRR);
        btn_register.setTypeface(MR);
        register_tv.setTypeface(MR);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_userape = userape.getText().toString();
                String txt_usernumber = usernumber.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                Utils.hideKeyboard(RegisterActivity.this);

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_userape) || TextUtils.isEmpty(txt_usernumber) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Todos los archivos son obligatorios", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_userape, txt_usernumber, txt_email, txt_password);
                }
            }
        });
    }

    private void register(final String username, final String userape, final String usernumber, String email, String password){

        dialog = Utils.showLoader(RegisterActivity.this);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users/Clientes").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("userape", userape);
                            hashMap.put("usernumber", usernumber);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("bio", "");
                            hashMap.put("search", username.toLowerCase());
                            if(dialog!=null){
                                dialog.dismiss();
                            }
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MapClienteActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "No puede registrarse con este correo electrónico o contraseña", Toast.LENGTH_SHORT).show();
                            if(dialog!=null){
                                dialog.dismiss();
                            }
                        }
                    }
                });
    }
}