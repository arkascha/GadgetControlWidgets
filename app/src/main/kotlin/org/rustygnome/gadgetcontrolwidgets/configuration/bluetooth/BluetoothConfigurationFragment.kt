package org.rustygnome.gadgetcontrolwidgets.configuration.bluetooth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.core.view.forEach
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.configuration.ConfigurationFragment
import org.rustygnome.gadgetcontrolwidgets.configuration.ConfigurationItem
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBluetoothBinding
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.BluetoothModel
import timber.log.Timber

class BluetoothConfigurationFragment: ConfigurationFragment() {

    private lateinit var binding: ConfigurationBluetoothBinding

    init {
        Timber.d("> init()")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Timber.v("> onCreateView()")
        ConfigurationBluetoothBinding.inflate(inflater,container,false).also {
            binding = it
        }
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.v("> onViewCreated()")
        addGadgetsToList()
        enableCheckBox()
    }


    private fun addGadgetsToList() {
        Timber.v("> addGadgetsToList()")
        binding.configurationBluetoothList.run {
            // first remove _all_ gadgets
            this.removeAllViewsInLayout()
            // add available gadgets
            BluetoothModel.instance.getListOfGadgets().forEach { gadget ->
                addView(
                    ConfigurationItem(gadget) {
                        storeSetOfCheckedGadgets()
                    }.toConfigurationView(requireContext())
                )
            }
        }
    }

    private fun enableCheckBox() {
        Timber.v("> enableCheckBox()")
        binding.configurationBluetoothCheckBox.apply {
            setOnClickListener {
                isChecked = false
                binding.configurationBluetoothList.forEach {
                    it.findViewById<CheckBox>(R.id.bluetoothWidget_configuration_checkBox).isChecked = true
                }
            }
        }
    }

    private fun storeSetOfCheckedGadgets() {
        Timber.v("> storeSetOfCheckedGadgets()")
        mutableSetOf<String>().apply {
            binding.configurationBluetoothList.children.forEach {
                it.findViewById<CheckBox>(R.id.bluetoothWidget_configuration_checkBox).run {
                    if (tag != null && isChecked) {
                        this@apply.add(tag as String)
                    }
                }
            }
        }.also {
            BluetoothModel.instance.writeSetOfCheckedGadgetNames(it)
        }
    }

    override fun onPause() {
        Timber.v("> onPause()")
        storeSetOfCheckedGadgets()
        super.onPause()
    }

    override fun onDestroyView() {
        Timber.v("> onDestroyView()")
        super.onDestroyView()
    }

}