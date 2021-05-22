package com.teachingthedeafanddumb.data.model

import java.io.Serializable

data class QuizModel(
    val id: Int?=0,
    val question: String?="",
    val optionOne: String?="",
    val optionTwo: String?="",
    val optionThree: String?="",
    val correctOption: Int?=0

): Serializable

