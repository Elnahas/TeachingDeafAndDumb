package com.teachingthedeafanddumb.data

data class QuizModel(
    val id: Int?=0,
    val question: String?="",
    val optionOne: String?="",
    val optionTwo: String?="",
    val optionThree: String?="",
    val correctOption: Int?=0

)

