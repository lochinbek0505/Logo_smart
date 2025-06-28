package com.azamovhudstc.logosmart.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.app.LogoApp
import com.azamovhudstc.logosmart.data.model.GameTypeModel
import com.azamovhudstc.logosmart.databinding.ItemChooseGameBinding
import com.azamovhudstc.logosmart.utils.setAnimation

class GameAdapter:RecyclerView.Adapter<GameAdapter.GameVh>() {

    private val  list= ArrayList<GameTypeModel>()

    private lateinit var itemClickedListener:(GameTypeModel)->Unit

    fun setItemClickedListener(listener:(GameTypeModel)->Unit){
        itemClickedListener=listener
    }


    inner class GameVh(var itemBinding: ItemChooseGameBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun onBind(data: GameTypeModel){
            itemBinding.root.setOnClickListener {
                itemClickedListener.invoke(data)
            }
            itemBinding.title.text=data.typeTitle
            itemBinding.typeIcon.setImageResource(data.imgRes)
            itemBinding.gameCount.text=data.gameCount
            setAnimation(LogoApp.currentContext()!!, itemBinding.root)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameVh {
        return  GameVh(ItemChooseGameBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameVh, position: Int) {
        holder.onBind(list.get(position))
    }

    fun submitList(newList:ArrayList<GameTypeModel>){
        list.clear()
        list.addAll(newList)
    }
}