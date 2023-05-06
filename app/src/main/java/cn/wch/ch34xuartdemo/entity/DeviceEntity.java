package cn.wch.ch34xuartdemo.entity;

import android.hardware.usb.UsbDevice;

import java.util.ArrayList;

public class DeviceEntity {
    private UsbDevice usbDevice;
    private String description;
    private SerialEntity serialEntities;

    public DeviceEntity(UsbDevice usbDevice, String description, SerialEntity serialEntities) {
        this.usbDevice = usbDevice;
        this.description = description;
        this.serialEntities = serialEntities;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public String getDescription() {
        return description;
    }

    public SerialEntity getSerialEntities() {
        return serialEntities;
    }
}
