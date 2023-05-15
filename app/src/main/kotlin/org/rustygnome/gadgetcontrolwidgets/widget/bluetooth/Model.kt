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
    private val bluetoothAdapterEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

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

    public fun isBluetoothEnabled(): LiveData<Boolean> = bluetoothAdapterEnabled

    public fun setBluetoothAdapterEnabled(enabled: Boolean) {
        Timber.v("> setBluetoothAdapterEnabled()")
        bluetoothAdapterEnabled.value = enabled
    }

    internal fun getRegisteredGadgets(context: Context): List<Gadget> =
        mutableListOf<Gadget>().apply {
            Timber.v("> getRegisteredGadgets()")
            try {
                // this devices bluetooth adapter itself
                add(Adapter())
                // all gadgets currently boded to this device's bluetooth adapter
                bluetoothManager.adapter.bondedDevices.forEach {
                    add(Device(it))
                }
                // a (passive) dummy gadget symbolizing gadgets bonded in future
                add(Dummy())
            } catch (exception: SecurityException) {
                Timber.e("Missing permission to access Bluetooth Adapter!")
            }
        }
}
