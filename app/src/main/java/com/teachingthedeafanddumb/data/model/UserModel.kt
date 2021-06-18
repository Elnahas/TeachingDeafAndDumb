package com.teachingthedeafanddumb.data.model

import com.teachingthedeafanddumb.data.model.Role
import java.io.Serializable


data class UserModel(
    var id: String? = null,
    var fullName: String? = null,
    var phone: String? = null,
    var photoUrl: String? =null ,
    var token: String? = null,
    var createAt: Any? = null,
    var role: Role? = null
): Serializable