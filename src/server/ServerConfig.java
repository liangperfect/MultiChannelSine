package server;

public class ServerConfig {

   public static int PORT = 9090;//服务端端口号
   public static int timeOut = 6;//向硬件发送请求接收反馈超时时间(设备之设备间)
   public static int timeInterval = 1000;//下一轮向硬件发送时间间隔(轮与轮之间)
}
