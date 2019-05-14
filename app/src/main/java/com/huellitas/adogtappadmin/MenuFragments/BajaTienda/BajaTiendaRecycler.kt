package com.huellitas.adogtappadmin.MenuFragments.BajaTienda


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritosAdapter
import com.huellitas.adogtappadmin.MenuFragments.BajaPerritos.BajaPerritosClass
import kotlinx.android.synthetic.main.tienda_recycler.view.*


class BajaTiendaRecycler(private val tiendaList: ArrayList<BajaTiendaClass>, private val listener: ClickListener): RecyclerView.Adapter<BajaTiendaRecycler.ViewHolder>() {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context)
            .inflate(com.huellitas.adogtappadmin.R.layout.tienda_recycler, p0, false)
        mContext = p0.getContext()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return tiendaList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val tienda : BajaTiendaClass = tiendaList[p1]
        val item = "${tienda.articulo}_${tienda.marca}_${tienda.cantidad}"
        p0.bind(item, listener)
        Glide.with(mContext)
            .asBitmap()
            .load(tienda.foto)
            .into(p0.imageViewFoto)
        val a ="${tienda.articulo}"
        val m ="Marca: ${tienda.marca}"
        val p ="Precio: $${tienda.precio}"
        val d ="Descripción: ${tienda.descripcion}"
        val c ="Cantidad: ${tienda.cantidad}"
        p0?.textViewArticulo?.text=a
        p0?.textViewMarca?.text=m
        p0?.textViewPrecio?.text=p
        p0?.textViewDescripcion?.text=d
        p0?.textViewCantidad?.text=c
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: String, listener: BajaTiendaRecycler.ClickListener) {
            eventTextView.setOnClickListener{
                listener.onItemClicked(item)
            }

        }
        val eventTextView = itemView.layout_tienda_recycler
        //Data to Recycler
        val imageViewFoto = itemView.findViewById(com.huellitas.adogtappadmin.R.id.foto_tienda_recycler) as ImageView
        val textViewArticulo = itemView.findViewById(com.huellitas.adogtappadmin.R.id.articulo_tienda_recycler) as TextView
        val textViewMarca = itemView.findViewById(com.huellitas.adogtappadmin.R.id.marca_tienda_recycler) as TextView
        val textViewCantidad = itemView.findViewById(com.huellitas.adogtappadmin.R.id.cantidad_tienda_recycler) as TextView
        val textViewDescripcion = itemView.findViewById(com.huellitas.adogtappadmin.R.id.descripcion_tienda_recycler) as TextView
        val textViewPrecio = itemView.findViewById(com.huellitas.adogtappadmin.R.id.precio_tienda_recycler) as TextView
        }

    interface ClickListener {
        fun onItemClicked(item: String)
    }
}


