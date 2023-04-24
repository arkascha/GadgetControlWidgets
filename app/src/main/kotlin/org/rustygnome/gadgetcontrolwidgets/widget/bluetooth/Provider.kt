package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.util.Log
import org.rustygnome.gadgetcontrolwidgets.ensureRequiredPermissions
import org.rustygnome.gadgetcontrolwidgets.deleteTitlePref

class Provider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            Log.i(TAG, "Updating widget with id $appWidgetId ...")
            Widget.updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        ensureRequiredPermissions(context, TAG, REQUIRED_PERMISSIONS)
        model = model ?: Model(
            context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        )
    }

    override fun onDisabled(context: Context) {
        model = null
    }

    companion object {
        var model: Model? = null
    }
}

