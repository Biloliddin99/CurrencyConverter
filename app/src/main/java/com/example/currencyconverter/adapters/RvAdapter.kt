package com.example.currencyconverter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemRvBinding
import com.example.currencyconverter.models.MyCurrency
import uz.biloliddinuz.currencyconverter.databinding.ItemRvBinding

class RvAdapter(val context: Context,val list: List<MyCurrency>, val rvClick: RvClick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: MyCurrency, position: Int) {
            if (user.Ccy!="XDR"){
                itemRvBinding.apply {
                    tvName.text = "1 "+user.CcyNm_EN
                    tvNumber.text = user.Rate+" So'm"
                    tvLabel.text = user.Ccy
                    flagImage.countryCode = user.Ccy.substring(0,2)
                }
                itemRvBinding.card.setOnClickListener {
                    rvClick.itemClicked(user,position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) = holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}
interface RvClick{
    fun itemClicked(user: MyCurrency,position: Int)
}