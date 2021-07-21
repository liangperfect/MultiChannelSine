package handle;

import server.IStateListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 处理某个连接的客户端
 */
public class HandClient implements IHandler {
    private IStateListener listener;
    private String imei = "";
    private IClientListener iClientListener;
    private volatile int deviceNO=0;
    public HandClient(IStateListener listener,IClientListener iClientListener){
        this.listener = listener;
        this.iClientListener = iClientListener;
    }

    @Override
    public int parseData(InputStream is) {
        byte[] bytes = new byte[256];
        try {
            int ret =  is.read(bytes);
            if(ret == -1){
                listener.onParseError(this,"解析数据失败");
                return -1;
            }
            listener.onReceive(this,bytes);
            return 1;
        } catch (IOException e) {
            listener.onParseError(this,e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void sendHW(OutputStream os) {
        try {
            os.write("OK".getBytes());
            listener.onSend(this,"OK".getBytes());
        } catch (IOException e) {
            listener.onSendError(this,e.getMessage());
            iClientListener.onRemoteDisconnect();
            e.printStackTrace();
        }
    }

    public void setDeviceNO(int no){
        this.deviceNO = no;
    }

    public String getImei() {
        return imei;
    }

    public int getDeviceNO() {
        return deviceNO;
    }
}
