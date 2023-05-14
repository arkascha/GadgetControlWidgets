package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetCompactBinding
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetVerboseBinding
import timber.log.Timber

open class Gadget (
    val alias: String? = null,
    val clazz: BluetoothClass? = null,
    val icon: Int,
    val name: String,
    val state: Int = 0,
    val type: Int? = BluetoothDevice.DEVICE_TYPE_UNKNOWN,
) {

    init {
        Timber.d("> init($name)")
    }

    fun toCompactView(context: Context): View =
        BluetoothGadgetCompactBinding.inflate(LayoutInflater.from(context)).apply {
            bluetoothWidgetCompactGadgetIcon.apply {
                setImageResource(icon)
                contentDescription = name
            }
        }.root

    fun toVerboseView(context: Context): View =
        BluetoothGadgetVerboseBinding.inflate(LayoutInflater.from(context)).apply {
            bluetoothWidgetVerboseGadgetIcon.apply {
                setImageResource(icon)
                contentDescription = name
            }
            bluetoothWidgetVerboseGadgetName.setText(phraseDisplayName())
            bluetoothWidgetVerboseGadgetDescription.setText(phraseDisplayDescription(context))
        }.root

    private fun phraseDisplayName(): String =
        alias?.run {
            if (alias.isNotEmpty())
                if (alias.equals(name)) alias else "$alias ($name)"
            else name
        } ?: name

    private fun phraseDisplayDescription(context: Context): String =
        mutableListOf<String>().apply {
            Service.map.forEach { (service, name) ->
                if (clazz?.hasService(service) == true) {
                    add(context.getString(name))
                }
            }
        }.run {
            return if (isNotEmpty()) joinToString(", ") else context.getString(R.string.bluetooth_service_unknown)
        }
}

class Adapter(name: String): Gadget(
    icon = R.drawable.ic_bluetooth,
    name = name,
)

class Dummy(name: String): Gadget (
    icon = R.drawable.ic_unknown,
    name = name,
)