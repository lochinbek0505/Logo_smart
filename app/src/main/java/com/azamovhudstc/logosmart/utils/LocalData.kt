package com.azamovhudstc.logosmart.utils

import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.data.model.*

object LocalData {

    val soundLessonList = arrayListOf(
        SoundLessonModel(R.drawable.ic_alphabet, "R tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.l_alphabet, "L tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.simg, "S tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.yimg, "Y tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.ic_alphabet, "R tovushini rivojlantirish", 32),
    )
    val trainingList = listOf(
        TrainingLayoutsModel(
            "Nutq chiqazish",
            "2+ yosh",
            R.color.pink_50,
            R.drawable.ic_person_1,
            R.drawable.icon_sound_1,
            R.color.pink,
            R.drawable.ic_next_button_whitebg
        ),
        TrainingLayoutsModel(
            "Artikulatsiya mashqlari",
            "3-5 yosh",
            R.color.orange_50,
            R.drawable.ic_person_2,
            R.drawable.icon_sound_2,
            R.color.basicBlue,
            R.drawable.ic_next_button_bluebg
        ),
        TrainingLayoutsModel(
            "Video darslar",
            "3-5 yosh",
            R.color.blue_50,
            R.drawable.ic_person_3,
            R.drawable.icon_sound_3,
            R.color.grey,
            R.drawable.ic_next_button_greybg
        ),
        TrainingLayoutsModel(
            "Diagnoz bo'yicha\nmashg'ulotlar",
            "0-5 yosh",
            R.color.orange_50,
            R.drawable.ic_person_4,
            R.drawable.icon_sound_4,
            R.color.basicBlue,
            R.drawable.ic_next_button_bluebg
        )
    )


    var gameTypeList = arrayListOf<GameTypeModel>(
        GameTypeModel(
            R.drawable.item_choose_img,
            "3 yoshgacha\nbolalar uchun",
            "10 ta oyin"
            ), GameTypeModel(
            R.drawable.item_choose_img2,
            "3 yoshdan 6\nyoshgacha",
            "16 ta oyin"
            ), GameTypeModel(
            R.drawable.item_choose_img3,
            "6 yoshdan katta\nbolalar uchun",
            "16 ta oyin"
            ),
                GameTypeModel(
                R.drawable.item_choose_img,
        "3 yoshgacha\nbolalar uchun",
        "10 ta oyin"
    ), GameTypeModel(
    R.drawable.item_choose_img2,
    "3 yoshdan 6\nyoshgacha",
    "16 ta oyin"
    ), GameTypeModel(
    R.drawable.item_choose_img3,
    "6 yoshdan katta\nbolalar uchun",
    "16 ta oyin"
    )
    )


    var gameList = arrayListOf<GameNestedModel>(
        GameNestedModel(
            "Artikulyatsiya o’yinlari",
            arrayListOf(GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ))
        ),
        GameNestedModel(
            "Nafas mashqlari",
            arrayListOf(GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ))
        ),
        GameNestedModel(
            "Tovushlar ustida ishlash",
            arrayListOf(GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ),GameModel(
                1,
                R.drawable.item_choose_img,
                "Oyinchoqlarni joylashtirish"
            ))
        )
    )
}