package com.huellitas.adogtappadmin.MenuFragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.huellitas.adogtappadmin.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_actualizacion_datos.*
import kotlinx.android.synthetic.main.fragment_perritos.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import java.io.File
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.ByteArrayOutputStream
import java.net.URL


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActualizacionDatos : Fragment() {
    var userPhotoUrl = ""
    var flag = false
    var error = false
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==457 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(getActivity()!!
                .getContentResolver(), selectedPhotoUri)
            photo_user_button_act.setImageBitmap(bitmap)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Actualización de Datos")
        builder.setMessage("Llene uno o más campos para actualizar en su cuenta.")
        builder.setNeutralButton("Entendido"){dialog, which->}
        actualizar_datos.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
        photo_user_button_act.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 457)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()


    }
    private fun uploadImageToFirebaseStorage() {
        val username = username_act.text.toString()
        val email = email_act.text.toString()
        val password = password_act.text.toString()
        val passwordConfirm = confirm_paswword_act.text.toString()
        var newUrl = ""

        //Todos los campos vacíos
        if(username.isEmpty() && email.isEmpty() && password.isEmpty()
            && passwordConfirm.isEmpty() && selectedPhotoUri == null){
            Toast.makeText(context, "No hay campos que actualizar.", Toast.LENGTH_LONG).show()
            return
        }else{
            Toast.makeText(context, "Realizando actualización, un momento por favor.", Toast.LENGTH_LONG).show()
        }
        //Actualizar Contraseña
        if(!password.isEmpty() || !passwordConfirm.isEmpty()){
            if (password != passwordConfirm) {
                Toast.makeText(context, "La contraseña no coincide.", Toast.LENGTH_LONG).show()
                return
            }else{
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                var user: FirebaseUser? = mAuth.getCurrentUser()
                user!!.updatePassword(password).addOnSuccessListener {
                    password_act.text.clear()
                    confirm_paswword_act.text.clear()
                    Toast.makeText(context, "Contraseña actualizada correctamente.", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    error = true
                    Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                }
            }
        }
        //Actualizar Email
        if(!email.isEmpty()){
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            var user: FirebaseUser? = mAuth.getCurrentUser()

             //Actualizo email
             user!!.updateEmail(email).addOnSuccessListener {
                Toast.makeText(context, "Email actualizado correctamente.", Toast.LENGTH_LONG).show()
                 email_act.text.clear()

              }.addOnFailureListener {
                error = true
                Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
              }
        }

        //Actualizar Username
        if(!username.isEmpty()){
            var _db= FirebaseDatabase.getInstance().getReference("/users/Administrator")
            val map: MutableMap<String, Any> = mutableMapOf<String, Any>()
            map["username"] = username
            _db.updateChildren(map).addOnSuccessListener {
                username_act.text.clear()
                Toast.makeText(context, "Nombre de usuario actualizado correctamente.", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                error = true
                Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
            }
        }
        //Actualizar imagen
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = mAuth.getCurrentUser()

        if(selectedPhotoUri != null) {
            var ref = FirebaseStorage.getInstance()
                .getReference("/Administrator/${user!!.uid.toString()}")
            ref.delete()
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    flag = true
                    Log.d("AdogtApp", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        flag = true
                        var _db= FirebaseDatabase.getInstance()
                            .getReference("/users/Administrator")
                        val map: MutableMap<String, Any> = mutableMapOf<String, Any>()
                        map["profileImageUrl"] = it.toString()
                        _db.updateChildren(map).addOnSuccessListener {
                            photo_user_button_act.setBackgroundResource(com.huellitas.adogtappadmin.R.drawable.redonditos_register_photo_button)
                            photo_user_button_act.setImageResource(com.huellitas.adogtappadmin.R.drawable.redonditos_register_photo_button)
                            selectedPhotoUri = null
                            Toast.makeText(context, "Imagen Actualizada Correctamente.",
                                Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.huellitas.adogtappadmin.R.layout.fragment_actualizacion_datos, container, false)
    }
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ActualizacionDatos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}