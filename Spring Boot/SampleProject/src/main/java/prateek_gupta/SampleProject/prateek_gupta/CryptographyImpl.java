package prateek_gupta.SampleProject.prateek_gupta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HexFormat;

public class CryptographyImpl implements Cryptography {
    private final Logger log = LoggerFactory.getLogger(CryptographyImpl.class);

    /**
     * Resolves the secret key to use. If a secret key is passed from the API payload it is
     * used, otherwise it falls back to the value of CRYPTOGRAPHY_SECRET_KEY from the
     * configuration.
     */
    private static String resolveSecretKey(String secretKey) {
        if (secretKey != null && !secretKey.isEmpty())
            return secretKey;
        return Init.getConfiguration("CRYPTOGRAPHY_SECRET_KEY", "").toString();
    }

    /**
     * This method gets Secret Key in String format and return as an object of the class
     * {@link SecretKey}
     */
    public static SecretKey getDESKey(String secretKey)
            throws Exception {
        // Getting bytes for Secret Key in String format
        byte[] key = resolveSecretKey(secretKey).getBytes();

        // Getting specifications for the DES keys
        DESKeySpec desKeySpec = new DESKeySpec(key);

        // Generating KeyFactory for DES algorithm
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        // Generating Secret Key as per DES Key specification
        return keyFactory.generateSecret(desKeySpec);
    }

    @Override
    public byte[] desEncrypt(String plaintext) throws ServiceException {
        return desEncrypt(plaintext, null);
    }

    @Override
    public byte[] desEncrypt(String plaintext, String secretKeyStr) throws ServiceException {
        log.info("Entering desEncrypt()");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Getting Secret Key
            SecretKey secretKey = getDESKey(secretKeyStr);

            // Generating random string using the algorithm SHA1PRNG which can be
            // used as IV later
            byte[] iv = new byte[8];
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(iv);

            // Generating IV from random string
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Creating Cipher(engine) for encrypting
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // Initializing Cipher with secret key and IV
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            // Inserting IV first
            byteArrayOutputStream.write(iv);
            byteArrayOutputStream.flush();

            // Creating an object of CipherOutputStream using cipher and an output stream so
            // any data passed through this will be automatically encrypted using Cipher and
            // added in the output stream
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    byteArrayOutputStream, cipher);

            // Writing plain text bytes to cipherOutputStream which will internally
            // update the byteArrayOutputStream
            cipherOutputStream.write(plaintext.getBytes(StandardCharsets.UTF_8));
            cipherOutputStream.flush();
            cipherOutputStream.close();

            // Not using ObjectOutputStream because it adds extra metadata, which might
            // create problems when we use in Python, so only we are not using.
            // But in wolken it uses this only
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(cipherOutputStream);
//            objectOutputStream.writeUTF(plaintext);
//            objectOutputStream.close();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting desEncrypt()");
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String desDecrypt(byte[] encryptedText) throws Exception {
        return desDecrypt(encryptedText, null);
    }

    @Override
    public String desDecrypt(byte[] encryptedText, String secretKeyStr) throws Exception {
        // Getting Secret Key
        SecretKey secretKey = getDESKey(secretKeyStr);

        // Setting encrypted data as input source
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encryptedText);

        // Reading IV first as it was inserted first while encrypting
        byte[] iv = new byte[8];
        int ivSize = byteArrayInputStream.read(iv, 0, 8);
        log.info("IVSize : {}", ivSize);

        // Generating IV from random string
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Creating Cipher(engine) for decrypting
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // Initializing Cipher with secret key and IV
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Creating an object of CipherInputStream using cipher and an input stream so
        // any data passed through this will be automatically decrypted using Cipher and
        // added in the input stream
        CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Reading decrypted bytes from cipherInputStream and writing in the
        // byteArrayOutputStream object
        while ((bytesRead = cipherInputStream.read(buffer)) != -1)
            byteArrayOutputStream.write(buffer, 0, bytesRead);

        cipherInputStream.close();

