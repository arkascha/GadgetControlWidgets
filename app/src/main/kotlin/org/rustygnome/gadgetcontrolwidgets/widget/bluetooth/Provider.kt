package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import org.rustygnome.gadgetcontrolwidgets.App
import timber.log.Timber

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
            Widget.updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }


//    override fun onUpdate(
//        context: Context,
//        appWidgetManager: AppWidgetManager,
//        appWidgetIds: IntArray?
//    ) {
//        Timber.v("> onUpdate()")
//        val remoteViews = RemoteViews(context.packageName, R.layout.widgetlayout)
//        val configIntent = Intent(context, Activity::class.java)
//        val configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0)
//        remoteViews.setOnClickPendingIntent(R.id.widget, configPendingIntent)
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
//    }


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
