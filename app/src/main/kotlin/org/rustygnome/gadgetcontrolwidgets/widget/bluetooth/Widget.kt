package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RemoteViews
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetWidgetHorizontalListBinding
import timber.log.Timber

internal abstract class Widget(
    context: Context
): RemoteViews(
    context.packageName,
    R.layout.bluetooth_gadget_widget_horizontal_list,
) {
    private val binding = BluetoothGadgetWidgetHorizontalListBinding.inflate(LayoutInflater.from(context))

    init {
        Timber.v("> init()")
        initializeGadgetViews(context)
        Timber.v("Showing ${binding.bluetoothWidgetItemListContainer.childCount} gadgets in widget.")
        Timber.v("Gadget 1 uses ${binding.bluetoothWidgetItemListContainer.children.elementAt(0).javaClass}")
    }

//    fun onPermissionsGranted(context: Context) {
//        Timber.v("> onPermissionGranted()")
//        initializeGadgetViews(context)
//    }

    protected abstract fun gadgetToView(context: Context, gadget: Gadget): View

    private fun initializeGadgetViews(context: Context) {
        Timber.v("> initializeGadgetViews()")
        Model.instance.getBondedGadgets(context).forEachIndexed { index, gadget ->
            Timber.v("Inflating gadget view for id $index")
            binding.bluetoothWidgetItemListContainer.addView(gadget.toView(context))
        }
    }

    private fun Gadget.toView(context: Context): View =
        gadgetToView(context, this)
}

internal class HorizontalCompactWidget(context: Context): Widget(context) {
    override fun gadgetToView(context: Context, gadget: Gadget): View =
        gadget.toCompactView(context)
}
internal class VerticalCompactWidget(context: Context): Widget(context) {
    override fun gadgetToView(context: Context, gadget: Gadget): View =
        gadget.toCompactView(context)
}
internal class VerticalVerboseWidget(context: Context): Widget(context) {
    override fun gadgetToView(context: Context, gadget: Gadget): View =
        gadget.toVerboseView(context)
}
