package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import org.rustygnome.gadgetcontrolwidgets.R
import timber.log.Timber

internal class Widget(
    context: Context
): RemoteViews(
    context.packageName,
    R.layout.bluetooth_gadget_widget_vertical,
) {

    fun onPermissionsGranted(context: Context) {
        Timber.v("> onPermissionGranted()")
        Model.instance.initializeGadgetViews(context, this)
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            with(Widget(context)) {
                Timber.d("Updating widget with id $appWidgetId ...")
                appWidgetManager.updateAppWidget(appWidgetId, this)
            }
        }
    }
}
