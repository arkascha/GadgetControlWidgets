package org.rustygnome.gadgetcontrolwidgets.configuration

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.App
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBinding
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Provider
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.ProviderCompactHorizontal
import timber.log.Timber

class HorizontalCompactBluetoothActivity: Activity(ProviderCompactHorizontal::class.java, { Bluetooth() })
class VerticalCompactBluetoothActivity: Activity(ProviderCompactHorizontal::class.java, { Bluetooth() })
class VerticalVerboseBluetoothActivity: Activity(ProviderCompactHorizontal::class.java, { Bluetooth() })

open class Activity(
    private val provider: Class<out Provider>,
    val fragment: () -> Fragment,
): AppCompatActivity() {

    private lateinit var binding: ConfigurationBinding

    init {
        Timber.d("> init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.initLogging()
        Timber.v("> onCreate()");

        // Will cause the widget host to cancel out of the widget placement if the user presses the back button
        setResult(RESULT_CANCELED)

        ConfigurationBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.v("> onResume()")

        fragment().also {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.configurationContainer.id, it)
                .commit()
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.v("> onPause()")

        Intent(this, provider).run {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            AppWidgetManager.getInstance(application)
                .getAppWidgetIds(ComponentName(getApplication(), provider)).also {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, it)
                }
            sendBroadcast(this)
        }
    }
}
