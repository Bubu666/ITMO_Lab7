package network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    private static final MessageDigest sha_384;

    static {
        MessageDigest digest_tmp = null;
        try {
            digest_tmp = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sha_384 = digest_tmp;
    }

    public static String SHA_384(String input) {
        byte[] hash = sha_384.digest(input.getBytes());

        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
