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
        _binding = ConfigBinding.inflate(inflater)
        with(requireContext()) {
            App.initLogging()
            Model.setup(this)
        }
        addGadgetsToList()
        return binding.root
    }

    private fun addGadgetsToList() {
        Timber.v("> addGadgetsToList()")
        Model.instance.getRegisteredGadgets(requireContext()).forEachIndexed { index, item ->
            binding.configBluetoothList.addView(item.toVerboseView(this.requireContext()))
        }
    }

    override fun onDestroyView() {
        Timber.v("> onDestroyView()")
        super.onDestroyView()
        _binding = null
    }
}
