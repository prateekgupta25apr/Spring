package prateek_gupta.SampleProject.prateek_gupta;

public interface Cryptography {
    byte[] desEncrypt(String plaintext) throws ServiceException;
    String desDecrypt(byte[] encryptedText) throws Exception;
    String hashSHA256(String plaintext) throws ServiceException;
}
