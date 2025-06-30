package com.azamovhudstc.logosmart.utils

import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.data.model.GameModel
import com.azamovhudstc.logosmart.data.model.GameNestedModel
import com.azamovhudstc.logosmart.data.model.GameTypeModel
import com.azamovhudstc.logosmart.data.model.SoundLessonModel
import com.azamovhudstc.logosmart.data.model.TrainingLayoutsModel

object LocalData {

    val soundLessonList = arrayListOf(
        SoundLessonModel(R.drawable.ic_alphabet, "R tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.l_alphabet, "L tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.simg, "S tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.yimg, "Y tovushini rivojlantirish", 32),
        SoundLessonModel(R.drawable.k, "K tovushini rivojlantirish", 32),
    )
    val trainingList = listOf(

        TrainingLayoutsModel(
            "Ekpress-diagnostika",
            "0-5 yosh",
            R.color.basicBlue,
            R.drawable.persona2,
            R.drawable.icon_sound_4,
            R.color.orange,
            R.drawable.ic_next_orange
        ),
        TrainingLayoutsModel(
            "Ilk nutqni rivojlantirish",
            "2+ yosh",
            R.color.pink_50,
            R.drawable.ic_person_1,
            R.drawable.icon_sound_1,
            R.color.pink,
            R.drawable.ic_next_button_whitebg
        ),
        TrainingLayoutsModel(
            "Tovushlar talaffuzini rivojlantirish",
            "3-5 yosh",
            R.color.orange_50,
            R.drawable.ic_person_2,
            R.drawable.icon_sound_2,
            R.color.basicBlue,
            R.drawable.ic_next_button_bluebg
        ),
        TrainingLayoutsModel(
            "Video mashg‘ulotlar",
            "3-5 yosh",
            R.color.blue_50,
            R.drawable.ic_person_3,
            R.drawable.icon_sound_3,
            R.color.grey,
            R.drawable.ic_next_button_greybg
        ),
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