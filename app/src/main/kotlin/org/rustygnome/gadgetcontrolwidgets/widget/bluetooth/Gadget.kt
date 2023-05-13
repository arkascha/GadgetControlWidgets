package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import org.rustygnome.gadgetcontrolwidgets.R
import timber.log.Timber

open class Gadget (
    val type: Int,
    val clazz: BluetoothClass?,
    val icon: Int,
    val name: String,
    val state: Int,
) {
    init {
        Timber.d("> init()");
    }

    fun toCompactView(context: Context): View =
        LayoutInflater
            .from(context)
            .inflate(R.layout.bluetooth_gadget_compact, null, false).apply {

            }

    fun toVerboseView(context: Context): View =
        LayoutInflater
            .from(context)
            .inflate(R.layout.bluetooth_gadget_verbose, null, false)

}

class Adapter(): Gadget(
    type = BluetoothDevice.DEVICE_TYPE_UNKNOWN,
    clazz = null,
    icon = R.drawable.ic_bluetooth,
    name = "Adapter",
    state = 0,
)

class Dummy(): Gadget (
    type = 0,
    clazz = null,
    icon = R.drawable.ic_unknown,
    name = "unknown",
    state = 0,
)