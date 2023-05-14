package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import org.rustygnome.gadgetcontrolwidgets.R

class Service {
    companion object {
        val map = mapOf<Int, Int>(
            2097152 to R.string.bluetooth_service_audio, // AUDIO
            524288 to R.string.bluetooth_service_capture, // CAPTURE
            8388608 to R.string.bluetooth_service_information, // INFORMATION
            16384 to R.string.bluetooth_service_le_audio, // LE_AUDIO
            8192 to R.string.bluetooth_service_limited_discoverability, // LIMITED_DISCOVERABILITY
            131072 to R.string.bluetooth_service_networking, // NETWORKING
            1048576 to R.string.bluetooth_service_object_transfer, // OBJECT_TRANSFER
            65536 to R.string.bluetooth_service_positioning, // POSITIONING
            262144 to R.string.bluetooth_service_render, // RENDER
            4194304 to R.string.bluetooth_service_telephony, // TELEPHONY
        )
    }
}
