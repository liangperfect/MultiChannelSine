package handle;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据处理接口
 */
public interface IHandler {

    /**
     * 解析数据
     * @param is 输入流
     * @return -1解析失败
     */
    int parseData(InputStream is);

    /**
     * 向硬件发送数据
     * @param os 输出流
     */
    void sendHW(OutputStream os);
}
