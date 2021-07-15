package server;

import handle.IHandler;

public interface ISocketService {

    //初始化服务
    void initServe();

    //开启服务
    void  startServe();

    //关闭服务
    void stopServe();
}
