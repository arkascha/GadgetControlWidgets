package org.rustygnome.gadgetcontrolwidgets

import android.content.Context
import android.content.Intent
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_PERMISSION_REQUEST
import org.rustygnome.gadgetcontrolwidgets.permissions.PermissionsActivity
import org.rustygnome.gadgetcontrolwidgets.permissions.INTENT_EXTRA_KEY_PERMISSIONS
import timber.log.Timber

fun ensureRequiredPermissions(
    context: Context,
    requiredPermission: Array<String>
) {
    Timber.v("> ensureRequiredPermissions()")
    requiredPermission
        .filter {PermissionChecker.checkSelfPermission(context, it) != PERMISSION_GRANTED }
        .also {
            if (it.isEmpty()) {
                Timber.v("All required permissions granted.")
            } else {
                Timber.d("Required permission(s) '${it.joinToString("', '")}' not yet granted...")
                Intent(context, PermissionsActivity::class.java).apply {
                    action = INTENT_ACTION_PERMISSION_REQUEST
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(INTENT_EXTRA_KEY_PERMISSIONS, it.toTypedArray())
                }.run {
                    Timber.d("Starting permission activity with intent $this ...")
                    context.applicationContext.startActivity(this)
                }
            }
        }
}
