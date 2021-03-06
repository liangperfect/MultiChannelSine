package handle;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据处理接口
 */
public interface IHandler {

    /**
     * 解析数据
     *
     * @param is 输入流
     * @return -1解析失败  1解析成功
     */
    int parseData(InputStream is);

    /**
     * 向硬件发送数据
     *
     * @param os
     * @return true 发送完一轮请求  fasle 未发送完一轮请求
     */
    boolean sendHW(OutputStream os);
}
