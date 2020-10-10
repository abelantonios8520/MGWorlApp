package com.abelsalcedo.mgworlapp.activities.colaborador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.Adapter.AdapterPedidos;
import com.abelsalcedo.mgworlapp.pojo.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class buscarClientesActivity extends AppCompatActivity implements AdapterPedidos.PedidosListener {
    private DatabaseReference ref;
    private ArrayList<Pedidos> list;
    private RecyclerView rv;
    private SearchView searchView;
    private AdapterPedidos adapter;

    private LinearLayoutManager lm;

    private static final String TAG = "buscarClientesActivity";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_buscar_clientes);
        Log.d(TAG, "onCreate: started.");

        ref = FirebaseDatabase.getInstance().getReference().child("Users/Pedidos");
        rv = findViewById(R.id.rv);
        searchView = findViewById(R.id.buscarcliente);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        list = new ArrayList<>();
        adapter = new AdapterPedidos(list, this);
        rv.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Pedidos pd = snapshot.getValue(Pedidos.class);
                        list.add(pd);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscar(s);
                return true;
            }
        });
    }

    private void buscar(String s) {
        ArrayList<Pedidos>lista = new ArrayList<>();
        for(Pedidos obj: list){
            if(obj.getGustos().toLowerCase().contains(s.toLowerCase())){
                lista.add(obj);
            }

        }

        AdapterPedidos adapter = new AdapterPedidos(lista, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void pedidosClick(int position) {
        Log.d(TAG, "pedidosClick: clicked.");
//        Intent intent = new Intent(this, FirebaseChat.class);
        Intent intent = new Intent(this, HistoryBookingDetailColaboradorActivity.class);
        intent.putExtra("some_object", "something else");
        startActivity(intent);
    }
}