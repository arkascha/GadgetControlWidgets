package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rustygnome.gadgetcontrolwidgets.ensureRequiredPermissions
import timber.log.Timber

class Model private constructor (
    private val bluetoothManager: BluetoothManager
) {

    init {
        Timber.v("> init()")
    }

    companion object {
        private lateinit var _instance: Model
        val instance: Model get() = _instance

        fun setup(context: Context) {
            ensureRequiredPermissions(context, REQUIRED_PERMISSIONS)
            _instance = Model(
                context.getSystemService(Application.BLUETOOTH_SERVICE) as BluetoothManager
            )
        }
    }

    internal fun getListOfGadgets(): List<Gadget> =
        mutableListOf<Gadget>().apply {
            Timber.v("> getRegisteredGadgets()")
            try {
                // this devices bluetooth adapter itself
                add(0, Adapter())
                // all gadgets currently bonded to this device's bluetooth adapter
                bluetoothManager.adapter.bondedDevices.forEach {
                    add(Device(it))
                }
                Timber.v("Found $size gadgets.")
            } catch (exception: SecurityException) {
                Timber.e("Missing permission to access Bluetooth Adapter!")
            }
        }
}
