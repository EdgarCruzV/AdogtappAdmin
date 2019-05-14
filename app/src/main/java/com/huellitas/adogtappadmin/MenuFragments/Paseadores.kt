package com.huellitas.adogtappadmin.MenuFragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.huellitas.adogtappadmin.R
import kotlinx.android.synthetic.main.fragment_actualizacion_datos.*
import kotlinx.android.synthetic.main.fragment_perritos.*
import HintAdapter
import android.graphics.Color
import android.widget.*
import kotlinx.android.synthetic.main.fragment_extras.*
import kotlinx.android.synthetic.main.fragment_paseadores.*
import kotlinx.android.synthetic.main.fragment_perritos.view.*
import kotlinx.android.synthetic.main.fragment_tienda.*
import kotlinx.android.synthetic.main.tienda_recycler.*
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Paseadores : Fragment() {
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
        if(requestCode==239 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media
                .getBitmap(getActivity()!!.getContentResolver(), selectedPhotoUri)
            paseadores_foto_button.setImageBitmap(bitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Dar de alta un paseador")
        builder.setMessage("Llene todos los campos para dar de alta un paseador.")
        builder.setNeutralButton("Entendido"){dialog, which->}
        registrar_paseadores.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
        paseadores_foto_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 239)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun uploadImageToFirebaseStorage() {
        val nombre = nombre_paseadores.text.toString()
        val colonia = colonia_paseadores.text.toString()
        val celular = celular_paseadores.text.toString()
        val descripcion = descripcion_paseadores.text.toString()
        val horario = horario_paseadores.text.toString()

        if ( selectedPhotoUri == null || nombre.isEmpty() || colonia.isEmpty()|| celular.isEmpty()
            || descripcion.isEmpty() || horario.isEmpty()) {
            Toast.makeText(context, "Favor de llenar todos los campos e insertar imagen.",
                Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(context, "Realizando registro, un momento por favor.", Toast.LENGTH_LONG).show()
        val filename = "${nombre}_${celular}"
        val ref = FirebaseStorage.getInstance().getReference("/Paseadores/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("AdogtApp", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("AdogtApp", "File Location: $it")
                    upload(nombre, colonia, celular, descripcion, horario, it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("AdogtApp", "Failed to upload image to storage: ${it.message}")
            }
    }
    private fun upload(nombre: String, colonia: String, celular: String, descripcion: String,
                       horario: String, uri: String){
        val path = "${nombre}_${celular}"
        val ref = FirebaseDatabase.getInstance().getReference("/paseadores/$path")
        val paseador = Paseador(nombre, colonia, celular, descripcion, horario, uri)
        ref.setValue(paseador).addOnSuccessListener {
            Toast.makeText(context, "Registro exitoso, ingrese datos nuevamente para agregar otro paseador."
                , Toast.LENGTH_LONG).show()

            nombre_paseadores.text.clear()
            colonia_paseadores.text.clear()
            celular_paseadores.text.clear()
            descripcion_paseadores.text.clear()
            horario_paseadores.text.clear()
            selectedPhotoUri = null
            paseadores_foto_button.setImageResource(com.huellitas.adogtappadmin.R.drawable.redonditos_register_photo_button)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.huellitas.adogtappadmin.R.layout.fragment_paseadores, container,
            false)
    }

    // TODO: Rename method, update argument and hook method into UI event
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

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            Paseadores().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

data class Paseador(
    val nombre: String = "",
    val colonia: String = "",
    val celular: String = "",
    val descripcion: String = "",
    val horario: String = "",
    val foto: String = ""
)

