package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import timber.log.Timber

abstract class Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val appWidgetManager = AppWidgetManager.getInstance(context);
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                context,
                Provider::class.java
            )
        )

        intent.action?.let { action ->
            if (action.matches("android.bluetooth.adapter.action.STATE_CHANGED".toRegex())) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                Timber.e("Received action '$action'.")
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        Model.instance.setBluetoothAdapterEnabled(false)
                    }
                    BluetoothAdapter.STATE_ON -> {
                        Model.instance.setBluetoothAdapterEnabled(true)
                    }
                }
            }
        }

        for (appWidgetId in appWidgetIds) {
            Widget.updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}
