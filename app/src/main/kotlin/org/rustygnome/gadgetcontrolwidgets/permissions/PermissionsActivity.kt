package org.rustygnome.gadgetcontrolwidgets.permissions

import android.content.Intent
import android.content.pm.PackageManager.GET_META_DATA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.databinding.PermissionsBinding
import org.rustygnome.gadgetcontrolwidgets.extension.string.addItemsAsBulletList
import org.rustygnome.gadgetcontrolwidgets.extension.string.firstCharToUpperCase
import timber.log.Timber

class PermissionsActivity: AppCompatActivity() {

    private var _binding: PermissionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissions: Array<String>

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.filter{ !it.value }.also {
            if (it.isEmpty()) {
                Timber.i("All required permissions granted.")
                setResult(RESULT_OK)
                finish()
            } else {
                Timber.i("Not granted, but required permissions: ${it.keys.joinToString(", ")}.")
                setResult(RESULT_CANCELED, Intent().apply {
                    putStringArrayListExtra(INTENT_EXTRA_KEY_PERMISSIONS, arrayListOf(*it.keys.toTypedArray()))
                })
                binding.permissionsButtonGrant.visibility = GONE
                binding.deniedPermissionsState.deniedPermissionsStateContainer.visibility = VISIBLE
            }
        }
    }

    init {
        Timber.d("> init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("> onCreate()")

        permissions = intent.getStringArrayExtra(INTENT_EXTRA_KEY_PERMISSIONS)?.apply {
            filter {checkSelfPermission(it) != PERMISSION_GRANTED }
        } ?: arrayOf()

        PermissionsBinding.inflate(layoutInflater).also {
            _binding = it
            setContentView(it.root)
        }.run {
            permissionsButtonGrant.setOnClickListener { launchRequestPermissionsDialog() }
            deniedPermissionsState.permissionsButtonOK.setOnClickListener { finish() }
        }
        requestRequiredPermissions()
    }

    private fun explainRequiredPermissions(permissions: List<String>) {
        binding.permissionsTextExplanation.setText(R.string.permission_activity_explanation_message)
        binding.permissionsListOfPermissions.apply {
            removeAllViewsInLayout()
            addItemsAsBulletList(
                permissions
                    .map {getString(packageManager.getPermissionInfo(it, GET_META_DATA).labelRes) }
                    .map { it.firstCharToUpperCase() }
            )
        }
    }

    private fun requestRequiredPermissions() {
        permissions
            .filter { PermissionChecker.checkSelfPermission(this, it) != PermissionChecker.PERMISSION_GRANTED }
            .also {
                if (it.isEmpty()) {
                    Timber.v("All required permissions granted.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Timber.d("Required, but not yet granted permission(s): ${it.joinToString(", ")} ...")
                    explainRequiredPermissions(it)
                    binding.permissionsButtonGrant.visibility = VISIBLE
                }
            }
    }

    private fun launchRequestPermissionsDialog() {
        permissions.also {
            Timber.v("> requestPermissions(${it.joinToString(", ")})")
            requestPermissionLauncher.launch(it)
        }
    }
}