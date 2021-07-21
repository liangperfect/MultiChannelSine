package config;

import com.google.gson.Gson;
import model.Container;
import model.DeviceModel;
import model.GatewayModel;

import java.util.ArrayList;
import java.util.List;

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
     * @param imei
     * @return
     */
    public List<DeviceModel> getDevicesByImei(String imei) {

        GatewayModel gm = (GatewayModel) container.getDatas().stream().filter(gatewayModel -> gatewayModel.getImei() == imei);
        return gm.getDevices();
    }

    /**
     * 获取网关的imei列表
     *
     * @return
     */
    public List<String> fetchImeiArr() {

        return imeiArr;
    }

    public String datas = "{\n" +
            "  \"datas\": [\n" +
            "    {\n" +
            "      \"imei\": \"111\",\n" +
            "      \"devices\": [\n" +
            "        {\n" +
            "          \"addrDev\": \"01\",\n" +
            "          \"channels\": [\n" +
            "            \"01\",\n" +
            "            \"02\",\n" +
            "            \"03\",\n" +
            "            \"04\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"imei\": \"222\",\n" +
            "      \"devices\": [\n" +
            "        {\n" +
            "          \"addrDev\": \"01\",\n" +
            "          \"channels\": [\n" +
            "            \"01\",\n" +
            "            \"02\",\n" +
            "            \"03\",\n" +
            "            \"04\"\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"addrDev\": \"02\",\n" +
            "          \"channels\": [\n" +
            "            \"01\",\n" +
            "            \"02\",\n" +
            "            \"03\",\n" +
            "            \"04\",\n" +
            "            \"05\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

}
