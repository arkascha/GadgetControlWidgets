package org.rustygnome.gadgetcontrolwidgets.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.PermissionsBinding
import timber.log.Timber

class Activity: AppCompatActivity() {

    private var _binding: PermissionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissions: Array<String>

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.d("Permission ??? granted.")
            finish()
        } else {
            Timber.d("Permission ??? denied.")
            binding.permissionsDenialMessage.visibility = VISIBLE
        }
    }

    init {
        Timber.d("> init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("> onCreate()");

        permissions = intent.getStringArrayExtra(INTENT_EXTRA_KEY_PERMISSIONS) ?: arrayOf()

        _binding = PermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Timber.v("> onResume()")

        explainRequestedPermissions()
        binding.permissionsButtonToGrant.setOnClickListener {
            requestPermissions()
        }
    }

    private fun explainRequestedPermissions() {
        binding.permissionsTextExplanation.setText(R.string.permission_activity_explanation_message)
        binding.permissionsListOfPermissions.setText(
            permissions.joinToString("\n") {
                getString(packageManager.getPermissionInfo(it, PackageManager.GET_META_DATA).labelRes)
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