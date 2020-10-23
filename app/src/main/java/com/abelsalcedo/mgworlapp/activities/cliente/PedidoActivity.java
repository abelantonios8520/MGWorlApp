package com.abelsalcedo.mgworlapp.activities.cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.Model.Cliente;
import com.abelsalcedo.mgworlapp.providers.AuthProvider;
import com.abelsalcedo.mgworlapp.providers.ClienteProvider;
import com.abelsalcedo.mgworlapp.providers.ImagesProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class PedidoActivity extends AppCompatActivity {

    private ImageView mPerfilPedido;
    private Button mbtnEnviar;
    private TextView mpedidoNombre;
    private TextInputEditText mPedidoTxt;
    private CircleImageView mcircleImagePedio;


    private ClienteProvider mClienteProvider;
    private AuthProvider mAuthProvider;
    private ImagesProvider mImageProvider;

    private File mImageFile;
    private String mImage;

    private final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgressDialog;
    private String mName;
    public static String mPedir = "Lomo saltado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        mPerfilPedido = findViewById(R.id.PerfilPedido);
        mbtnEnviar = findViewById(R.id.btnEnviar);
        mpedidoNombre = findViewById(R.id.pedidoNombre);
        mPedidoTxt = findViewById(R.id.PedidoTxt);
        mcircleImagePedio = findViewById(R.id.circleImagePedio);


        mClienteProvider = new ClienteProvider();
        mAuthProvider = new AuthProvider();
        mImageProvider = new ImagesProvider("Pedido_images");

        mProgressDialog = new ProgressDialog(this);

        getDriverInfo();

        mPerfilPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mbtnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        mcircleImagePedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidoActivity.this, MapClienteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageFile = com.abelsalcedo.mgworlapp.utils.FileUtil.from(this, data.getData());
                mPerfilPedido.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Mensaje: " +e.getMessage());
            }
        }
    }

    private void getDriverInfo() {
        mClienteProvider.getCliente(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String pedido = dataSnapshot.child("pedido").getValue().toString();
                    String image = "";
                    if (dataSnapshot.hasChild("image")) {
                        image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(PedidoActivity.this).load(image).into(mPerfilPedido);
                    }
                    mpedidoNombre.setText(name);
                    mPedidoTxt.setText(pedido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile() {
        mName = mpedidoNombre.getText().toString();
        mPedir = mPedidoTxt.getText().toString();
        if (!mName.equals("") && !mPedir.isEmpty()) {
            mProgressDialog.setMessage("Espere un momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveImage();
            Toast.makeText(this, "Ya hiciste tu pedido, sigue los siguientes pasos", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Por favor ingresa tu pedido", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mImageProvider.saveImage(PedidoActivity.this, mImageFile, mAuthProvider.getId()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            Cliente cliente = new Cliente();
                            cliente.setImageURL(image);
                            cliente.setName(mName);
                            cliente.setId(mAuthProvider.getId());
                            cliente.setPedido(mPedir);
                            mClienteProvider.update(cliente).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(PedidoActivity.this, "Su informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(PedidoActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
