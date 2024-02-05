package nl.hva.hvacrawler.util.security.password;

import java.security.SecureRandom;

public class SaltMaker {

    private SecureRandom srng;
    private int saltLength;

    public static final int STANDARD_SALT_LENGTH = 8;
    public static final int MINIMUM_SALT_LENGTH = 1;
    public static final int MAXIMUM_SALT_LENGTH = 20;

    public SaltMaker(int saltLength) {
        setSaltLength(saltLength);
        srng = new SecureRandom();
    }

    public SaltMaker() {
        this(STANDARD_SALT_LENGTH);
    }

    public String generateSalt() throws IllegalArgumentException{
        byte[]arr = new byte[saltLength];
        srng.nextBytes(arr);
        return ByteArrayToHexHelper.encodeHexString(arr);
    }

    public void setSaltLength(int saltLength) {
        if(saltLength<MINIMUM_SALT_LENGTH || saltLength>MAXIMUM_SALT_LENGTH){
            throw new IllegalArgumentException("Salt length must be between"
                    + MINIMUM_SALT_LENGTH + " and " + (MAXIMUM_SALT_LENGTH+1));
        }else{
            this.saltLength = saltLength;
        }
    }
}
