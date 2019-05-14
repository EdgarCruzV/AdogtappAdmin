package com.huellitas.adogtappadmin.MenuFragments.BajaExtras


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
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaClass
import com.huellitas.adogtappadmin.MenuFragments.BajaTienda.BajaTiendaRecycler
import kotlinx.android.synthetic.main.extras_recycler.view.*
import kotlinx.android.synthetic.main.tienda_recycler.view.*


class BajaExtrasAdapter(private val extrasList: ArrayList<BajaExtrasClass>,
                        private val listener: ClickListener):
    RecyclerView.Adapter<BajaExtrasAdapter.ViewHolder>() {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context)
            .inflate(com.huellitas.adogtappadmin.R.layout.extras_recycler, p0, false)
        mContext = p0.getContext()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return extrasList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val extras : BajaExtrasClass = extrasList[p1]
        val item = "${extras.articulo}_${extras.marca}_${extras.cantidad}"
        p0.bind(item, listener)
        Glide.with(mContext)
            .asBitmap()
            .load(extras.foto)
            .into(p0.imageViewFoto)
        val a ="${extras.articulo}"
        val m ="Marca: ${extras.marca}"
        val p ="Precio: $${extras.precio}"
        val d ="Descripción: ${extras.descripcion}"
        val c ="Cantidad: ${extras.cantidad}"
        p0?.textViewArticulo?.text=a
        p0?.textViewMarca?.text=m
        p0?.textViewPrecio?.text=p
        p0?.textViewDescripcion?.text=d
        p0?.textViewCantidad?.text=c
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: String, listener: BajaExtrasAdapter.ClickListener) {
            eventTextView.setOnClickListener{
                listener.onItemClicked(item)
            }

        }
        val eventTextView = itemView.layout_extras_recycler
        //Data to Recycler
        val imageViewFoto = itemView.findViewById(com.huellitas.adogtappadmin.R.id.foto_extras_recycler) as ImageView
        val textViewArticulo = itemView.findViewById(com.huellitas.adogtappadmin.R.id.articulo_extras_recycler) as TextView
        val textViewMarca = itemView.findViewById(com.huellitas.adogtappadmin.R.id.marca_extras_recycler) as TextView
        val textViewCantidad = itemView.findViewById(com.huellitas.adogtappadmin.R.id.cantidad_extras_recycler) as TextView
        val textViewDescripcion = itemView.findViewById(com.huellitas.adogtappadmin.R.id.descripcion_extras_recycler) as TextView
        val textViewPrecio = itemView.findViewById(com.huellitas.adogtappadmin.R.id.precio_extras_recycler) as TextView
    }

    interface ClickListener {
        fun onItemClicked(item: String)
    }
}


