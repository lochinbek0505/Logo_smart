package com.azamovhudstc.logosmart.ui.screens.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.R

class VideoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_video, container, false)

//        recyclerView = view.findViewById(R.id.videoRecyclerView)
//
//        val videoList = listOf(
//            Video("1. Dars nomi", "K18cpp_-gP8"),
//            Video("2. Dars nomi", "nPt8bK2gbaU"),
//            Video("3. Dars nomi", "iLnmTe5Q2Qw")
//        )
//
//        recyclerView.adapter = VideoAdapter(requireContext(), videoList)

        return view
    }
}
