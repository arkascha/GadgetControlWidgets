package org.rustygnome.gadgetcontrolwidgets

import android.bluetooth.BluetoothManager
import org.junit.jupiter.Test

class RegistryTest {

    private lateinit var registry: Registry

    @BeforeEach
    fun setUp() {
        registry = Registry(mock())
    }

    @Test
    fun locatorBluetoothManager_acceptsInjectedMock() {
        // given
        val mock: BluetoothManager = mock()
        // when
        registry.bluetoothManager = mock()
        // then
        assertThat(registry.bluetoothManager.instance).isEqualTo(mock)
    }
}