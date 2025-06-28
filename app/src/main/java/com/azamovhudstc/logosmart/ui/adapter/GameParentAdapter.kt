package com.azamovhudstc.logosmart.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.data.model.GameNestedModel
import com.azamovhudstc.logosmart.databinding.ItemGameMiddleBinding
import com.azamovhudstc.logosmart.utils.setSlideIn
import com.azamovhudstc.logosmart.utils.slideStart
import com.azamovhudstc.logosmart.utils.slideUp

class GameParentAdapter:RecyclerView.Adapter<GameParentAdapter.GameParentVh>(){
    private val list =ArrayList<GameNestedModel>()

    private lateinit var viewAllClickListener:(GameNestedModel)->Unit

    fun setViewAllClickListener(listener:(GameNestedModel)->Unit){
        viewAllClickListener=listener
    }
    inner class GameParentVh(var binding:ItemGameMiddleBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(data:GameNestedModel){
            binding.nestedTitle.text=data.title
            binding.viewAll.slideUp(800,0)
            binding.viewAll.setOnClickListener {
                viewAllClickListener.invoke(data)
            }
            binding.nestedTitle.slideStart(800,0)
            val adapter= GameCompatAdapter()
            binding.nestedGameRv.layoutAnimation =
                LayoutAnimationController(setSlideIn(), 0.25f)
            adapter.submitList(data.list)
            binding.nestedGameRv.adapter=adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameParentVh {
        return GameParentVh(ItemGameMiddleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(newList:ArrayList<GameNestedModel>){
        list.clear()
        list.addAll(newList)
    }
    override fun onBindViewHolder(holder: GameParentVh, position: Int) {
        holder.onBind(list[position])
    }
}