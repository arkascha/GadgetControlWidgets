package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.rustygnome.gadgetcontrolwidgets.App
import timber.log.Timber

abstract class Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        App.initLogging()
        Timber.v("> onReceive()")

        var updateRequired = true

        intent.action?.let { action ->
            Timber.e("Received action '$action'.")
            when (action) {
                "android.bluetooth.adapter.action.STATE_CHANGED" -> {
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR).also {
                        when (it) {
                            BluetoothAdapter.STATE_OFF -> {
                                Model.instance.setBluetoothAdapterEnabled(false)
                            }
                            BluetoothAdapter.STATE_ON -> {
                                Model.instance.setBluetoothAdapterEnabled(true)
                            }
                        }
                    }
                }
                INTENT_ACTION_UPDATE_WIDGETS -> {}
                else -> updateRequired = false
            }
        }

        val appWidgetManager = AppWidgetManager.getInstance(context);
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, Provider::class.java)
        )

        for (appWidgetId in appWidgetIds) {
            Widget.updateWidget(context, appWidgetId).also {
                appWidgetManager.updateAppWidget(appWidgetId, it)
            }
        }
    }
}
