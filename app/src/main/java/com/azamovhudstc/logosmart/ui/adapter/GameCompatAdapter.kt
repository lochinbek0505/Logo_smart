package com.azamovhudstc.logosmart.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.app.LogoApp
import com.azamovhudstc.logosmart.data.model.GameModel
import com.azamovhudstc.logosmart.databinding.ItemGameCompatBinding
import com.azamovhudstc.logosmart.utils.setAnimation

class GameCompatAdapter:RecyclerView.Adapter<GameCompatAdapter.GameCompatVh>() {
    private var list =ArrayList<GameModel>(

    )
    inner class GameCompatVh(var binding:ItemGameCompatBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(data:GameModel){
                binding.gameTitle.text=data.gameTitle
                binding.typeIcon.setImageResource(data.gameImg)
                    setAnimation(LogoApp.currentContext()!!, binding.root)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCompatVh {
        return GameCompatVh(ItemGameCompatBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun submitList(newList:ArrayList<GameModel>){
        list.clear()
        list.addAll(newList)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameCompatVh, position: Int) {
        holder.onBind(list.get(position))
    }
}