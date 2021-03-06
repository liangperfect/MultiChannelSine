import config.Config;
import config.Utils;
import handle.HandClient;
import server.IStateListener;
import server.SocketService;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println("开启服务");
        Config.getSingleton().init();//初始化网关，设备，通道的配置数据
        SocketService ss = new SocketService(new IStateListener() {

            @Override
            public synchronized void onReceive(HandClient client, byte[] bytes) {
                System.out.println("onReceive imei:" + client.getImei() + "----" + "deviceNo:" + client.getDeviceNO());

            }

            @Override
            public synchronized void onSend(HandClient client, byte[] bytes) {
                System.out.println("onSend imei:" + client.getImei() + "----" + "deviceNo:" + client.getDeviceNO());
            }

            @Override
            public synchronized void onDisconnect(HandClient client) {
                System.out.println("onDisconnect imei:" + client.getImei() + "断开连接");
            }

            @Override
            public synchronized void onError(HandClient client) {
                System.out.println("onError imei:" + client.getImei() + "发生错误");
            }

            @Override
            public synchronized void onParseError(HandClient client, String err) {
                System.out.println("onParseError imei:" + client.getImei() + "----解析数据错误" + "   原因:" + err);
            }

            @Override
            public synchronized void onSendError(HandClient client, String err) {
                System.out.println("onSendError imei:" + client.getImei() + "----发送数据错误" + "   原因:" + err);
            }
        });
        ss.startServe();
    }
}
