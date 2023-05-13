package org.rustygnome.gadgetcontrolwidgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.configuration.Config
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_APWIDGET_CONFIGURE
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_PERMISSION_REQUEST
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_WIDGET_CONFIGURATION
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.INTENT_EXTRA_KEY_PERMISSIONS
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.Request
import org.rustygnome.gadgetcontrolwidgets.databinding.GuiBinding
import org.rustygnome.gadgetcontrolwidgets.introduction.Introduction
import timber.log.Timber

class Gui : AppCompatActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    private lateinit var binding: GuiBinding
    private lateinit var fragmentContainer: ViewGroup

    init {
        Timber.d("> init()");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.v("> onCreate()");
        // Will cause the widget host to cancel out of the widget placement if the user presses the back button
        setResult(RESULT_CANCELED)

        binding = GuiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentContainer = binding.interfaceFragmentContainer
        Timber.d("intent action: ${intent.action.toString()}")
        Timber.d("intent extras: ${intent.extras?.keySet()?.first().toString()}")

        when (intent.action) {
            INTENT_ACTION_PERMISSION_REQUEST -> intent.getStringArrayExtra(INTENT_EXTRA_KEY_PERMISSIONS)?.run {
                createPermissionRequest(this)
            }
            INTENT_ACTION_WIDGET_CONFIGURATION -> createWidgetConfiguration(
                intent.extras?.getInt("appWidgetId")
            )
            INTENT_ACTION_APWIDGET_CONFIGURE -> createWidgetConfiguration(
                intent.extras?.getInt("appWidgetId")
            )
            else -> createAppIntroduction()
        }

//        // Find the widget id from the intent.
//        val intent = intent
//        val extras = intent.extras
//        if (extras != null) {
//            appWidgetId = extras.getInt(
//                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
//            )
//        }
//
//        // If this activity was started with an intent without an app widget ID, finish with an error.
//        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//            finish()
//            return
//        }
//
//        appWidgetText.setText(
//            loadTitlePref(
//                this@Gui,
//                appWidgetId
//            )
//        )
    }

    private fun createAppIntroduction(): Fragment {
        Timber.v("> creatingAppIntroduction")
        return Introduction().also {
            supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, it)
                .commit()
        }
    }

    private fun createPermissionRequest(permissions: Array<String>): Fragment {
        Timber.v("> createPermissionRequest()")
        return Request(permissions).also {
            supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, it)
                .commit()
//            layoutInflater.inflate(R.layout.introduction, fragmentContainer)
        }
    }

    private fun createWidgetConfiguration(
        widgetId: Int?,
    ): Fragment {
        Timber.v("> createWidgetConfiguration")
        return Config().also {
            supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, it)
                .commit()
//            layoutInflater.inflate(R.layout.config, fragmentContainer)
        }

    }
}

private const val PREFS_NAME = "org.rustygnome.gadgetcontrolwidgets.BluetoothGadgetWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    Timber.v("> saveTitlePref()")
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    Timber.v("> loadTitlePref()")
    return context.getSharedPreferences(PREFS_NAME, 0).run {
        getString(PREF_PREFIX_KEY + appWidgetId, null) ?: ""
    }
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    Timber.v("> deleteTitlePref()")
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}
