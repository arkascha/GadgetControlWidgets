package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.bluetooth.BluetoothClass
import androidx.annotation.DrawableRes

data class Item(
    val clazz: BluetoothClass?,
    @DrawableRes val icon: Int,
    val name: String,
    val state: Int,
    val type: Int,
)
