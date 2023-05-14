package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.bluetooth.BluetoothClass.Service
import org.rustygnome.gadgetcontrolwidgets.R

class Service {
    companion object {
        /**
         * map does NOT contain the entries RENDER and LIMITED_DISCOVERABILITY, no interest in those.
         * LE_AUDIO is mapped onto AUDIO, the difference is irrelevant here.
         */
        val map = mapOf(
            Service.AUDIO to R.string.bluetooth_service_audio,
            Service.CAPTURE to R.string.bluetooth_service_capture,
            Service.INFORMATION to R.string.bluetooth_service_information,
            Service.LE_AUDIO to R.string.bluetooth_service_le_audio,
            Service.NETWORKING to R.string.bluetooth_service_networking,
            Service.OBJECT_TRANSFER to R.string.bluetooth_service_object_transfer,
            Service.POSITIONING to R.string.bluetooth_service_positioning,
            Service.TELEPHONY to R.string.bluetooth_service_telephony,
        )
        val unknown = R.string.bluetooth_service_unknown
    }
}
