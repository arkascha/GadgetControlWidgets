package org.rustygnome.gadgetcontrolwidgets.configuration

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.App
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBinding
import org.rustygnome.gadgetcontrolwidgets.ensureRequiredPermissions
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.BluetoothModel
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Provider
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.ProviderCompactHorizontal
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.BLUETOOTH_REQUIRED_PERMISSIONS
import timber.log.Timber

abstract class ConfigurationActivity(
    private val provider: Class<out Provider>,
    @StringRes private val fragmentTitle: Int,
    val fragment: () -> Fragment,
): AppCompatActivity() {

    private lateinit var binding: ConfigurationBinding
    protected abstract var requiredPermissions: Array<String>
    protected abstract fun onRequiredPermissionsGranted()

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
        }.also {
            it.configurationDoneButton.setOnClickListener { finish() }
            it.deniedPermissionsState.permissionsButtonOK.setOnClickListener { finish() }
        }

        ensureRequiredPermissions()
    }

    override fun onPause() {
        Timber.v("> onPause()")
        triggerUpdateOfWidgets()
        super.onPause()
    }

    private fun ensureRequiredPermissions() {
        if (ensureRequiredPermissions(this, requiredPermissions)
            { result: ActivityResult ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        Timber.i("Required permissions are ensured.")
                        onRequiredPermissionsGranted()
                        placeFragmentInContainer()
                    }

                    else -> {
                        Timber.w("Required permissions are NOT ensured.")
                        explaineDeniedPermissionState()
                    }
                }
            }
        ) {
            Timber.v("All permissions required by fragment already granted.")
            BluetoothModel.setup(this)
            placeFragmentInContainer()
        } else {
            Timber.v("Waiting for permissions required by fragment to get granted...")
        }
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

    protected fun explaineDeniedPermissionState() {
        binding.deniedPermissionsState.deniedPermissionsStateContainer.visibility = VISIBLE
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
