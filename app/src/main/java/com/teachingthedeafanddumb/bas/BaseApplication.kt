package com.teachingthedeafanddumb.bas

import android.app.Application
import com.yariksoffice.lingver.Lingver
import com.yariksoffice.lingver.store.PreferenceLocaleStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*


@HiltAndroidApp
class BaseApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())


}
}