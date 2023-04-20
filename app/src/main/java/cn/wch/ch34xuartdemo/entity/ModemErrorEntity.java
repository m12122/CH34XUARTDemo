package cn.wch.ch34xuartdemo.entity;

public class ModemErrorEntity {
    public int serialNumber;
    public ErrorType errorType;

    public ModemErrorEntity(int serialNumber, ErrorType errorType) {
        this.serialNumber = serialNumber;
        this.errorType = errorType;
    }

    public static enum ErrorType {
        OVERRUN,
        PARITY,
        FRAME;
    }
}
