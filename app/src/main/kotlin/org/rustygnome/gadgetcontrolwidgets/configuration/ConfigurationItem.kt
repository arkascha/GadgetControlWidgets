package org.rustygnome.gadgetcontrolwidgets.configuration

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import org.rustygnome.gadgetcontrolwidgets.databinding.BluetoothGadgetItemBinding
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Gadget
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Model

class ConfigurationItem(
    private val device: Gadget,
    private val onCheckedListener: (View) -> Unit,
) {
    fun toConfigurationView(context: Context): View =
        BluetoothGadgetItemBinding.inflate(LayoutInflater.from(context)).apply {

            bluetoothWidgetConfigurationGadget.apply {
                bluetoothWidgetVerboseGadgetIcon.setImageResource(device.icon(context))
                bluetoothWidgetVerboseGadgetIcon.contentDescription = device.description(context)
                bluetoothWidgetVerboseGadgetName.text = device.name(context)
                bluetoothWidgetVerboseGadgetDescription.text = device.services(context)
            }
            bluetoothWidgetConfigurationCheckBox.apply {
                tag = device.tag
                isChecked = isItemChecked()
                setOnClickListener(onCheckedListener)
            }
        }.root

    private fun isItemChecked(): Boolean =
        Model.instance.readSetOfCheckedGadgetNames().contains(device.tag)
}