
package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimplePasswordUtil {
    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public static String hash(String rawPin) {
        // Demo only; in production use salt and a KDF (e.g., PBKDF2, bcrypt, scrypt, Argon2)
        return sha256(rawPin);
    }

    public static boolean matches(String rawPin, String hash) {
        return sha256(rawPin).equals(hash);
    }
}
