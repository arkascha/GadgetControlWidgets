package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.BOND_BONDED
import android.bluetooth.BluetoothDevice.BOND_BONDING
import android.content.Context
import android.widget.RemoteViews
import org.rustygnome.gadgetcontrolwidgets.R
import timber.log.Timber

abstract class Gadget(
    val tag: String
) {
    abstract fun name(context: Context): String
    abstract fun description(context: Context): String
    abstract fun services(context: Context): String
    abstract fun icon(context: Context): Int
    abstract fun isEnabled(): Boolean
    abstract fun isBusy(): Boolean

    fun toCompactRemoteViews(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.bluetooth_gadget_compact).apply {
            setImageViewResource(R.id.bluetoothWidget_compact_gadgetIcon, icon(context))
            setContentDescription(R.id.bluetoothWidget_compact_gadgetIcon, description(context))
        }

    fun toVerboseRemoteViews(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.bluetooth_gadget_verbose).apply {
            setImageViewResource(R.id.bluetoothWidget_verbose_gadgetIcon, icon(context))
            setContentDescription(R.id.bluetoothWidget_verbose_gadgetIcon, description(context))
            setTextViewText(R.id.bluetoothWidget_verbose_gadgetName, name(context))
            setTextViewText(R.id.bluetoothWidget_verbose_gadgetDescription, services(context))
        }
}

@SuppressLint("MissingPermission")
class Adapter(
    private val device: BluetoothAdapter,
): Gadget(device.name) {
    override fun name(context: Context): String =
        context.getString(R.string.gadget_name_bluetooth_adapter)
    override fun description(context: Context): String =
        context.getString(R.string.gadget_name_bluetooth_adapter)
    override fun services(context: Context): String =
        context.getString(R.string.bluetooth_service_adapter)
    override fun icon(context: Context): Int =
        R.drawable.ic_bluetooth
    @SuppressLint("MissingPermission")
    override fun isEnabled(): Boolean =
        device.isEnabled
    @SuppressLint("MissingPermission")
    override fun isBusy() =
        device.isDiscovering
}

@SuppressLint("MissingPermission")
class Device(
    private val device: BluetoothDevice,
): Gadget(device.name) {

    init {
        Timber.d("> init(${device.name})")
    }

    override fun name(context: Context): String =
        device.alias?.run {
            if (device.alias!!.isNotEmpty())
                if (device.alias.equals(device.name)) device.alias else "${device.alias} (${device.name})"
            else device.name
        } ?: device.name

    override fun description(context: Context): String = device.alias ?: device.name

    override fun services(context: Context): String =
        mutableListOf<String>().apply {
            BluetoothService.map.forEach { (service, name) ->
                if (device.bluetoothClass.hasService(service)) {
                    add(context.getString(name))
                }
            }
        }.run {
            return if (isNotEmpty()) joinToString(", ") else context.getString(BluetoothService.unknown)
        }

    override fun icon(context: Context): Int =
        if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.COMPUTER))
            R.drawable.ic_device_laptop
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.HEALTH))
            R.drawable.ic_device_health
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.IMAGING))
            R.drawable.ic_device_imaging
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.NETWORKING))
            R.drawable.ic_device_networking
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.WEARABLE))
            R.drawable.ic_device_wearable
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.PHONE))
            R.drawable.ic_device_phone
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.TOY))
            R.drawable.ic_device_toy
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.PERIPHERAL))
            R.drawable.ic_device_peripheral
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.AUDIO_VIDEO)) {
            if (device.bluetoothClass.hasService(BluetoothClass.Service.CAPTURE))
                R.drawable.ic_device_headset
            else
                R.drawable.ic_device_headphones
        }
        else if (device.bluetoothClass.majorDeviceClass.equals(BluetoothClass.Device.Major.MISC))
            R.drawable.ic_device_misc
        else R.drawable.ic_unknown

    override fun isEnabled(): Boolean =
        device.bondState == BOND_BONDED
    override fun isBusy() =
        device.bondState == BOND_BONDING
}
