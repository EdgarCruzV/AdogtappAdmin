package com.huellitas.adogtappadmin.MenuFragments.BajaPaseadores


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.huellitas.adogtappadmin.MenuFragments.BajaExtras.BajaExtrasAdapter
import com.huellitas.adogtappadmin.MenuFragments.BajaExtras.BajaExtrasClass
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritosAdapter
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritosClass
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaClass
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaRecycler
import kotlinx.android.synthetic.main.extras_recycler.view.*
import kotlinx.android.synthetic.main.paseadores_recycler.view.*
import kotlinx.android.synthetic.main.tienda_recycler.view.*


class BajaPaseadoresAdapter(private val paseadoresList: ArrayList<BajaPaseadoresClass>,
                        private val listener: ClickListener):
    RecyclerView.Adapter<BajaPaseadoresAdapter.ViewHolder>() {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context)
            .inflate(com.huellitas.adogtappadmin.R.layout.paseadores_recycler, p0, false)
        mContext = p0.getContext()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return paseadoresList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val paseadores : BajaPaseadoresClass = paseadoresList[p1]
        val item = "${paseadores.nombre}_${paseadores.celular}"
        p0.bind(item, listener)
        Glide.with(mContext)
            .asBitmap()
            .load(paseadores.foto)
            .into(p0.imageViewFoto)
        val n ="${paseadores.nombre}"
        val c ="Col. ${paseadores.colonia}"
        val t ="Cel. ${paseadores.celular}"
        val d ="${paseadores.descripcion}"
        val h ="${paseadores.horario}"
        p0?.textViewNombre?.text=n
        p0?.textViewColonia?.text=c
        p0?.textViewCelular?.text=t
        p0?.textViewDescripcion?.text=d
        p0?.textViewHorario?.text=h
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: String, listener: BajaPaseadoresAdapter.ClickListener) {
            eventTextView.setOnClickListener{
                listener.onItemClicked(item)
            }

        }
        val eventTextView = itemView.layout_paseadores_recycler
        //Data to Recycler
        val imageViewFoto = itemView.findViewById(com.huellitas.adogtappadmin.R.id.foto_paseadores_recycler) as ImageView
        val textViewNombre = itemView.findViewById(com.huellitas.adogtappadmin.R.id.nombre_paseadores_recycler) as TextView
        val textViewCelular = itemView.findViewById(com.huellitas.adogtappadmin.R.id.celular_paseadores_recycler) as TextView
        val textViewColonia = itemView.findViewById(com.huellitas.adogtappadmin.R.id.colonia_paseadores_recycler) as TextView
        val textViewDescripcion = itemView.findViewById(com.huellitas.adogtappadmin.R.id.descripcion_paseadores_recycler) as TextView
        val textViewHorario = itemView.findViewById(com.huellitas.adogtappadmin.R.id.horario_paseadores_recycler) as TextView
    }

    interface ClickListener {
        fun onItemClicked(item: String)
    }
}


