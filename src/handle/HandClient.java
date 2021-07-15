package handle;

import server.IStateListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 处理某个连接的客户端
 */
public class HandClient implements IHandler {
    IStateListener listener;
    String imei = "";
    IClientListener iClientListener;
    public HandClient(IStateListener listener,IClientListener iClientListener){
        this.listener = listener;
        this.iClientListener = iClientListener;
    }

    @Override
    public String parseData(InputStream is) {
        byte[] bytes = new byte[1024];
        try {
            is.read(bytes);
            listener.onReceive(this,bytes);
        } catch (IOException e) {
            listener.onParseError(this,e.getMessage());
            e.printStackTrace();
        }
        String ret = new String(bytes);
        return ret;
    }

    @Override
    public void sendHW(OutputStream os) {
        try {
            os.write("OK".getBytes());
            listener.onSend(this,"OK".getBytes());
        } catch (IOException e) {
//            System.out.println("sendHW发送数据的问题");
            listener.onSendError(this,e.getMessage());
            iClientListener.onRemoteDisconnect();
            e.printStackTrace();
        }
    }
}
