package com.teachingthedeafanddumb.bas

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.*


@HiltAndroidApp
class BaseApplication :Application(){

    override fun onCreate() {
        super.onCreate()

}
}