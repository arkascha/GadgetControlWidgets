package org.rustygnome.gadgetcontrolwidgets.configuration.bluetooth

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import org.rustygnome.gadgetcontrolwidgets.R
import org.rustygnome.gadgetcontrolwidgets.configuration.ConfigurationActivity
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.BLUETOOTH_REQUIRED_PERMISSIONS
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.BluetoothModel
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.Provider
import org.rustygnome.gadgetcontrolwidgets.widget.bluetooth.ProviderCompactHorizontal

class HorizontalCompactBluetoothActivity: BluetoothConfigurationActivity(
    provider = ProviderCompactHorizontal::class.java,
    fragmentTitle = R.string.widget_bluetooth_description,
    fragment = { BluetoothConfigurationFragment() }
)
class VerticalCompactBluetoothActivity: BluetoothConfigurationActivity(
    provider = ProviderCompactHorizontal::class.java,
    fragmentTitle = R.string.widget_bluetooth_description,
    fragment = { BluetoothConfigurationFragment() }
)
class VerticalVerboseBluetoothActivity: BluetoothConfigurationActivity(
    provider = ProviderCompactHorizontal::class.java,
    fragmentTitle = R.string.widget_bluetooth_description,
    fragment = { BluetoothConfigurationFragment() }
)


abstract class BluetoothConfigurationActivity(
    provider: Class<out Provider>,
    @StringRes fragmentTitle: Int,
    fragment: () -> Fragment,
): ConfigurationActivity(
    provider,
    fragmentTitle,
    fragment,
) {
    override var requiredPermissions: Array<String> = BLUETOOTH_REQUIRED_PERMISSIONS
    override fun onRequiredPermissionsGranted() {
        BluetoothModel.setup(this)
    }
}
