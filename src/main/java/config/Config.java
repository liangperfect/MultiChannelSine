package config;

import com.google.gson.Gson;
import model.Container;
import model.DeviceModel;
import model.GatewayModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 设备配置
 */
public class Config {

    private volatile Container container;
    private volatile static Config singleton;
    private volatile List<String> imeiArr;

    private Config() {
    }

    public static Config getSingleton() {
        if (singleton == null) {
            synchronized (Config.class) {
                if (singleton == null) {
                    singleton = new Config();
                }
            }
        }
        return singleton;
    }

    public void init() {
        Gson gs = new Gson();
        container = gs.fromJson(datas, Container.class);
        imeiArr = new ArrayList<>();
        container.getDatas().forEach(gatewayModel -> imeiArr.add(gatewayModel.getImei()));
    }

    /**
     * 根据网关imei获取下面的设备列表
     *
     * @param imei
     * @return
     */
    public List<DeviceModel> getDevicesByImei(String imei) {

//        GatewayModel gm = (GatewayModel) container.getDatas().stream().filter(gatewayModel -> gatewayModel.getImei() == imei);
        for (int i = 0; i < container.getDatas().size(); i++) {
            if (container.getDatas().get(i).getImei().equals(imei)) {
                System.out.println("有该imei号的");
                return container.getDatas().get(i).getDevices();
            }
        }

        return null;
    }

    /**
     * 获取网关的imei列表
     *
     * @return
     */
    public List<String> fetchImeiArr() {

        return imeiArr;
    }

    private String datas = "{\n" +
            "  \"datas\": [\n" +
            "    {\n" +
            "      \"imei\": \"868221049924056\",\n" +
            "      \"devices\": [\n" +
            "        {\n" +
            "          \"addrDev\": \"01\",\n" +
            "          \"channels\": [\n" +
            "            \"01\",\n" +
            "            \"02\",\n" +
            "            \"03\",\n" +
            "            \"04\",\n" +
            "            \"05\",\n" +
            "            \"06\",\n" +
            "            \"07\",\n" +
            "            \"08\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
