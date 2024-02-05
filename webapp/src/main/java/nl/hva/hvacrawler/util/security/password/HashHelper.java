package nl.hva.hvacrawler.util.security.password;

import nl.hva.hvacrawler.exception.AlgorithmNotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {

    public static final String SHA_256 = "SHA-256";

    public static String hash(String password){
        String noSalt = "";
        return hash(password,noSalt);
    }

    public static String hash(String password, String salt){
        String noPepper = "";
        return hash(password, salt, noPepper);
    }

    public static String hash(String password, String salt, String pepper){
        String pepper1 = "";
        String pepper2 = pepper;
        if (pepper.length()>=2){
            pepper1 = pepper.substring(0, (pepper.length()/2));
            pepper2 = pepper.substring((pepper.length()/2));
        }
        byte[] digest = getPassBytes(salt, pepper1, password, pepper2);
        return ByteArrayToHexHelper.encodeHexString(digest);
    }

    private static byte[] getPassBytes(String salt,  String pepper1, String password, String pepper2) {
        MessageDigest md = getMessageDigest(SHA_256);
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        md.update(pepper1.getBytes(StandardCharsets.UTF_8));
        md.update(password.getBytes(StandardCharsets.UTF_8));
        md.update(pepper2.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }

    private static MessageDigest getMessageDigest(String algorithm) throws AlgorithmNotFoundException{
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmNotFoundException(algorithm, e);
        }
    }

}
