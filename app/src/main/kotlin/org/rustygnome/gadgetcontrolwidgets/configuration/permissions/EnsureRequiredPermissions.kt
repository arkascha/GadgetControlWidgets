package org.rustygnome.gadgetcontrolwidgets

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.PermissionChecker
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_PERMISSION_REQUEST
import org.rustygnome.gadgetcontrolwidgets.configuration.TAG
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.INTENT_EXTRA_KEY_CATEGORY
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.INTENT_EXTRA_KEY_PERMISSIONS

fun ensureRequiredPermissions(
    context: Context,
    requestedCategory: String,
    requiredPermissions: String
) {
    Log.d(TAG, "Ensuring permissions '$requiredPermissions' are granted.")
    if (PermissionChecker.checkSelfPermission(context, requiredPermissions) == PermissionChecker.PERMISSION_DENIED) {
        Log.d(org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.TAG, "Required permissions '$requiredPermissions' not yet granted.")
        Intent(context, Gui::class.java).apply {
            action = INTENT_ACTION_PERMISSION_REQUEST
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(INTENT_EXTRA_KEY_CATEGORY, requestedCategory)
            putExtra(INTENT_EXTRA_KEY_PERMISSIONS, requiredPermissions)
        }.run {
            Log.d(TAG, "Starting activity with $this ...")
            context.startActivity(this)
        }
    }
    Log.d(TAG, "All required permissions is already granted.")
}
