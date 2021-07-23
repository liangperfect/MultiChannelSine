package server.processor;

import handle.HandClient;
import server.ServerConfig;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;

/**
 * 业务状态处理器
 */
public class Processor {

    Socket socket;
    HandClient handler;
    Timer timer;
    private volatile int timeTick = 0; //业务判断的时间戳
    private volatile int heartTime = 0;//心跳监听时间戳
    private volatile int deviceNo = 0; //一个网关下面到设备地址一般都是1-10
    private volatile int isRecv = 0; //0未收到数据，1收到数据
    private volatile int isStart = ServerConfig.PROCESSOR_PAUSE;//0未开始  1开始
    private volatile boolean startSend = false;//是否开始发送消息请求

    public Processor(Socket socket, HandClient handler, Timer timer) {
        this.socket = socket;
        this.handler = handler;
        this.timer = timer;
    }

    public void tick() {
        if (isStart == ServerConfig.PROCESSOR_START) {
            timeTick = timeTick + 1;
            heartTime = heartTime + 1;

            if (!startSend) {
                Date d = new Date();
                int hour = d.getHours();
                int minute = d.getMinutes();
                int seconds = d.getSeconds();
                int totalSeconds = hour * 60 * 60 + minute * 60 + seconds;
                int duration = ServerConfig.TIMEINTERVAL * 60;//每隔10分钟开始获取一次数据
                if (totalSeconds % duration == 0) {
                    startSend = true;
                }
            }
            System.out.println("timeTick:" + timeTick + " ---- " + "  heartTime:" + heartTime);
            if (heartTime != ServerConfig.HEARTTIME_TIMEOUT) {
                //心跳未超时
                if (startSend) {
                    if (timeTick == ServerConfig.TIMEOUT || isRecv == 1) {//6秒都没接收到数据就发下一个通道到
                        //查询的一轮里面
                        try {
                            timeTick = 0;//重新判断6秒
                            isRecv = ServerConfig.PROCESSOR_NO_HAS_RECV;
                            if (handler.sendHW(socket.getOutputStream())) {
                                //发送完一轮了
                                timeTick = 0;
                                startSend = false;
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
                }
            } else {
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
        this.isStart = ServerConfig.PROCESSOR_START;
    }

    /**
     * 接收到数据，发起下一次数据包请求
     */
    public void fetchNextPackage() {
        this.isRecv = ServerConfig.PROCESSOR_HAS_RECV;
    }
}
