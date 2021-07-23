package config;

public class Utils {

    /**
     * 二进制数组转16进制字符串
     *
     * @param
     * @return
     */

    public static String ByteArraytoHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String bs = String.format("%02X", b);
            sb.append(bs);
        }

        return sb.toString();
    }

    /**
     * 16进制字符串转二进制数组
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        if (s.length() % 2 != 0) {
            StringBuilder stringBuilder = new StringBuilder(s);
            stringBuilder.insert(s.length() - 1, "0");
            s = stringBuilder.toString();
        }


        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 16进制字符串转换为Ascii字符串
     *
     * @param s 16进制字符串
     * @return ascii字符串
     */
    public static String hexStringToAscii(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 命令加上crc码
     *
     * @param cmd
     * @return
     */
    public static String addCrcString(String cmd) {
        int length = cmd.length();
        byte[] data = hexToByteArray(cmd);
        int crc = CRC16CheckUtil.CRC16_Check(data, data.length);
        String md = CRC16CheckUtil.bytesToHexString(CRC16CheckUtil.intToBytes2(crc));
        if (md.length() > 4) md = md.substring(md.length() - 4);
        cmd += md.substring(0, 2) + md.substring(2);
        return cmd;
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }
}
