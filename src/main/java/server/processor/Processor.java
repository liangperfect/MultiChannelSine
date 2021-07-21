package server.processor;

import handle.HandClient;
import server.ServerConfig;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

/**
 * 业务状态处理器
 */
public class Processor {

    Socket socket;
    HandClient handler;
    Timer timer;
    private volatile int timeTick = 0; //业务判断的时间戳
    private volatile int deviceNo = 0; //一个网关下面到设备地址一般都是1-10
    private volatile int isRecv = 0; //0未收到数据，1收到数据
    private volatile int isStart = 0;//0未开始  1开始
    private volatile int heartTime = 0;//心跳监听时间戳
    private int jdTime = 0;

    public Processor(Socket socket, HandClient handler, Timer timer) {
        this.socket = socket;
        this.handler = handler;
        this.timer = timer;
        jdTime = -ServerConfig.TIMEINTERVAL + ServerConfig.HEARTTIME_TIMEOUT;
    }

    public void tick() {
        if (isStart == 1) {
            timeTick = timeTick + 1;
            heartTime = heartTime + 1;
            System.out.println("timeTick:" + timeTick + " ---- " + "  heartTime:" + heartTime + " ---- " + "   deviceNo:" + deviceNo);
            if (heartTime != jdTime && heartTime != ServerConfig.HEARTTIME_TIMEOUT) {
                if (timeTick == ServerConfig.TIMEOUT || isRecv == 1) {//6秒都没接收到数据就发下一个通道到
                    try {
                        handler.sendHW(socket.getOutputStream());
                        timeTick = 0;//一轮里面到时间判断
                        isRecv = 0;
                        deviceNo = deviceNo + 1;//发送一次就是一轮中的一次
                        handler.setDeviceNO(deviceNo);
                        if (deviceNo == ServerConfig.MAXDEVICENO) {
                            //一轮已经完成了
                            deviceNo = 0;
                            timeTick = -ServerConfig.TIMEINTERVAL;//如果当前链接没有断到话，下轮发送到时间
                        }
                    } catch (IOException e) {
                        try {
                            timer.cancel();
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("心跳监听超时");
                //心跳监听超时了
                timer.cancel();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重置心跳监听事件
     */
    public void resetHeartTime() {
        this.heartTime = 0;
    }

    /**
     * 开启processor
     */
    public void startProcessor() {
        this.isStart = 1;
    }

    /**
     * 接收到数据，发起下一次数据包请求
     */
    public void fetchNextPackage(){
        this.isRecv = 1;
    }
}
