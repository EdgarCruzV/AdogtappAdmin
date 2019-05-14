package com.huellitas.adogtappadmin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*

class LoginActivity : AppCompatActivity() {

    lateinit var _db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            doLogin()
            //finish?
            //finish()
        }



    }
    private fun verifyAdmin(){
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = mAuth.getCurrentUser()
        _db = FirebaseDatabase.getInstance().getReference("/users")


        if (user != null) {
            if (user!!.uid != null && user!!.uid == "Zmm2TSfx8Xc8fVuZOins5sEsYX92") {
                Toast.makeText(this, "Welcome, Administrator!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else{
                Toast.makeText(this, "You are not administrator and do not have access.", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "User doesn't exist.", Toast.LENGTH_SHORT).show()

        }


    }

    private  fun doLogin(){
        val email = email_edittext.text.toString()
        val password = password_edittext.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                finish()
                verifyAdmin()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,
                    " id: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}





