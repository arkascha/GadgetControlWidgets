package org.rustygnome.gadgetcontrolwidgets.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.App
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigBinding
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Model
import timber.log.Timber

class Config: Fragment() {
    private var _binding: ConfigBinding? = null
    private val binding get() = _binding!!

    init {
        Timber.d("> init()")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("> onCreateView()")
        _binding = ConfigBinding.inflate(inflater, container, false)
        with(requireContext()) {
            App.initLogging()
            Model.setup(this)
        }
        addGadgetsToList(inflater, container)
        return binding.root
    }

    private fun addGadgetsToList(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        Timber.v("> addGadgetsToList()")
        Model.instance.getRegisteredGadgets().forEachIndexed { index, item ->
            (inflater.inflate(R.layout.bluetooth_gadget_item, container) as FrameLayout)
//                .also {
//                    it.addView(item.toVerboseView(this.requireContext()))
//                }.also {
//                    binding.configBluetoothList.addView(it)
//                }
        }
    }

    override fun onDestroyView() {
        Timber.v("> onDestroyView()")
        super.onDestroyView()
        _binding = null
    }
}
