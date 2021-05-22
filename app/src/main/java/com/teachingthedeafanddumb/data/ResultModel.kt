package com.teachingthedeafanddumb.data

import java.io.Serializable

data class ResultModel(
    var id:String?=null,
    var createAt:String?=null,
    var uid:String?=null,
    var resultCorrect:Int=0,
    var resultWrong:Int=0,
    var resultMissed:Int=0,
    var lessonId:String?=null,
    var uid_lessonId:String?=null
): Serializable