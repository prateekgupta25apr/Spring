package prateek_gupta.SampleProject.prateek_gupta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class CryptographyImpl implements Cryptography {
    private final Logger log = LoggerFactory.getLogger(CryptographyImpl.class);

    /**
     * This method takes Secret Key in String format and return as an object of the class
     * {@link SecretKey}
     */
    public static SecretKey getDESKey()
            throws Exception {
        // Getting bytes for Secret Key in String format
        String secretKey= Init.getConfiguration("CRYPTOGRAPHY_SECRET_KEY","").toString();
        byte[] key = secretKey.getBytes();

        // Getting specifications for the DES keys
        DESKeySpec desKeySpec = new DESKeySpec(key);

        // Generating KeyFactory for DES algorithm
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        // Generating Secret Key as per DES Key specification
        return keyFactory.generateSecret(desKeySpec);
    }

    @Override
    public byte[] desEncrypt(String plaintext) throws ServiceException {
        log.info("Entering desEncrypt()");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Getting Secret Key
            SecretKey secretKey = getDESKey();

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
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting desEncrypt()");
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String desDecrypt(byte[] encryptedText) throws Exception {
        // Getting Secret Key
        SecretKey secretKey = getDESKey();

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
}
