package com.abelsalcedo.mgworlapp.activities.colaborador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.abelsalcedo.mgworlapp.Model.Colaborador;
import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.providers.AuthProvider;
import com.abelsalcedo.mgworlapp.providers.ColaboradorProvider;
import com.abelsalcedo.mgworlapp.providers.ImagesProvider;
import com.abelsalcedo.mgworlapp.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Venta_Colaborador extends AppCompatActivity {
    private ImageView mfotoPerfilVenta;
    private Button mEnviarVenta;
    private TextView mColaboradorName;
    private EditText mtxtVenta;
    private CircleImageView mCircleImageBack;

    private ColaboradorProvider mColaboradorProvider;
    private AuthProvider mAuthProvider;
    private ImagesProvider mImagesProvider;

    private File mImageFile;
    private String mImage;
    private final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgressDialog;
    private String mName;
    private String mVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta__colaborador);
        //MyToolbar.show(this, "Actualizar perfíl", true);

        mfotoPerfilVenta = findViewById(R.id.fotoPerfilVenta);
        mEnviarVenta = findViewById(R.id.EnviarVenta);
        mColaboradorName = findViewById(R.id.Colaborador);
        mtxtVenta = findViewById(R.id.txtVenta);
        mCircleImageBack = findViewById(R.id.circleImageBack);

        mColaboradorProvider = new ColaboradorProvider();
        mAuthProvider = new AuthProvider();
        mImagesProvider = new ImagesProvider("Colaborador_image");

        mProgressDialog = new ProgressDialog(this);
        getColaboradorInfo();

        mfotoPerfilVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        mEnviarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mfotoPerfilVenta.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Mensaje: " + e.getMessage());
            }
        }
    }

    private void getColaboradorInfo() {
        mColaboradorProvider.getColaborador(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String venta = dataSnapshot.child("venta").getValue().toString();
                    mColaboradorName.setText(name);
                    mtxtVenta.setText(venta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile() {
        mName = mColaboradorName.getText().toString();
        mVenta = mtxtVenta.getText().toString();
        if (!mName.equals("") && mImageFile != null) {
            mProgressDialog.setMessage("Espere un momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveImage();
        } else {
            Toast.makeText(this, "Ingresa la imagen y el nombre", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mImagesProvider.saveImage(Venta_Colaborador.this, mImageFile, mAuthProvider.getId()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImagesProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            Colaborador colaborador = new Colaborador();
                            colaborador.setImage(image);
                            colaborador.setName(mName);
                            colaborador.setId(mAuthProvider.getId());
                            colaborador.setVenta(mVenta);
                            mColaboradorProvider.update(colaborador).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(Venta_Colaborador.this, "Su información se actualizo correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(Venta_Colaborador.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}