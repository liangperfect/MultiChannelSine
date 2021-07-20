package model;

import java.util.List;

public class GatewayModel {

    private String imei;
    private List<DeviceModel> devices;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public List<DeviceModel> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceModel> devices) {
        this.devices = devices;
    }
}
