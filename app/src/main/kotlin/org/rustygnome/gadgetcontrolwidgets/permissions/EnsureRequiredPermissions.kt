package org.rustygnome.gadgetcontrolwidgets

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import org.rustygnome.gadgetcontrolwidgets.configuration.INTENT_ACTION_PERMISSION_REQUEST
import org.rustygnome.gadgetcontrolwidgets.permissions.INTENT_EXTRA_KEY_PERMISSIONS
import org.rustygnome.gadgetcontrolwidgets.permissions.PermissionsActivity
import timber.log.Timber

fun ensureRequiredPermissions(
    context: AppCompatActivity,
    requiredPermission: Array<String>,
    resultCallback: ActivityResultCallback<ActivityResult>,
): Boolean {
    Timber.v("> ensureRequiredPermissions()")
    requiredPermission
        .filter {PermissionChecker.checkSelfPermission(context, it) != PERMISSION_GRANTED }
        .also {

            context.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                resultCallback
            ).apply {

                if (it.isEmpty()) {
                    Timber.v("All required permissions granted.")
                    return true

                } else {
                    Timber.d("Required permission(s) '${it.joinToString("', '")}' not yet granted...")
                    launch(
                        Intent(context, PermissionsActivity::class.java).apply {
                            action = INTENT_ACTION_PERMISSION_REQUEST
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            putExtra(INTENT_EXTRA_KEY_PERMISSIONS, it.toTypedArray())
                        }
                    )
                    // indicates, that permissions have to be requested and that the calling activity has to wait for the callback
                    return false
                }
            }
        }
}
