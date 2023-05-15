package org.rustygnome.gadgetcontrolwidgets.configuration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.rustygnome.gadgetcontrolwidgets.databinding.ConfigurationBinding
import timber.log.Timber

class Activity: AppCompatActivity() {

    private lateinit var binding: ConfigurationBinding

    init {
        Timber.d("> init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("> onCreate()");

        // Will cause the widget host to cancel out of the widget placement if the user presses the back button
        setResult(RESULT_CANCELED)

        ConfigurationBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.v("> onResume()")

        Bluetooth().also {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.configurationContainer.id, it)
                .commit()
        }
    }
}
