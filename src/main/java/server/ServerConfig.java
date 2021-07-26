package server;

public class ServerConfig {

    //服务基础配置
    public static int PORT = 9090;//服务端端口号
    public static int TIMEOUT = 10;//向硬件发送请求接收反馈超时时间(设备之设备间)
    public static int TIMEINTERVAL = 10;//下一轮向硬件发送时间间隔(轮与轮之间) 只能有 10 ，30，60 120分钟（单位是分钟）
    //Processor常量
    public static int HEARTTIME_TIMEOUT = 180;//心跳监听超时时间
    //    public static int HEARTTIME_TIMEOUT_IDLE = -TIMEINTERVAL + HEARTTIME_TIMEOUT;//在不发送数据的间隔时间内的心跳超时时间
    public static int PROCESSOR_START = 1;//开启processor
    public static int PROCESSOR_PAUSE = 0;//暂停processor
    public static int PROCESSOR_NO_HAS_RECV = 0;//prcessor未收到数据
    public static int PROCESSOR_HAS_RECV = 1;//processor已收到数据
    //Pack解析到配置
    public static String PACKAGE_REGISTER_HEADER = "4040";//注册包到头部标识
    public static String PACKAGE_DATA_HEADER = "4843";//正式数据到头部标识
    public static String PACKAGE_HEARTTIME_HEADER = "4141";//心跳包到头部标识
}
