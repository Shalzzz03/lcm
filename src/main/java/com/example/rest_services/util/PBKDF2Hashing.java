package com.example.rest_services.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

public class PBKDF2Hashing {

    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000; // Adjust for security vs. performance
        int keyLength = 256; // Key length in bits
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits salt
        random.nextBytes(salt);
        return salt;
    }
    public static boolean verifyPassword(String originalPassword, String storedHash, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedPassword = hashPassword(originalPassword, salt);
        return hashedPassword.equals(storedHash);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "mysecretpassword";
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        System.out.println("Hashed Password: " + hashedPassword);
        System.out.println("Salt: " + Arrays.toString(salt));

        boolean check = verifyPassword("mysecretpassword", hashedPassword, salt);
        System.out.println("Password check: " + check);
    }
}