package server;

import handle.IHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService implements ISocketService {

    ServerSocket serverSocket = null;
    Socket socket = null;
    IStateListener listener = null;
    public SocketService(IStateListener listener){
        this.listener = listener;
        initServe();
    }

    @Override
    public void initServe() {
        try {
            //监听socket服务器及配置监听端口
            System.out.println("监听端口");
            serverSocket = new ServerSocket(ServerConfig.PORT);
        } catch (Exception e) {
            System.out.println("端口被占用");
            e.printStackTrace();
        }
    }

    @Override
    public void startServe() {

        try {
            while (true){
                socket = serverSocket.accept();
                ServerThread st = new ServerThread(socket,listener);
                st.start();
            }
        }catch (Exception e) {
            System.out.println("建立与客户端的连接出现异常");
            e.printStackTrace();
        }
    }

    @Override
    public void stopServe() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("关闭服务出现异常");
            e.printStackTrace();
        }
    }
}
