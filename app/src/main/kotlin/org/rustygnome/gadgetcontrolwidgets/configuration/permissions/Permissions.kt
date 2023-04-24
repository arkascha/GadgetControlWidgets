package org.rustygnome.gadgetcontrolwidgets.configuration.permissions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.configuration.TAG
import org.rustygnome.gadgetcontrolwidgets.databinding.PermissionsBinding

const val INTENT_EXTRA_KEY_CATEGORY = "requested category"
const val INTENT_EXTRA_KEY_PERMISSIONS = "required permissions"

class Permissions: Fragment() {

    lateinit var requiredPermissions: String

    private lateinit var viewButtonToGrant: Button
    private lateinit var viewListOfPermissions: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PermissionsBinding.inflate(inflater, null, false)
        val rootView = binding.getRoot()
        return rootView
    }
//
//    private fun initViews() {
//        setContentView(R.layout.request_permissions);
//
//        viewButtonToGrant = findViewById<Button?>(R.id.requestPermissions_buttonToGrant).apply {
//            setOnClickListener {
//                requestPermissions()
//            }
//        }
//        viewListOfPermissions = findViewById<TextView?>(R.id.requestPermissions_listOfPermissions).apply {
//            text = requiredPermissions
//        }
//    }

    private fun requestPermissions() {
        Log.d(TAG, "Received back result for request of required permissions.")

    }
}