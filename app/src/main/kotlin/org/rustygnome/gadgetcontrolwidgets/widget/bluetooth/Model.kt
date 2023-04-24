package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rustygnome.gadgetcontrolwidgets.R

class Model (
    private val bluetoothManager: BluetoothManager,
) {

    private val bluetoothAdapterEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

    public fun isBluetoothEnabled(): LiveData<Boolean> = bluetoothAdapterEnabled

    public fun setBluetoothAdapterEnabled(enabled: Boolean) {
        bluetoothAdapterEnabled.value = enabled
    }

    internal fun getRegisteredGadgets(): List<Item> = mutableListOf<Item>().apply {
        try {
            add(createAdapterItem())
            bluetoothManager.adapter.bondedDevices.forEach {
                add(createBondedItem(it))
            }
        } catch (exception: SecurityException) {
            Log.e(TAG, "Missing permission to access Bluetooth Adapter!")
        }
    }

    @SuppressLint("MissingPermission")
    private fun createAdapterItem(): Item = Item(
        clazz = null,
        icon = R.drawable.ic_bluetooth,
        name = bluetoothManager.adapter.name,
        state = bluetoothManager.adapter.state,
        type = BluetoothDevice.DEVICE_TYPE_UNKNOWN
    )

    @SuppressLint("MissingPermission")
    private fun createBondedItem(device: BluetoothDevice): Item = Item(
        clazz = device.bluetoothClass,
        icon = R.drawable.ic_bluetooth,
        name = device.name,
        state = device.bondState,
        type = device.type,
    )
}