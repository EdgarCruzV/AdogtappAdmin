package com.huellitas.adogtappadmin.MenuFragments.BajaExtras

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritos
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritosAdapter
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritosClass
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTienda
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaClass
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaRecycler

import com.huellitas.adogtappadmin.R
import com.huellitas.adogtappadmin.User
import kotlinx.android.synthetic.main.fragment_actualizacion_datos.*
import kotlinx.android.synthetic.main.fragment_baja_extras.*
import kotlinx.android.synthetic.main.fragment_baja_perritos.*
import kotlinx.android.synthetic.main.fragment_baja_tienda.*
import kotlinx.android.synthetic.main.fragment_tienda.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BajaPerritos.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BajaPerritos.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BajaExtras : Fragment(), BajaExtrasAdapter.ClickListener {
    override fun onItemClicked(item: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Baja de artículo en la sección 'Extras'")
        builder.setMessage("¿Está seguro de eliminar este artículo?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val db = FirebaseDatabase.getInstance().reference.child("extras/$item")
            db.removeValue().addOnSuccessListener {
                var ref = FirebaseStorage.getInstance()
                    .getReference("/Extras/${item}")
                ref.delete().addOnSuccessListener {
                    //Toast
                    Toast.makeText(context, "Artículo eliminado exitosamente", Toast.LENGTH_LONG).show()
                    val adoptarFragment = BajaExtras.newInstance()
                    fragmentManager!!.beginTransaction().replace(R.id.fragment_container, adoptarFragment).commit();

                }
            }
        }
        builder.setNegativeButton("No"){dialog, which->
            //Nada
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    // TODO: Rename and change types of parameters
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_baja_extras, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = baja_extras_recycler
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        //DATA!!
        val tienda = ArrayList<BajaExtrasClass>()
        val db = FirebaseDatabase.getInstance().getReference()

        db.addValueEventListener(

            object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.child("extras").children) {
                        val foto = d.child("foto").value!!.toString()
                        val articulos = d.child("articulo").value!!.toString()
                        val marca = d.child("marca").value!!.toString()
                        val descripcion = d.child("descripcion").value!!.toString()
                        val cantidad = d.child("cantidad").value!!.toString()
                        val precio = d.child("precio").value!!.toString()
                        tienda.add(BajaExtrasClass(foto, articulos, marca, descripcion,precio,cantidad))

                    }
                    val adapter = BajaExtrasAdapter(tienda, this@BajaExtras)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
        )

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BajaPerritos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            BajaExtras().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
