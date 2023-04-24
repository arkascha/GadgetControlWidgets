package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import org.rustygnome.gadgetcontrolwidgets.R

internal class Widget(
    context: Context
): RemoteViews(
    context.packageName,
    R.layout.bluetooth_gadget_widget_vertical,
) {

    init {
        Provider.model?.getRegisteredGadgets()?.forEachIndexed { index, item ->
            RemoteViews(
                context.packageName,
                R.layout.bluetooth_gadget_item_full,
            ).apply {
                val intent = Intent(context, Widget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

                setImageViewResource(R.id.bluetoothWidget_itemFull_itemIcon, item.icon)
                setTextViewText(R.id.bluetoothWidget_itemFull_itemName, item.name)

                PendingIntent.getBroadcast(
                    context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                ).also {
                    setOnClickPendingIntent(R.id.bluetoothWidget_itemFull_itemIcon, it)
                    setOnClickPendingIntent(R.id.bluetoothWidget_itemFull_itemName, it)
                }
            }.also {
                addView(index, it)
            }
        }

    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            with(Widget(context)) {
                appWidgetManager.updateAppWidget(appWidgetId, this)
            }
        }
    }
}
