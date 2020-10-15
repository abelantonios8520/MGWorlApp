package com.abelsalcedo.mgworlapp.activities.cliente

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.NonNull
import com.abelsalcedo.mgworlapp.R
import com.abelsalcedo.mgworlapp.providers.AuthProvider
import com.abelsalcedo.mgworlapp.providers.ClienteProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PedidoActivity : AppCompatActivity() {

    private var mTextViewName: TextView? = null
    private var mClienteProvider: ClienteProvider? = null
    private var mAuthProvider: AuthProvider? = null
    private val mProgressDialog: ProgressDialog? = null
    private val mName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        mClienteProvider = ClienteProvider()
        mAuthProvider = AuthProvider()
        mTextViewName = findViewById(R.id.pedidoNombre)
        getClienteInfopedido()



    }

    private fun getClienteInfopedido() {
        mClienteProvider!!.getCliente(mAuthProvider!!.id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    mTextViewName!!.text = name
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

}