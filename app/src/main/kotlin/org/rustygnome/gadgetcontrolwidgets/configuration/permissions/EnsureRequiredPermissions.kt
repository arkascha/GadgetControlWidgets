package org.rustygnome.gadgetcontrolwidgets

import android.content.Context
import android.content.Intent
import androidx.core.content.PermissionChecker
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_PERMISSION_REQUEST
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.INTENT_EXTRA_KEY_CATEGORY
import org.rustygnome.gadgetcontrolwidgets.configuration.permissions.INTENT_EXTRA_KEY_PERMISSIONS
import timber.log.Timber

fun ensureRequiredPermissions(
    context: Context,
    requestedCategory: String,
    requiredPermission: String
) {
    Timber.v("> ensureRequiredPermissions()")
    Timber.d("Checking permission '$requiredPermission' ...")
    if (PermissionChecker.checkSelfPermission(context, requiredPermission) == PermissionChecker.PERMISSION_DENIED) {
        Timber.d("Required permissions '$requiredPermission' not yet granted.")
        Intent(context, Gui::class.java).apply {
            action = INTENT_ACTION_PERMISSION_REQUEST
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(INTENT_EXTRA_KEY_CATEGORY, requestedCategory)
            putExtra(INTENT_EXTRA_KEY_PERMISSIONS, arrayOf(requiredPermission))
        }.run {
            Timber.d("Starting activity with $this ...")
            context.applicationContext.startActivity(this)
            return
        }
    }
    Timber.d("All required permissions is already granted.")
}
