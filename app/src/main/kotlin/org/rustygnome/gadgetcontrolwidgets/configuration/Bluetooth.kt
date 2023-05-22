package org.rustygnome.gadgetcontrolwidgets.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBluetoothBinding
import org.rustygnome.gadgetcontrolwidgets.ensureRequiredPermissions
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Model
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.REQUIRED_PERMISSIONS
import timber.log.Timber

class Bluetooth: Fragment() {

    private lateinit var binding: ConfigurationBluetoothBinding

    init {
        Timber.d("> init()")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Timber.v("> onCreateView()")
        ensureRequiredPermissions(requireContext(), REQUIRED_PERMISSIONS)
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
        binding.configurationBluetoothList.also {
            Model.instance.getListOfGadgets().forEach { gadget ->
                it.addView(
                    Item(gadget) {
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
            Model.instance.writeSetOfCheckedGadgetNames(it)
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