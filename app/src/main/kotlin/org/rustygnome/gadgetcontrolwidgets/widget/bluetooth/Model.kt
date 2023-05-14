package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rustygnome.gadgetcontrolwidgets.R
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
            ensureRequiredPermissions(context, TAG, REQUIRED_PERMISSIONS)
            Companion._instance = Model(
                context.getSystemService(Application.BLUETOOTH_SERVICE) as BluetoothManager
            )
        }
    }

    public fun isBluetoothEnabled(): LiveData<Boolean> = bluetoothAdapterEnabled

    public fun setBluetoothAdapterEnabled(enabled: Boolean) {
        Timber.v("> setBluetoothAdapterEnabled()")
        bluetoothAdapterEnabled.value = enabled
    }

    internal fun initializeGadgetViews(context: Context, widget: RemoteViews) {
        Timber.v("> initializeGadgetViews()")
        getRegisteredGadgets(context).forEachIndexed { index, item ->
            RemoteViews(
                context.packageName,
                R.layout.bluetooth_gadget_verbose,
            ).apply {
                val intent = Intent(context, Widget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

//                setImageViewResource(R.id.bluetoothWidget_verbose_gadgetIcon, item.icon)
//                setTextViewText(R.id.bluetoothWidget_verbose_gadgetName, item.name)

                PendingIntent.getBroadcast(
                    context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                ).also {
                    setOnClickPendingIntent(R.id.bluetoothWidget_verbose_gadgetIcon, it)
                    setOnClickPendingIntent(R.id.bluetoothWidget_verbose_gadgetName, it)
                }
            }.also {
                widget.addView(index, it)
            }
        }

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
