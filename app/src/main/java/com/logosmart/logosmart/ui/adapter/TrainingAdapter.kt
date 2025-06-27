package com.azamovhudstc.logosmart.ui.adapter

import android.graphics.PorterDuff
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.data.model.TrainingLayoutsModel
import com.azamovhudstc.logosmart.databinding.ItemLayoutTrainingBinding
import com.azamovhudstc.logosmart.utils.LocalData
import com.azamovhudstc.logosmart.utils.inflateViewBinding
import com.azamovhudstc.logosmart.utils.slideStart
import com.azamovhudstc.logosmart.utils.slideUp

class TrainingAdapter : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    private val trainingList = LocalData.trainingList

    private lateinit var itemClickListener: (TrainingLayoutsModel, Int) -> Unit

    fun setTrainItemClickListener(listener: (TrainingLayoutsModel, Int) -> Unit) {
        itemClickListener = listener
    }

    inner class TrainingViewHolder(private val binding: ItemLayoutTrainingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val model = trainingList[position]
            binding.gameAge.text = model.gameAge
            binding.trainingTitle.text = model.trainingTitle
            binding.gameStart.setOnClickListener {
                itemClickListener.invoke(model, position)
            }
            binding.gameStart.setOnClickListener {
                itemClickListener.invoke(model, position)
            }
            binding.gameAge.slideUp(700,1)
            binding.trainingTitle.slideUp(700,1)
            binding.gameStart.slideUp(700,1)
            binding.itemcontainer.slideUp(700,1)
            binding.itemcontainer.slideStart(500,1)

            val layoutParams = binding.littleIcon.layoutParams as RelativeLayout.LayoutParams
            layoutParams.apply {
                if (position % 2 == 1) {
                    addRule(RelativeLayout.ALIGN_PARENT_START)
                    removeRule(RelativeLayout.ALIGN_PARENT_END)
                } else {
                    addRule(RelativeLayout.ALIGN_PARENT_END)
                    removeRule(RelativeLayout.ALIGN_PARENT_START)
                }
            }

            binding.littleIcon.layoutParams = layoutParams
            binding.littleIcon.setImageResource(model.littleIcon)
            binding.gameImage.setImageResource(model.gameImage)
            binding.gameBg.setColorFilter(
                ContextCompat.getColor(binding.root.context, model.gameBg),
                PorterDuff.Mode.SRC_IN
            )
            binding.gameStart.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    model.buttonColor
                )
            )
            binding.gameStart.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(binding.root.context, model.buttonIcon),
                null
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val binding = parent.inflateViewBinding(ItemLayoutTrainingBinding::class)
        return TrainingViewHolder(binding)
    }

    override fun getItemCount(): Int = trainingList.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.run {
            onBind(position)
        }
    }
}