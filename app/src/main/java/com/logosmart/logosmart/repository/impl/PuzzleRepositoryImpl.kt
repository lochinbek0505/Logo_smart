package com.azamovhudstc.logosmart.repository.impl

import com.azamovhudstc.logosmart.repository.PuzzleRepository


class PuzzleRepositoryImpl : PuzzleRepository {
    override val easy: ArrayList<Int> = ArrayList()

    init {
        easy.add(com.azamovhudstc.logosmart.R.drawable.bulgor)
        easy.add(com.azamovhudstc.logosmart.R.drawable.tomato)
        easy.add(com.azamovhudstc.logosmart.R.drawable.blindly)
        easy.add(com.azamovhudstc.logosmart.R.drawable.cucumber)

    }
}