package com.example.rawan.junkfoodtracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.product_item.view.*

/**
 * Created by rawan on 18/09/18.
 */
class ProductAdapter(val productList: List<String>?): RecyclerView.Adapter<ProductAdapter.ProductViewholderView>(){
    open class ProductViewholderView(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ProductViewholderView, position: Int) {
        if (holder!=null){
            holder.itemView.tvproducts.text = productList?.get(position)
        }    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewholderView {
        return ProductViewholderView(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }


    override fun getItemCount() = productList!!.size
}