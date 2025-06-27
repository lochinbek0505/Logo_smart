package com.azamovhudstc.logosmart.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.app.LogoApp
import com.azamovhudstc.logosmart.data.model.SoundLessonModel
import com.azamovhudstc.logosmart.databinding.ItemSoundLessonBinding
import com.azamovhudstc.logosmart.utils.setAnimation

class SoundLessonAdapter : RecyclerView.Adapter<SoundLessonAdapter.SoundLessonVh>() {
    private val list = ArrayList<SoundLessonModel>()

    private lateinit var itemClickListener: (SoundLessonModel, Int) -> Unit

    fun setTrainItemClickListener(listener: (SoundLessonModel, Int) -> Unit) {
        itemClickListener = listener
    }

    inner class SoundLessonVh(private val binding: ItemSoundLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(soundLesson: SoundLessonModel) {
            binding.apply {
                binding.root.setOnClickListener {
                    itemClickListener.invoke(soundLesson, adapterPosition)
                }
                setAnimation(LogoApp.currentContext()!!, binding.root)
                binding.lessonImgIcon.setImageResource(soundLesson.imgId)
                binding.lessonTitle.text = soundLesson.lessonTitle.toString()
//                binding.lessonCount.text = "${soundLesson.lessonCount} ta  mashgulot"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundLessonVh {
        return SoundLessonVh(
            ItemSoundLessonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun submitList(arrayList: ArrayList<SoundLessonModel>) {
        list.clear()
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SoundLessonVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}