package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import org.rustygnome.gadgetcontrolwidgets.ensureRequiredPermissions
import timber.log.Timber


const val MODE_BLACKLIST = false
const val CHECKED_GADGETS = "checked gadgets"

class Model private constructor (
    private val bluetoothManager: BluetoothManager,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    init {
        Timber.v("> init()")
    }

    companion object {
        private val sharedPreferencesName = javaClass.packageName
        private lateinit var _instance: Model
        val instance: Model get() = _instance

        fun setup(context: Context) {
            ensureRequiredPermissions(context, REQUIRED_PERMISSIONS)
            _instance = Model(
                bluetoothManager = context.getSystemService(Application.BLUETOOTH_SERVICE) as BluetoothManager,
                sharedPreferences = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE),
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

    internal fun writeSetOfCheckedGadgetNames(gadgets: Set<String>) {
        sharedPreferences.edit()
            .putStringSet(CHECKED_GADGETS, gadgets)
            .apply()
    }

    internal fun readSetOfCheckedGadgetNames(): Set<String> =
        sharedPreferences.getStringSet(CHECKED_GADGETS, emptySet()) ?: emptySet()
}
