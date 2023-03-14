package com.averta.inventory.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashGenerator {

    private static final Logger logger = LogManager.getLogger(HashGenerator.class);
     private static String myEncryptionKey;
       private static String myEncryptionScheme;
       public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
       private static final String UNICODE_FORMAT = "UTF8";
          static byte[] arrayBytes;
       private static KeySpec ks;
       private static SecretKeyFactory skf;
       private static Cipher cipher;
       static SecretKey key;
    
    public static String generateHash(String password, String salt)
            throws NoSuchAlgorithmException {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }catch (NoSuchAlgorithmException e) {
            logger.error("Error in passowrd hash NoSuchAlgo : ", e);
        }catch(Exception e){
            logger.error("Error in passowrd hash : ", e);
        }
        return generatedPassword;
    }
    
     public static String decrypt(String encryptedString) {
            String decryptedText=null;
            try {
                myEncryptionKey = "ThisIsSpartaThisIsSparta";
                myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
                arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
                ks = new DESedeKeySpec(arrayBytes);
                skf = SecretKeyFactory.getInstance(myEncryptionScheme);
                cipher = Cipher.getInstance(myEncryptionScheme);
                key = skf.generateSecret(ks);
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] encryptedText = Base64.decodeBase64(encryptedString);
                byte[] plainText = cipher.doFinal(encryptedText);
                decryptedText= new String(plainText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decryptedText;
        }

}