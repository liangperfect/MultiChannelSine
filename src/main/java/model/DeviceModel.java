package model;

import java.util.List;

public class DeviceModel {

    private String addrDev;
    private List<String> channels;

    public String getAddrDev() {
        return addrDev;
    }

    public void setAddrDev(String addrDev) {
        this.addrDev = addrDev;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
