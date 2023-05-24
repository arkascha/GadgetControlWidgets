package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RemoteViews
import androidx.databinding.ViewDataBinding
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothWidgetHorizontalCompactBinding
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothWidgetVerticalCompactBinding
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothWidgetVerticalVerboseBinding
import timber.log.Timber

internal abstract class Widget(
    context: Context,
    layout: Int,
    protected val binding: ViewDataBinding,
): RemoteViews(
    context.packageName,
    layout,
) {

    init {
        Timber.v("> init()")
        initializeGadgetViews(context)
    }

//    fun onPermissionsGranted(context: Context) {
//        Timber.v("> onPermissionGranted()")
//        initializeGadgetViews(context)
//    }

    protected abstract fun gadgetToView(context: Context, gadget: Gadget): RemoteViews
    protected abstract fun getWidgetContainerView(): LinearLayout

    private fun initializeGadgetViews(context: Context) {
        Timber.v("> initializeGadgetViews()")
        val setOfCheckedGadgetNames = Model.instance.readSetOfCheckedGadgetNames()
        getWidgetContainerView().apply {
            removeAllViews(getWidgetContainerView().id)
            Model.instance.getListOfGadgets(context).filter {
                setOfCheckedGadgetNames.contains(it.name(context))
            }.forEachIndexed { index, gadget ->
                run {
                    Timber.v("Adding gadget number $index.")
                    addGadget(this, gadget, context)
                }
            }
        }
    }

    private fun RemoteViews.addGadget(layout: LinearLayout, gadget: Gadget, context: Context) =
        addView(layout.id, gadget.toRemoteViews(context))

    private fun Gadget.toRemoteViews(context: Context): RemoteViews =
        gadgetToView(context, this)
}

internal class HorizontalCompactWidget(context: Context): Widget(
    context,
    R.layout.bluetooth_widget_horizontal_compact,
    BluetoothWidgetHorizontalCompactBinding.inflate(LayoutInflater.from(context)),
) {
    override fun getWidgetContainerView(): LinearLayout =
        (binding as BluetoothWidgetHorizontalCompactBinding).bluetoothWidgetGadgetListContainer
    override fun gadgetToView(context: Context, gadget: Gadget): RemoteViews =
        gadget.toCompactRemoteViews(context)
}
internal class VerticalCompactWidget(context: Context): Widget(
    context,
    R.layout.bluetooth_widget_vertical_compact,
    BluetoothWidgetVerticalCompactBinding.inflate(LayoutInflater.from(context)),
) {
    override fun getWidgetContainerView(): LinearLayout =
        (binding as BluetoothWidgetVerticalCompactBinding).bluetoothWidgetGadgetListContainer
    override fun gadgetToView(context: Context, gadget: Gadget): RemoteViews =
        gadget.toCompactRemoteViews(context)
}
internal class VerticalVerboseWidget(context: Context): Widget(
    context,
    R.layout.bluetooth_widget_vertical_verbose,
    BluetoothWidgetVerticalVerboseBinding.inflate(LayoutInflater.from(context)),
) {
    override fun getWidgetContainerView(): LinearLayout =
        (binding as BluetoothWidgetVerticalVerboseBinding).bluetoothWidgetGadgetListContainer
    override fun gadgetToView(context: Context, gadget: Gadget): RemoteViews =
        gadget.toVerboseRemoteViews(context)
}
