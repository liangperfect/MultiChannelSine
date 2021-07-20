package server;

public class ServerConfig {

    public static int PORT = 9090;//服务端端口号
    public static int TIMEOUT = 6;//向硬件发送请求接收反馈超时时间(设备之设备间)
    public static int TIMEINTERVAL = 1000;//下一轮向硬件发送时间间隔(轮与轮之间)
    public static int MAXDEVICENO = 10;//设备的最大编号
}
