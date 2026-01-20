package prateek_gupta.SampleProject.prateek_gupta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.SecureRandom;

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
            CipherOutputStream cos = new CipherOutputStream(byteArrayOutputStream, cipher);

            // Creating an object of ObjectOutputStream which helps us to write data along with
            // metadata like length of the content written which will help us while decrypting
            ObjectOutputStream oos = new ObjectOutputStream(cos);
            oos.writeUTF(plaintext);
            oos.close();

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
        CipherInputStream cis = new CipherInputStream(byteArrayInputStream, cipher);

        // Creating an object of ObjectInputStream which helps us to read data from an
        // input source correctly using the metadata like length of the content
        ObjectInputStream ois = new ObjectInputStream(cis);
        return ois.readUTF();
    }
}
