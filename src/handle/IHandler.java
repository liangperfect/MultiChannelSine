package handle;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据处理接口
 */
public interface IHandler {

    //解析从硬件返回的数据
    String parseData(InputStream is);

    //向硬件发送数据请求
    void sendHW(OutputStream os);
}
