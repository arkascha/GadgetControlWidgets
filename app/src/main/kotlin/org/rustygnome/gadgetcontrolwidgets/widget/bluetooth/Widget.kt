package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.LayoutInflater
import android.widget.RemoteViews
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetWidgetHorizontalBinding
import timber.log.Timber


internal class Widget(
    context: Context
): RemoteViews(
    context.packageName,
    R.layout.bluetooth_gadget_widget_vertical,
) {
    private val binding = BluetoothGadgetWidgetHorizontalBinding.inflate(LayoutInflater.from(context))

    fun onPermissionsGranted(context: Context) {
        Timber.v("> onPermissionGranted()")
        initializeGadgetViews(context)
    }

    private fun initializeGadgetViews(context: Context) {
        Timber.v("> initializeGadgetViews()")
        Model.instance.getRegisteredGadgets(context).forEachIndexed { index, item ->
            binding.bluetoothWidgetItemListContainer.addView(item.toVerboseView(context))
        }
    }

    companion object {
        internal fun updateWidget(context: Context, appWidgetId: Int): Widget {
            Timber.d("Updating widget with id $appWidgetId ...")
            Widget(context).also {
                return it
            }
        }
    }
}
