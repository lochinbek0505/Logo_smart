package com.azamovhudstc.logosmart.data.model

data class GameNestedModel(
    val title:String,
    var list:ArrayList<GameModel>
):java.io.Serializable