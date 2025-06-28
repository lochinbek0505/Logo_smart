package com.azamovhudstc.logosmart.ui.screens.games.action_game

import com.azamovhudstc.logosmart.databinding.ActionGameBinding
import com.azamovhudstc.logosmart.utils.BaseFragment

class ActionGame:BaseFragment<ActionGameBinding>(ActionGameBinding::inflate) {

    private var list =ArrayList<String>()
    override fun onViewCreate() {
        list.add("Yomg‘ir yog‘di")
        list.add("Rasm chizdi")
        list.add("Arg‘imchoq uchdi")
        list.add("Arqon sakradi")
        list.add("Baraban chaldi")
        list.add("Varrak uchdi")
        
    }
}