package com.huellitas.adogtappadmin.MenuFragments.BajaPerritos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaRecycler
import kotlinx.android.synthetic.main.perritos_recycler.view.*
import kotlinx.android.synthetic.main.tienda_recycler.view.*


class BajaPerritosAdapter(private val perritosList: ArrayList<BajaPerritosClass>, private val listener: BajaPerritosAdapter.ClickListener): RecyclerView.Adapter<BajaPerritosAdapter.ViewHolder>() {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       // this.mContext = mContext

        val v = LayoutInflater.from(p0?.context).inflate(com.huellitas.adogtappadmin.R.layout.perritos_recycler, p0, false)
        mContext = p0.getContext()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return perritosList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val perritos : BajaPerritosClass = perritosList[p1]
        val item = perritos.nombre
        p0.bind(item, listener)
            Glide.with(mContext)
            .asBitmap()
            .load(perritos.foto)
            .into(p0.imageViewFoto)
        val n ="${perritos.nombre}"
        val e ="Edad: ${perritos.edad}"
        val c ="Contacto: ${perritos.contacto}"
        val d ="Descripción: ${perritos.descripcion}"
        val t ="Talla: ${perritos.talla}"
        val g = "Género: ${perritos.genero}"
        p0?.textViewName?.text=n
        p0?.textViewEdad?.text=e
        p0?.textViewContacto?.text=c
        p0?.textViewDescripcion?.text=d
        p0?.textViewTalla?.text=t
        p0?.textViewGenero?.text=g
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: String, listener: BajaPerritosAdapter.ClickListener) {
            eventTextView.setOnClickListener{
                listener.onItemClicked(item)
            }
        }
        val eventTextView = itemView.layout_perritos_recycler
        //Data to Recycler
        val imageViewFoto = itemView.findViewById(com.huellitas.adogtappadmin.R.id.foto_perritos_recycler) as ImageView
        val textViewName = itemView.findViewById(com.huellitas.adogtappadmin.R.id.nombre_perritos_recycler) as TextView
        val textViewEdad = itemView.findViewById(com.huellitas.adogtappadmin.R.id.edad_perritos_recycler) as TextView
        val textViewTalla = itemView.findViewById(com.huellitas.adogtappadmin.R.id.talla_perritos_recycler) as TextView
        val textViewDescripcion = itemView.findViewById(com.huellitas.adogtappadmin.R.id.descripcion_perritos_recycler) as TextView
        val textViewContacto = itemView.findViewById(com.huellitas.adogtappadmin.R.id.contacto_perritos_recycler) as TextView
        val textViewGenero = itemView.findViewById(com.huellitas.adogtappadmin.R.id.genero_perritos_recycler) as TextView
    }

    interface ClickListener {
        fun onItemClicked(item: String)
    }
}