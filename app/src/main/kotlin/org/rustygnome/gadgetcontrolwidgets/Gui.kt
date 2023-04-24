package org.rustygnome.gadgetcontrolwidgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_PERMISSION_REQUEST
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_WIDGET_CONFIGURATION
import org.rustygnome.gadgetcontrolwidgets.configuration.Selection
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.Permissions
import org.rustygnome.gadgetcontrolwidgets.databinding.GuiBinding
import org.rustygnome.gadgetcontrolwidgets.introduction.Introduction

class Gui : AppCompatActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    private lateinit var binding: GuiBinding
    private lateinit var fragmentContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Will cause the widget host to cancel out of the widget placement if the user presses the back button
        setResult(RESULT_CANCELED)

        binding = GuiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentContainer = binding.interfaceFragmentContainer

        when (intent.action) {
            INTENT_ACTION_PERMISSION_REQUEST -> createPermissionRequest(fragmentContainer)
            INTENT_ACTION_WIDGET_CONFIGURATION -> createWidgetSelection(fragmentContainer)
            else -> createIntroduction(fragmentContainer)
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

    private fun createIntroduction(
        container: ViewGroup?
    ): Fragment {
        Log.d(TAG, "Creating introduction ...")
        return Introduction().also {
            supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, it)
                .commit()
        }
    }

    private fun createPermissionRequest(
        container: ViewGroup?
    ): Fragment {
        Log.d(TAG, "Creating permission request ...")
        return Permissions().also {
            supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, it)
                .commit()
            layoutInflater.inflate(R.layout.introduction, container)
        }
    }

    private fun createWidgetSelection(
        container: ViewGroup?
    ): Fragment {
        Log.d(TAG, "Creating widget configuration ...")
        return Selection().also {
            supportFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.id, it)
                .commit()
            layoutInflater.inflate(R.layout.introduction, container)
        }
    }

}

private const val PREFS_NAME = "org.rustygnome.gadgetcontrolwidgets.BluetoothGadgetWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    return context.getSharedPreferences(PREFS_NAME, 0).run {
        getString(PREF_PREFIX_KEY + appWidgetId, null) ?: ""
    }
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}