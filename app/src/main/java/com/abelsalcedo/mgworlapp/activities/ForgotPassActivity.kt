package com.abelsalcedo.mgworlapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abelsalcedo.mgworlapp.R
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var mTextInputEmail: EditText
    private lateinit var auth:FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var mCircleImageView: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        mCircleImageView = findViewById(R.id.circleImageBack);
        mTextInputEmail=findViewById(R.id.txtEmail)
        progressBar=findViewById(R.id.progressBar)
        auth= FirebaseAuth.getInstance()

        mCircleImageView.setOnClickListener(View.OnClickListener { finish() })
    }

    fun send(view: View) {
        val email=mTextInputEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this){
                        task ->
                        if (task.isSuccessful){
                            progressBar.visibility=View.VISIBLE
                            startActivity(Intent(this, LoginActivity::class.java))
                        }else{
                            Toast.makeText(this, "Error al enviar el email", Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }
}