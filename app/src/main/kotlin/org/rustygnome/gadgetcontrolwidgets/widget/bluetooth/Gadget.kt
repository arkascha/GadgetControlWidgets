package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetCompactBinding
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetVerboseBinding
import timber.log.Timber

open class Gadget (
    val alias: String? = null,
    val clazz: BluetoothClass? = null,
    val icon: Int? = null,
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
                setImageResource(choseGadgetIcon(context))
                contentDescription = alias ?: name
            }
        }.root

    fun toVerboseView(context: Context): View =
        BluetoothGadgetVerboseBinding.inflate(LayoutInflater.from(context)).apply {
            bluetoothWidgetVerboseGadgetIcon.apply {
                setImageResource(choseGadgetIcon(context))
                contentDescription = alias ?: name
            }
            bluetoothWidgetVerboseGadgetName.setText(phraseGadgetName())
            bluetoothWidgetVerboseGadgetDescription.setText(phraseGadgetServices(context))
        }.root

    private fun phraseGadgetName(): String =
        alias?.run {
            if (alias.isNotEmpty())
                if (alias.equals(name)) alias else "$alias ($name)"
            else name
        } ?: name

    private fun phraseGadgetServices(context: Context): String =
        mutableListOf<String>().apply {
            Service.map.forEach { (service, name) ->
                if (clazz?.hasService(service) == true) {
                    add(context.getString(name))
                }
            }
        }.run {
            return if (isNotEmpty()) joinToString(", ") else context.getString(Service.unknown)
        }

    @DrawableRes
    private fun choseGadgetIcon(context: Context): Int =
        icon ?: R.drawable.ic_bluetooth
}

class Adapter(name: String): Gadget(
    icon = R.drawable.ic_bluetooth,
    name = name,
)

class Dummy(name: String): Gadget (
    icon = R.drawable.ic_unknown,
    name = name,
)