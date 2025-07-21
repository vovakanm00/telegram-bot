package com.yourname;

public class Equipment {
    private String model;
    private String serialNumber;

    public Equipment(String model, String serialNumber) {
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "Модель: " + model + "\nСерийный номер: " + serialNumber;
    }
}