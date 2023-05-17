package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.rustygnome.gadgetcontrolwidgets.App
import timber.log.Timber

const val INTENT_ACTION_UPDATE_WIDGETS = "org.rustygnome.gadgetcontrolwidgets.INTENT_ACTION_UPDATE_WIDGETS"
const val INTENT_EXTRA_CATEGORY = "org.rustygnome.gadgetcontrolwidgets.INTENT_EXTRA_CATEGORY"
const val INTENT_EXTRA_HANDLE = "org.rustygnome.gadgetcontrolwidgets.INTENT_EXTRA_HANDLE"

class ProviderCompactHorizontal: Provider() {
}

class ProviderCompactVertical: Provider() {
}

class ProviderVerboseVertical: Provider() {
}

abstract class Provider : AppWidgetProvider() {

    init {
        Timber.d("> init()")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        App.initLogging()
        Timber.v("> onReceive()")
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        App.initLogging()
        Timber.v("> onUpdate()")

        Model.setup(context)

        for (appWidgetId in appWidgetIds) {
            Timber.i("Updating widget with id $appWidgetId ...")
            Widget.updateWidget(context, appWidgetId).also {
                appWidgetManager.updateAppWidget(appWidgetId, it)
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        Timber.v("> onDelete()")
    }

    override fun onEnabled(context: Context) {
        App.initLogging()
        Timber.v("> onEnabled()")
    }

    override fun onDisabled(context: Context) {
        Timber.v("> onDisabled()")
    }

    private fun getInstalledWidgetIds(context: Context): IntArray =
        // ToDo: is Provider::clas..java really the right one here?
        AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, Provider::class.java))
}
