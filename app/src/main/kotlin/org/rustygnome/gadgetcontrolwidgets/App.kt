package org.rustygnome.gadgetcontrolwidgets

import android.app.Application
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Model
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initLogging()
        Timber.d("> onCreate()")
        Model.setup(applicationContext)
    }

    companion object {
        fun initLogging() {
            if (Timber.forest().isEmpty()) {
                if (BuildConfig.DEBUG)
                    Timber.plant(Timber.DebugTree())
            }
        }
    }
}