package handle;

import config.Config;
import config.Utils;
import model.DeviceModel;
import server.IStateListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据解析与发送
 */
public class HandClient implements IHandler {
    private IStateListener listener;
    private String imei = "";
    private IClientListener iClientListener;
    private volatile int deviceNO = 0;
    private volatile int deviceIndex = 0; //记录当前查询设备的索引
    private volatile int channelIndex = 0;//记录当前查询某个设备里的索引
    private volatile int deviceMaxIndex = 0;//记录设备的最大索引
    private volatile int currChanMaxIndex = 0;//记录当前设备的最大通道索引
    private volatile List<DeviceModel> devices;//当前网关下的设备列表

    public HandClient(IStateListener listener, IClientListener iClientListener) {
        this.listener = listener;
        this.iClientListener = iClientListener;
        this.devices = new ArrayList<>();
    }

    @Override
    public int parseData(InputStream is) {
        byte[] bytes = new byte[32];
        try {
            int ret = is.read(bytes);
            if (ret == -1) {
                listener.onParseError(this, "解析数据失败");
                return -1;
            }
            //二进制转16禁止
            if (analysisData(bytes) == -1) {
                return -1;
            }
            listener.onReceive(this, bytes);
            return 1;
        } catch (IOException e) {
            listener.onParseError(this, e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean sendHW(OutputStream os) {
        try {
            // 索引记录变换
            if (channelIndex > currChanMaxIndex) {
                deviceIndex = deviceIndex + 1;
                channelIndex = 0;
                currChanMaxIndex = this.devices.get(deviceIndex).getChannels().size() - 1;
                return false;
            }

            if (deviceIndex > deviceMaxIndex) {
                //发送完一轮了
                deviceIndex = 0;
                channelIndex = 0;
                return true;
            }
            String deviceAddr = devices.get(deviceIndex).getAddrDev();
            String channelAddr = devices.get(deviceIndex).getChannels().get(channelIndex);
            String sendStr = "4843" + "00" + deviceAddr + "0000" + "01" + channelAddr + "0000000000" + "8CA7AF";
            byte[] sendByte = Utils.hexStringToByteArray(sendStr);
            os.write(sendByte);
            channelIndex = channelIndex + 1;
            listener.onSend(this, sendByte);
        } catch (IOException e) {
            listener.onSendError(this, e.getMessage());
            iClientListener.onRemoteDisconnect();
            e.printStackTrace();
        }
        return false;
    }

    public void setDeviceNO(int no) {
        this.deviceNO = no;
    }

    public String getImei() {
        return imei;
    }

    public int getDeviceNO() {
        return deviceNO;
    }

    /**
     * 分析数据包
     */
    public int analysisData(byte[] bytes) {

        if (!validImei() || bytes == null || bytes.length == 0) {
            return -1;
        }

        String hexStr = Utils.ByteArraytoHex(bytes);
        String packHeader = hexStr.substring(0, 4);
        if (packHeader.equals("4141")) {
            //接收到心跳包
            return 1;
        }

        if (packHeader.equals("4843")) {
            //通道实际数据解析
            //System.out.println("请求到到数据是->" + hexStr);
//            System.out.println("收到到正式数据包是->" + hexStr);
            analysisRealPack(hexStr);
        }

        if (packHeader.equals("4040")) {
            //解析注册包
            int ret = analysisRegisterPack(hexStr);
            return ret;
        }

        return 1;
    }

    /**
     * 解析注册包
     *
     * @param registerStr 注册包数据
     */
    private int analysisRegisterPack(String registerStr) {
        //获取注册包的长度
        String datalenStr = registerStr.substring(4, 6);
        int datalen = Integer.parseInt(datalenStr, 16);
        String realData = registerStr.substring(6, 6 + datalen * 2);
        String realDataAscii = Utils.hexStringToAscii(realData);
        //如果注册包在不在配置列表里面
        boolean isContain = Config.getSingleton().fetchImeiArr().contains(realDataAscii);
        this.imei = realDataAscii;
        if (isContain) {
            //网关包含到配置文件列表中
            List<DeviceModel> dms = Config.getSingleton().getDevicesByImei(imei);
            int s = dms.size();
            if (dms == null || s == 0) {
                return -1;
            }
            this.devices.addAll(dms);
            this.deviceMaxIndex = s - 1;//因为是索引所以要减1
            this.currChanMaxIndex = dms.get(0).getChannels().size() - 1;//因为是索引所以要减1
        }
        return isContain ? 1 : -1;
    }

    /**
     * 解析接收到真实数据
     *
     * @param realStr
     */
    private void analysisRealPack(String realStr) {
        if (realStr.length() <= 50) {
            return;
        }
        String recvAddr = realStr.substring(4, 8);//接收地址，一般规定未0
        String deviceAddr = realStr.substring(8, 12);//设备地址
        String cmd = realStr.substring(12, 14);//采集命令  固定为01
        String chan = realStr.substring(14, 16);//响应采集到通道号
        String rate = realStr.substring(16, 24);//频率值
        String templature = realStr.substring(24, 32);//温度值
        String time = realStr.substring(32, 44);//时间
        String crc = realStr.substring(44, 48);//crc校验码
        String end = realStr.substring(48, 50);//包尾
    }


    /**
     * 验证imei是否在配置列表里面
     *
     * @return
     */
    public boolean validImei() {


        return true;
    }
}
