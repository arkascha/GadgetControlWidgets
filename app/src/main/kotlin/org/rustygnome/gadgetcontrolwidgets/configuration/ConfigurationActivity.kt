package org.rustygnome.gadgetcontrolwidgets.configuration

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.rustygnome.gadgetcontrolwidgets.App
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBinding
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Model
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Provider
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.ProviderCompactHorizontal
import timber.log.Timber

class HorizontalCompactBluetoothActivity: Activity(
    provider = ProviderCompactHorizontal::class.java,
    fragmentTitle = R.string.widget_bluetooth_description,
    fragment = { BluetoothConfigurationFragment() }
)
class VerticalCompactBluetoothActivity: Activity(
    provider = ProviderCompactHorizontal::class.java,
    fragmentTitle = R.string.widget_bluetooth_description,
    fragment = { BluetoothConfigurationFragment() }
)
class VerticalVerboseBluetoothActivity: Activity(
    provider = ProviderCompactHorizontal::class.java,
    fragmentTitle = R.string.widget_bluetooth_description,
    fragment = { BluetoothConfigurationFragment() }
)

open class Activity(
    private val provider: Class<out Provider>,
    @StringRes private val fragmentTitle: Int,
    val fragment: () -> Fragment,
): AppCompatActivity() {

    private lateinit var binding: ConfigurationBinding

    init {
        Timber.d("> init()")
    }

    fun getModel() = ViewModelProvider(this)[Model::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.initLogging()
        Timber.v("> onCreate()");

        // Will cause the widget host to cancel out of the widget placement if the user presses the back button
        setResult(RESULT_CANCELED)

        initViews()
    }

    private fun initViews() {
        ConfigurationBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }.also {
            it.configurationDoneButton.setOnClickListener { finish() }
        }

        placeFragmentInContainer()
    }

    override fun onResume() {
        super.onResume()
        Timber.v("> onResume()")
    }

    private fun placeFragmentInContainer() {
        binding.configurationFragmentTitle.setText(fragmentTitle)
        fragment().also {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.configurationContainer.id, it)
                .commit()
        }
    }

    override fun onPause() {
        Timber.v("> onPause()")
        triggerUpdateOfWidgets()
        super.onPause()
    }

    private fun triggerUpdateOfWidgets() {
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
