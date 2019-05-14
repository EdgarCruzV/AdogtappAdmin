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
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Perritos : Fragment(), AdapterView.OnItemSelectedListener {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var genero = ""

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        /*public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedCar = position;
            String item = (String) parent.getItemAtPosition(position);
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#b911bc"));
        }*/
        val s = parent.getItemAtPosition(pos).toString()
        if(s == "Género"){

        }
        if(s == "Macho"){
            genero = "Macho"
        }
        if(s == "Hembra"){
            genero = "Hembra"
        }
        //Toast.makeText(context, parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG ).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
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
        if(requestCode==456 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(getActivity()!!.getContentResolver(), selectedPhotoUri)
            perrito_foto_button.setImageBitmap(bitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Dar de alta a un perrito")
        builder.setMessage("Llene todos los campos para dar de alta a un perrito en adopción.")
        builder.setNeutralButton("Entendido"){dialog, which->}
        registrar_perrito.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
        perrito_foto_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 456)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

        val spinner: Spinner = spinner

        spinner.setSelection(2);
        // Create an ArrayAdapter using the string array and a default spinner layout

        /*ArrayAdapter.createFromResource(
            context,
            R.array.spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this*/

        val objects: List<String> = ArrayList<String>().also {
            it.add("Macho")
            it.add("Hembra")
            // add hint as last item
            it.add("Género")
        }
        val adapter = HintAdapter(context, android.R.layout.simple_spinner_item, objects)


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        // show hint
        spinner.setSelection(adapter.count)
        spinner.onItemSelectedListener = this

    }
    private fun uploadImageToFirebaseStorage() {
        val nombre = nombre_perrito.text.toString()
        val edad = edad_perrito.text.toString()
        val talla = talla_perrito.text.toString()
        val descripcion = descripcion_perrito.text.toString()
        val contacto = contacto_perrito.text.toString()

        if (genero == "" || selectedPhotoUri == null || nombre.isEmpty() || edad.isEmpty()|| talla.isEmpty() || descripcion.isEmpty() || contacto.isEmpty()) {
            Toast.makeText(context, "Favor de llenar todos los campos e insertar imagen.", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(context, "Realizando registro, un momento por favor.", Toast.LENGTH_LONG).show()
        val filename = nombre_perrito.text.toString()
        val ref = FirebaseStorage.getInstance().getReference("/Perritos/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("AdogtApp", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("AdogtApp", "File Location: $it")
                    upload(nombre, edad, talla, descripcion, contacto, it.toString(), genero)
                }
            }
            .addOnFailureListener {
                Log.d("AdogtApp", "Failed to upload image to storage: ${it.message}")
            }
    }
    private fun upload(nombre: String, edad: String, talla: String, descripcion: String,
                       contacto: String, uri: String, generoP : String){
        val ref = FirebaseDatabase.getInstance().getReference("/perritos/$nombre")
        val perrito = Perrito(nombre, edad, talla, descripcion, contacto, uri, generoP)
        ref.setValue(perrito).addOnSuccessListener {
            Toast.makeText(context, "Registro exitoso, ingrese datos nuevamente para agregar otro perrito",
                Toast.LENGTH_LONG).show()
            genero = ""

            spinner.setSelection(2)
            nombre_perrito.text.clear()
            edad_perrito.text.clear()
            talla_perrito.text.clear()
            descripcion_perrito.text.clear()
            contacto_perrito.text.clear()
            selectedPhotoUri = null
            perrito_foto_button.setImageResource(com.huellitas.adogtappadmin.R.drawable.redonditos_register_photo_button)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.huellitas.adogtappadmin.R.layout.fragment_perritos, container, false)
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
            Perritos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

data class Perrito(
    val nombre: String = "",
    val edad: String = "",
    val talla: String = "",
    val descripcion: String = "",
    val contacto: String = "",
    val foto: String = "",
    val genero : String = ""
)

