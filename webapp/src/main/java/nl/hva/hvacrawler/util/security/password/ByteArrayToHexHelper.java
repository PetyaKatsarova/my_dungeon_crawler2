package nl.hva.hvacrawler.util.security.password;

public class ByteArrayToHexHelper {

    private static final int HEX_BASE = 16;

    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }
    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, HEX_BASE);
        hexDigits[1] = Character.forDigit((num & 0xF), HEX_BASE);
        return new String(hexDigits);
    }

}
