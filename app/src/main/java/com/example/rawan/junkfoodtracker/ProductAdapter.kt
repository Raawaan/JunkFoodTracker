package com.example.rawan.junkfoodtracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import kotlinx.android.synthetic.main.product_item.view.*

/**
 * Created by rawan on 18/09/18.
 */
class ProductAdapter(private val productList: List<BrandNameAndCounter>?): RecyclerView.Adapter<ProductAdapter.ProductHolderView>(){
    open class ProductHolderView(itemView:View):RecyclerView.ViewHolder(itemView)
    override fun onBindViewHolder(holder: ProductHolderView, position: Int) {
            val listOfProducts = productList?.get(position)
            holder.itemView.tvproducts.append(listOfProducts?.brandName+listOfProducts?.counter.toString())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolderView {
        return ProductHolderView(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }
    override fun getItemCount() = productList!!.size
}