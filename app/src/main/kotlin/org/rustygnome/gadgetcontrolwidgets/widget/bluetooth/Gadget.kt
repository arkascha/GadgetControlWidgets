package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RemoteViews
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetVerboseBinding
import timber.log.Timber

abstract class Gadget {
    fun toCompactRemoteViews(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.bluetooth_gadget_compact).apply {
            setImageViewResource(R.id.bluetoothWidget_compact_gadgetIcon, choseGadgetIcon(context))
            setContentDescription(R.id.bluetoothWidget_compact_gadgetIcon, phraseGadgetDescription(context))
        }

    fun toVerboseRemoteViews(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.bluetooth_gadget_compact).apply {
            setImageViewResource(R.id.bluetoothWidget_verbose_gadgetIcon, choseGadgetIcon(context))
            setContentDescription(R.id.bluetoothWidget_verbose_gadgetIcon, phraseGadgetDescription(context))

            setTextViewText(R.id.bluetoothWidget_verbose_gadgetName, phraseGadgetName(context))
            setTextViewText(R.id.bluetoothWidget_verbose_gadgetDescription, phraseGadgetServices(context))
        }

    fun toVerboseView(context: Context): View =
        BluetoothGadgetVerboseBinding.inflate(LayoutInflater.from(context)).apply {
            bluetoothWidgetVerboseGadgetIcon.apply {
                setImageResource(choseGadgetIcon(context))
                contentDescription = phraseGadgetDescription(context)
            }
            bluetoothWidgetVerboseGadgetName.setText(phraseGadgetName(context))
            bluetoothWidgetVerboseGadgetDescription.setText(phraseGadgetServices(context))
        }.root

    protected abstract fun choseGadgetIcon(context: Context): Int

    protected abstract fun phraseGadgetName(context: Context): String
    protected abstract fun phraseGadgetDescription(context: Context): String
    protected abstract fun phraseGadgetServices(context: Context): String
}

final class Adapter: Gadget() {
    override fun phraseGadgetName(context: Context): String =
        context.getString(R.string.gadget_name_bluetooth_adapter)
    override fun phraseGadgetDescription(context: Context): String =
        context.getString(R.string.gadget_name_bluetooth_adapter)
    override fun phraseGadgetServices(context: Context): String =
        context.getString(R.string.bluetooth_service_adapter)
    override fun choseGadgetIcon(context: Context): Int =
        R.drawable.ic_bluetooth
}

final class Dummy: Gadget() {
    override fun phraseGadgetName(context: Context): String =
        context.getString(R.string.gadget_name_dummy)

    override fun phraseGadgetDescription(context: Context): String =
        context.getString(R.string.gadget_name_dummy)

    override fun phraseGadgetServices(context: Context): String =
        context.getString(R.string.bluetooth_service_unknown)

    override fun choseGadgetIcon(context: Context): Int =
        R.drawable.ic_unknown
}

@SuppressLint("MissingPermission")
open class Device(
    val device: BluetoothDevice
) : Gadget() {

    init {
        Timber.d("> init(${device.name})")
    }

    override fun phraseGadgetName(context: Context): String =
        device.alias?.run {
            if (device.alias!!.isNotEmpty())
                if (device.alias.equals(device.name)) device.alias else "${device.alias} (${device.name})"
            else device.name
        } ?: device.name

    override fun phraseGadgetDescription(context: Context): String = device.alias ?: device.name

    override fun phraseGadgetServices(context: Context): String =
        mutableListOf<String>().apply {
            Service.map.forEach { (service, name) ->
                if (device.bluetoothClass.hasService(service)) {
                    add(context.getString(name))
                }
            }
        }.run {
            return if (isNotEmpty()) joinToString(", ") else context.getString(Service.unknown)
        }

    override fun choseGadgetIcon(context: Context): Int =
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
}