        // Convert decrypted bytes to String
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    @Override
    public String hashSHA256(String plaintext) throws ServiceException {
        String hex;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            hex = HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e.getMessage());
        }
        return hex;
    }

    /**
     * This method gets Secret Key in String format and return as an object of the class
     * {@link SecretKeySpec}
     */
    public static SecretKeySpec getHMacSHA256Key(String secretKey) {
        // Getting bytes for Secret Key in String format
        byte[] key = resolveSecretKey(secretKey).getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(key, "HmacSHA256");
    }

    @Override
    public byte[] hMacSHA256Digest(byte[] key, String plaintext) throws ServiceException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String hMacSHA256Hex(byte[] key, String plaintext) throws ServiceException {
        return HexFormat.of().formatHex(hMacSHA256Digest(key, plaintext));
    }

    @Override
    public String hMacSHA256(String plaintext) throws ServiceException {
        return hMacSHA256(plaintext, null);
    }

    @Override
    public String hMacSHA256(String plaintext, String secretKey) throws ServiceException {
        return hMacSHA256Hex(getHMacSHA256Key(secretKey).getEncoded(), plaintext);
    }

    /**
     * This method gets Secret Key in String format and return as an object of the class
     * {@link SecretKeySpec}
     */
    public static SecretKeySpec getAESKey(String secretKey)
            throws NoSuchAlgorithmException {
        // Getting secret key string
        String secretKeyStr = resolveSecretKey(secretKey);

        // Convert to bytes (UTF-8)
        byte[] keyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);

        // Generating hash, which will be of size 32
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        keyBytes = sha256.digest(keyBytes);

        // Ensure key length is valid for AES (16 bytes = AES-128)
        keyBytes = Arrays.copyOf(keyBytes, 16); // 16 bytes = 128-bit key

        // Create AES key
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public byte[] aesEncrypt(String plaintext) throws ServiceException {
        return aesEncrypt(plaintext, null);
    }

    @Override
    public byte[] aesEncrypt(String plaintext, String secretKeyStr) throws ServiceException {
        log.info("Entering aesEncrypt()");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Getting Secret Key
            SecretKey secretKey = getAESKey(secretKeyStr);

            // Generating random string using the algorithm SHA1PRNG which can be
            // used as IV later
            byte[] iv = new byte[16];
            SecureRandom sr = SecureRandom.getInstanceStrong();
            sr.nextBytes(iv);

            // Generating IV from random string
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Creating Cipher(engine) for encrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Initializing Cipher with secret key and IV
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            // Inserting IV first
            byteArrayOutputStream.write(iv);
            byteArrayOutputStream.flush();

            // Creating an object of CipherOutputStream using cipher and an output stream so
            // any data passed through this will be automatically encrypted using Cipher and
            // added in the output stream
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    byteArrayOutputStream, cipher);

            // Writing plain text bytes to cipherOutputStream which will internally
            // update the byteArrayOutputStream
            cipherOutputStream.write(plaintext.getBytes(StandardCharsets.UTF_8));
            cipherOutputStream.flush();
            cipherOutputStream.close();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting aesEncrypt()");
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String aesDecrypt(byte[] encryptedText) throws Exception {
        return aesDecrypt(encryptedText, null);
    }

    @Override
    public String aesDecrypt(byte[] encryptedText, String secretKeyStr) throws Exception {
        // Getting Secret Key
        SecretKey secretKey = getAESKey(secretKeyStr);

        // Setting encrypted data as input source
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encryptedText);

        // Reading IV first as it was inserted first while encrypting
        byte[] iv = new byte[16];

        @SuppressWarnings("unused")
        int ivSize = byteArrayInputStream.read(iv, 0, 16);

        // Generating IV from random string
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Creating Cipher(engine) for decrypting
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Initializing Cipher with secret key and IV
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Creating an object of CipherInputStream using cipher and an input stream so
        // any data passed through this will be automatically decrypted using Cipher and
        // added in the input stream
        CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Reading decrypted bytes from cipherInputStream and writing in the
        // byteArrayOutputStream object
        while ((bytesRead = cipherInputStream.read(buffer)) != -1)
            byteArrayOutputStream.write(buffer, 0, bytesRead);

        cipherInputStream.close();

        // Convert decrypted bytes to String
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }
}
