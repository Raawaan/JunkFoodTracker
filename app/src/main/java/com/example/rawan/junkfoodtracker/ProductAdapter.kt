package com.example.rawan.junkfoodtracker

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import kotlinx.android.synthetic.main.product_item.view.*

/**
 * Created by rawan on 18/09/18.
 */
class ProductAdapter(val productList: List<BrandNameAndCounter>?): RecyclerView.Adapter<ProductAdapter.ProductViewholderView>(){
    open class ProductViewholderView(itemView:View):RecyclerView.ViewHolder(itemView)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewholderView, position: Int) {
        if (holder!=null){
            val lista = productList?.get(position)
            holder.itemView.tvproducts.text =lista?.brandName+" " +
                    ""+lista?.counter.toString()

        }    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewholderView {
        return ProductViewholderView(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }


    override fun getItemCount() = productList!!.size
}