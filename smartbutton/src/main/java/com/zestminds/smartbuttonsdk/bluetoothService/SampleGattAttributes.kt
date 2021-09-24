package com.zestminds.smartbuttonsdk.bluetoothService

import java.util.*

object SampleGattAttributes {
    private val attributes: HashMap<Any?, Any?> =
        HashMap<Any?, Any?>()
    var Generic_Attribute = "00001801-0000-1000-8000-00805f9b34fb"
    var CLIENT_CHARACTERISTIC_CONFIG = "00001800-0000-1000-8000-00805f9b34fb"
    fun lookup(uuid: String?, defaultName: String): Any {
        val name = attributes[uuid]
        return name ?: defaultName
    }

    init {
        // Sample Services.
        attributes["0000180f-0000-1000-8000-00805f9b34fb"] = "BATTERY_SERVICE"
        attributes["0000180a-0000-1000-8000-00805f9b34fb"] = "Device Information"
        // Sample Characteristics.
        attributes[Generic_Attribute] = "Generic_Attribute"
        attributes[CLIENT_CHARACTERISTIC_CONFIG] = "Generic_Access"
        attributes["1d14d6ee-fd63-4fa1-bfa4-8f47b42119f0"] = "OTA_Service"
        attributes["00002a29-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name"
        attributes["00002a24-0000-1000-8000-00805f9b34fb"] = "Model Number"

        attributes["00002a05-0000-1000-8000-00805f9b34fb"]= "Client Characteristics configuration"
        attributes["00002b2a-0000-1000-8000-00805f9b34fb"]= "Database hash"
        attributes["00002b29-0000-1000-8000-00805f9b34fb"]= "Client Supported Feature"
        attributes["00002a00-0000-1000-8000-00805f9b34fb"]= "Device Name"
        attributes["00002a01-0000-1000-8000-00805f9b34fb"]= "Appearance"
        attributes["00002a23-0000-1000-8000-00805f9b34fb"]= "System_Id"
        attributes["f7bf3564-fb6d-4e53-88a4-5e37e0326063"] = "OTA Control Attribute"
        attributes["00002a19-0000-1000-8000-00805f9b34fb"] = "Battery Level"
        attributes["489711b1-65f5-4e17-a1e9-a2debc7ff143"] = "Audio_Data"
        attributes["1c1fe842-9b45-4c26-a9bc-8b1b26d95cdd"] = "Data From Device"



    }
}