package server;

import handle.HandClient;
import handle.IClientListener;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 处理连接请求线程
 */
public class ServerThread extends Thread implements IClientListener {
    private Socket socket;
    HandClient handler;
    IStateListener iStateListener;
    Timer t;
    private volatile int timeTick = 0; //计时tick
    private volatile int deviceNo = 0; //一个网关下面到设备地址一般都是1-10
    private volatile int isRecv = 0; //0未收到数据，1收到数据

    public ServerThread(Socket socket, IStateListener iStateListener) {
        this.iStateListener = iStateListener;
        this.socket = socket;
        this.handler = new HandClient(iStateListener, this);
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                timeTick = timeTick + 1;
                System.out.println(timeTick + " ---- " + isRecv + " ---- " + deviceNo);
                if (timeTick == ServerConfig.TIMEOUT || isRecv == 1) {//6秒都没接收到数据就发下一个通道到
                    try {
                        handler.sendHW(socket.getOutputStream());
                        timeTick = 1;//一轮里面到时间判断
                        isRecv = 0;
                        deviceNo = deviceNo + 1;//发送一次就是一轮中到一次
                        handler.setDeviceNO(deviceNo);
                        if (deviceNo == ServerConfig.MAXDEVICENO) {
                            //一轮已经完成了
                            System.out.println("完成一轮");
                            deviceNo = 0;
                            timeTick = -ServerConfig.TIMEINTERVAL;//如果当前链接没有断到话，下轮发送到时间
                        }
                    } catch (IOException e) {
                        t.cancel();
                        System.out.println("发送数据异常");
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000);
    }

    public void run() {
        try {
            while (true) {
                //解析从硬件来的数据
                handler.parseData(socket.getInputStream());
                isRecv = 1;
            }
        } catch (Exception e) {
            t.cancel();
            System.out.println("客户端主动断开连接了");
            e.printStackTrace();
        }
        try {
            socket.close();
            t.cancel();
        } catch (IOException e) {
            t.cancel();
            System.out.println("关闭连接出现异常");
            e.printStackTrace();
        }
    }

    @Override
    public void onRemoteDisconnect() {
        t.cancel();
    }
}