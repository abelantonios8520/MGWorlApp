package com.abelsalcedo.mgworlapp.activities.colaborador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.Model.ClienteBooking;
import com.abelsalcedo.mgworlapp.Model.HistoryBooking;
import com.abelsalcedo.mgworlapp.providers.ClienteBookingProvider;
import com.abelsalcedo.mgworlapp.providers.HistoryBookingProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class CalificationClienteActivity extends AppCompatActivity {

    private TextView mTextViewOrigin;
    private TextView mTextViewDestination;
    private RatingBar mRatinBar;
    private Button mButtonCalification;

    private ClienteBookingProvider mClienteBookingProvider;

    private String mExtraClienteId;

    private HistoryBooking mHistoryBooking;
    private HistoryBookingProvider mHistoryBookingProvider;

    private float mCalification = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calification_cliente);

        mTextViewDestination = findViewById(R.id.textViewDestinationCalification);
        mTextViewOrigin = findViewById(R.id.textViewOriginCalification);
        mRatinBar = findViewById(R.id.ratingbarCalification);
        mButtonCalification = findViewById(R.id.btnCalification);

        mClienteBookingProvider = new ClienteBookingProvider();
        mHistoryBookingProvider = new HistoryBookingProvider();

        mExtraClienteId = getIntent().getStringExtra("idCliente");

        mRatinBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float calification, boolean b) {
                mCalification = calification;
            }
        });
        mButtonCalification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calificate();
            }
        });

        getClienteBooking();
    }

    private void getClienteBooking() {
        mClienteBookingProvider.getClienteBooking(mExtraClienteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ClienteBooking clienteBooking = dataSnapshot.getValue(ClienteBooking.class);
                    mTextViewOrigin.setText(clienteBooking.getOrigin());
                    mTextViewDestination.setText(clienteBooking.getDestination());
                    mHistoryBooking = new HistoryBooking(
                            clienteBooking.getIdHistoryBooking(),
                            clienteBooking.getIdCliente(),
                            clienteBooking.getIdColaborador(),
                            clienteBooking.getDestination(),
                            clienteBooking.getOrigin(),
                            clienteBooking.getTime(),
                            clienteBooking.getKm(),
                            clienteBooking.getStatus(),
                            clienteBooking.getOriginLat(),
                            clienteBooking.getOriginLng(),
                            clienteBooking.getDestinationLat(),
                            clienteBooking.getDestinationLng()
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calificate() {
        if (mCalification  > 0) {
            mHistoryBooking.setCalificationCliente(mCalification);
            mHistoryBooking.setTimestamp(new Date().getTime());
            mHistoryBookingProvider.getHistoryBooking(mHistoryBooking.getIdHistoryBooking()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mHistoryBookingProvider.updateCalificactionCliente(mHistoryBooking.getIdHistoryBooking(), mCalification).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CalificationClienteActivity.this, "La calificacion se guardo correctamente", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CalificationClienteActivity.this, MapColaboradorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    else {
                        mHistoryBookingProvider.create(mHistoryBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CalificationClienteActivity.this, "La calificacion se guardo correctamente", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CalificationClienteActivity.this, MapColaboradorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else {
            Toast.makeText(this, "Debes ingresar la calificacion", Toast.LENGTH_SHORT).show();
        }
    }
}
