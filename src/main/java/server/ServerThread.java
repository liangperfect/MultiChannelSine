package server;

import handle.HandClient;
import handle.IClientListener;
import server.processor.Processor;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 处理连接请求线程
 */
public class ServerThread extends Thread implements IClientListener {
    Socket socket;
    HandClient handler;
    IStateListener iStateListener;
    Timer t;
    Processor processor;

    public ServerThread(Socket socket, IStateListener iStateListener) {
        this.iStateListener = iStateListener;
        this.socket = socket;
        this.handler = new HandClient(iStateListener, this);
        t = new Timer();
        processor = new Processor(socket, handler, t);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                processor.tick();
            }
        }, 0, 1000);
    }

    public void run() {
        try {
            while (true) {
                int ret = handler.parseData(socket.getInputStream());
                if (ret == -1) {
                    break;
                }
                processor.startProcessor();
                processor.resetHeartTime();
                processor.fetchNextPackage();
            }
        } catch (Exception e) {
            t.cancel();
            System.out.println("客户端主动断开连接了");
            e.printStackTrace();
        }
        try {
            socket.close();
            t.cancel();
            ServerThread.this.interrupt();
        } catch (IOException e) {
            System.out.println("关闭连接出现异常");
            e.printStackTrace();
        }
    }

    @Override
    public void onRemoteDisconnect() {
        t.cancel();
    }
}