package nl.hva.hvacrawler.util;

public class HashAndSaltUtil {

    private static final int HASH_LENGTH = 64;
    public static String getSaltFromHashSalt(String hashSalt){
        return hashSalt.substring(HASH_LENGTH);
    }

    public static String getHashFromHashSalt(String hashSalt){
        return hashSalt.substring(0, HASH_LENGTH);
    }
}
