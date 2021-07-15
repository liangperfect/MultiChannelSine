package server;

import handle.HandClient;

public interface IStateListener {

    //某个连接中获取到数据
    void onReceive(HandClient client,byte[] bytes);

    //向某个连接发送数据请求
    void onSend(HandClient client,byte[] bytes);

    //某个连接断开
    void onDisconnect(HandClient client);

    //某个连接出错
    void onError(HandClient client);

    //某个连接请求结束数据错误
    void onParseError(HandClient client,String err);

    //m讴歌连接请求发送数据错误
    void onSendError(HandClient client,String err);
}
