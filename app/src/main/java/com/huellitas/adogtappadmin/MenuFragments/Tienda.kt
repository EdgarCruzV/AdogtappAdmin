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
import kotlinx.android.synthetic.main.fragment_perritos.view.*
import kotlinx.android.synthetic.main.fragment_tienda.*
import kotlinx.android.synthetic.main.tienda_recycler.*
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Tienda : Fragment() {
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
        if(requestCode==459 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(getActivity()!!.getContentResolver(), selectedPhotoUri)
            tienda_foto_button.setImageBitmap(bitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Dar de alta un artículo en la tienda")
        builder.setMessage("Llene todos los campos para dar de alta un artículo en la tienda.")
        builder.setNeutralButton("Entendido"){dialog, which->}
        registrar_tienda.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
        tienda_foto_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 459)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun uploadImageToFirebaseStorage() {
        val articulo = articulo_tienda.text.toString()
        val marca = marca_tienda.text.toString()
        val precio = precio_tienda.text.toString()
        val descripcion = descripcion_tienda.text.toString()
        val cantidad = cantidad_tienda.text.toString()

        if ( selectedPhotoUri == null || articulo.isEmpty() || marca.isEmpty()|| precio.isEmpty() || descripcion.isEmpty() || cantidad.isEmpty()) {
            Toast.makeText(context, "Favor de llenar todos los campos e insertar imagen.", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(context, "Realizando registro, un momento por favor.", Toast.LENGTH_LONG).show()
        val filename = "${articulo_tienda.text.toString()}_${marca}_${cantidad}"
        val ref = FirebaseStorage.getInstance().getReference("/Tienda/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("AdogtApp", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("AdogtApp", "File Location: $it")
                    upload(articulo, marca, cantidad, precio, descripcion, it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("AdogtApp", "Failed to upload image to storage: ${it.message}")
            }
    }
    private fun upload(articulo: String, marca: String, cantidad: String, precio: String,
                       descripcion: String, uri: String){
        val path = "${articulo_tienda.text.toString()}_${marca}_${cantidad}"
        val ref = FirebaseDatabase.getInstance().getReference("/tienda/$path")
        val tienda = Tiendas(articulo, marca, cantidad, precio, descripcion, uri)
        ref.setValue(tienda).addOnSuccessListener {
            Toast.makeText(context, "Registro exitoso, ingrese datos nuevamente para agregar otro artículo",
                Toast.LENGTH_LONG).show()

            articulo_tienda.text.clear()
            marca_tienda.text.clear()
            descripcion_tienda.text.clear()
            precio_tienda.text.clear()
            cantidad_tienda.text.clear()
            selectedPhotoUri = null
            tienda_foto_button.setImageResource(com.huellitas.adogtappadmin.R.drawable.redonditos_register_photo_button)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.huellitas.adogtappadmin.R.layout.fragment_tienda, container, false)
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
            Tienda().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

data class Tiendas(
    val articulo: String = "",
    val marca: String = "",
    val cantidad: String = "",
    val precio: String = "",
    val descripcion: String = "",
    val foto: String = ""
)

