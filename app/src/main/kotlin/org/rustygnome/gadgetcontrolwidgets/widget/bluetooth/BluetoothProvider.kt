package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import org.rustygnome.gadgetcontrolwidgets.App
import timber.log.Timber

const val INTENT_ACTION_UPDATE_WIDGETS = "org.rustygnome.gadgetcontrolwidgets.INTENT_ACTION_UPDATE_WIDGETS"
const val INTENT_EXTRA_CATEGORY = "org.rustygnome.gadgetcontrolwidgets.INTENT_EXTRA_CATEGORY"
const val INTENT_EXTRA_HANDLE = "org.rustygnome.gadgetcontrolwidgets.INTENT_EXTRA_HANDLE"

class ProviderCompactHorizontal: Provider() {
    override fun createWidget(context: Context): Widget =
        HorizontalCompactWidget(context)
}

class ProviderCompactVertical: Provider() {
    override fun createWidget(context: Context): Widget =
        VerticalCompactWidget(context)
}

class ProviderVerboseVertical: Provider() {
    override fun createWidget(context: Context): Widget =
        VerticalVerboseWidget(context)
}

abstract class Provider : AppWidgetProvider() {

    init {
        Timber.d("> init()")
    }

    abstract internal fun createWidget(context: Context): Widget

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
            createWidget(context).also {
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
}
