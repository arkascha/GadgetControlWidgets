package org.rustygnome.gadgetcontrolwidgets.configuration

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResult
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.App
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBinding
import org.rustygnome.gadgetcontrolwidgets.permissions.INTENT_EXTRA_KEY_PERMISSIONS
import org.rustygnome.gadgetcontrolwidgets.permissions.ensureRequiredPermissions
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Provider
import timber.log.Timber

abstract class ConfigurationActivity(
    private val provider: Class<out Provider>,
    @StringRes private val fragmentTitle: Int,
    val fragment: () -> Fragment,
): AppCompatActivity() {

    private lateinit var binding: ConfigurationBinding
    protected abstract val requiredPermissions: Array<String>

    @CallSuper
    protected open fun onRequiredPermissionsGranted() {
        Timber.v("> onRequiredPermissionsGranted()")
        binding.deniedPermissionsState.deniedPermissionsStateContainer.visibility = GONE
        placeFragmentInContainer()
    }

    init {
        Timber.d("> init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.initLogging()
        Timber.v("> onCreate()")

        // Will cause the widget host to cancel out of the widget placement if the user presses the back button
        setResult(RESULT_CANCELED)

        ConfigurationBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }.also {
            it.configurationDoneButton.setOnClickListener { finish() }
            it.deniedPermissionsState.permissionsButtonOK.setOnClickListener { finish() }
        }

        registerForEnsureRequiredPermissionsResult()
    }

    override fun onPause() {
        Timber.v("> onPause()")
        triggerUpdateOfWidgets()
        super.onPause()
    }

    private fun registerForEnsureRequiredPermissionsResult() {
        Timber.v("> registerForEnsureRequiredPermissionsResult()")
        if (ensureRequiredPermissions(this, requiredPermissions, ::onEnsureRequiredPermissionsResult)) {
            Timber.v("All permissions required by fragment already granted.")
            onRequiredPermissionsGranted()
        } else {
            Timber.v("Waiting for permissions required by fragment to get granted...")
        }
    }

    private fun onEnsureRequiredPermissionsResult(result: ActivityResult) {
        Timber.d("Received back a result from requesting required permissions: ${result.resultCode}")
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                Timber.i("Required permissions are ensured.")
                onRequiredPermissionsGranted()
            }

            else -> {
                result.data?.run {
                    getStringArrayExtra(INTENT_EXTRA_KEY_PERMISSIONS).also {
                        Timber.w("Required permissions are NOT ensured: ${it?.joinToString(", ")}")
                        // TODO: visualize missing permissions
                    }
                } ?: {
                    Timber.w("Required permissions are NOT ensured.")
                }
                onRequiredPermissionsDenied()
            }
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

    private fun onRequiredPermissionsDenied() {
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
