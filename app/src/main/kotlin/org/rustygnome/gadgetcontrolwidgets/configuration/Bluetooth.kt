package org.rustygnome.gadgetcontrolwidgets.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBluetoothBinding
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Model
import timber.log.Timber

class Bluetooth: Fragment() {

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
    }


    private fun addGadgetsToList() {
        Timber.v("> addGadgetsToList()")
        Model.instance.getRegisteredGadgets(requireContext()).forEachIndexed { _, item ->
            binding.configurationBluetoothList.addView(item.toVerboseView(requireContext()))
        }
    }

    override fun onDestroyView() {
        Timber.v("> onDestroyView()")
        super.onDestroyView()
    }
}