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
}
