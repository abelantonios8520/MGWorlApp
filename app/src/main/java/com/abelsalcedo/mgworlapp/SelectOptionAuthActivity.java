package com.abelsalcedo.mgworlapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abelsalcedo.mgworlapp.activities.cliente.MapClienteActivity;
import com.abelsalcedo.mgworlapp.activities.cliente.RegisterActivity;
import com.abelsalcedo.mgworlapp.activities.colaborador.MapColaboradorActivity;
import com.abelsalcedo.mgworlapp.activities.colaborador.RegisterColaboradorActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SelectOptionAuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    Button login, register;
    TextView chat_title_tv;
    Typeface MR, MRR;

    Button google_singIn_button;
    SharedPreferences mPref;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSingInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        chat_title_tv = findViewById(R.id.chat_title_tv);
        google_singIn_button = findViewById(R.id.google_singIn_button);

        login.setTypeface(MR);
        register.setTypeface(MR);
        chat_title_tv.setTypeface(MR);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSingInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        google_singIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSingInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == RC_SIGN_IN){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try{
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                }catch (ApiException e){
                    String data2 = e.getMessage();

                }
            }
        }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SelectOptionAuthActivity.this.access();
                        } else {
                            Snackbar.make(google_singIn_button, "Autentication Failed", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(this, task ->{
            String data2 = task.getMessage();
        });
    }


    private void access(){
        String typeUser = mPref.getString("user", "");
        if(typeUser.equals("cliente")){
            startActivity(new Intent(this, MapClienteActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                            .FLAG_ACTIVITY_NEW_TASK));
        } else{
            startActivity(new Intent(this, MapColaboradorActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                            .FLAG_ACTIVITY_NEW_TASK));
        }
    }


    private void goToLogin() {
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToRegister(){
        String typeUser = mPref.getString("user", "");
        if(typeUser.equals("cliente")){
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterColaboradorActivity.class);
            startActivity(intent);
        }
    }
}