package com.teachingthedeafanddumb.data

import java.io.Serializable

data class LessonModel(
    var id:String?=null,
    var lessonNumber:String?=null,
    var lessonTitle:String?=null,
    var videoUrl:String?=null,
    var quiz:MutableMap<String , QuizModel>?= mutableMapOf()
) : Serializable