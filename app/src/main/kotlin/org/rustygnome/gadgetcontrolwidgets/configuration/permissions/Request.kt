package org.rustygnome.gadgetcontrolwidgets.configuration.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.PermissionsBinding
import timber.log.Timber


const val INTENT_EXTRA_KEY_CATEGORY = "requested category"
const val INTENT_EXTRA_KEY_PERMISSIONS = "required permissions"

class Request(
    val permissions: Array<String>,
): Fragment() {

    private lateinit var binding: PermissionsBinding

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.d("Permission ??? granted.")
            // dismiss / perform back
        } else {
            Timber.d("Permission ??? denied.")
            binding.permissionsDenialMessage.visibility = VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.v("> onCreateView()")
        binding = PermissionsBinding.inflate(inflater, null, false)
        explainRequestedPermissions()
        binding.permissionsButtonToGrant.setOnClickListener {
            requestPermissions()
        }
        return binding.root
    }

    private fun explainRequestedPermissions() {
        binding.permissionsTextExplanation.setText(R.string.permission_activity_explanation_message)
        binding.permissionsListOfPermissions.setText(
            permissions.joinToString("\n") {
                context?.run {
                    getString(
                        packageManager.getPermissionInfo(it, PackageManager.GET_META_DATA).labelRes
                    )
                } ?: it
            }
        )
    }

    private fun requestPermissions() {
        Timber.v("> requestPermissions()")
        permissions.forEach {
            requestPermissionLauncher.launch(it)
        }
    }
}