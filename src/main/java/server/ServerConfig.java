package server;

public class ServerConfig {

    public static int PORT = 9090;//服务端端口号
    public static int TIMEOUT = 6;//向硬件发送请求接收反馈超时时间(设备之设备间)
    public static int TIMEINTERVAL = 1000;//下一轮向硬件发送时间间隔(轮与轮之间)
    public static int MAXDEVICENO = 10;//设备的最大编号
    //Processor
    public static int HEARTTIME_TIMEOUT = 20;//心跳监听超时时间
    public static int HEARTTIME_TIMEOUT_IDLE = -TIMEINTERVAL + HEARTTIME_TIMEOUT;//在不发送数据的间隔时间内的心跳超时时间
    public static int PROCESSOR_START = 1;//开启processor
    public static int PROCESSOR_PAUSE = 0;//暂停processor
    public static int PROCESSOR_NO_HAS_RECV = 0;//prcessor未收到数据
    public static int PROCESSOR_HAS_RECV = 1;//processor已收到数据
}
