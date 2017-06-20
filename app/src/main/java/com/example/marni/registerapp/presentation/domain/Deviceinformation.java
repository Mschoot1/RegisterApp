package com.example.marni.registerapp.presentation.domain;

/**
 * Created by Wallaard on 16-5-2017.
 */

public class Deviceinformation {
    String hardware;
    String type;
    String model;
    String brand;
    String device;
    String manufacturer;
    String user;
    String serial;
    String host;
    String id;
    String bootloader;
    String board;
    String display;

    public Deviceinformation(String hardware, String type, String model, String brand, String device, String manufacturer, String user, String serial, String host, String id, String bootloader, String board, String display) {
        this.hardware = hardware;
        this.type = type;
        this.model = model;
        this.brand = brand;
        this.device = device;
        this.manufacturer = manufacturer;
        this.user = user;
        this.serial = serial;
        this.host = host;
        this.id = id;
        this.bootloader = bootloader;
        this.board = board;
        this.display = display;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBootloader() {
        return bootloader;
    }

    public void setBootloader(String bootloader) {
        this.bootloader = bootloader;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
