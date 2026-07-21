package prateek_gupta.SampleProject.prateek_gupta;

public interface Cryptography {
    byte[] desEncrypt(String plaintext) throws ServiceException;
    byte[] desEncrypt(String plaintext, String secretKey) throws ServiceException;
    String desDecrypt(byte[] encryptedText) throws Exception;
    String desDecrypt(byte[] encryptedText, String secretKey) throws Exception;
    String hashSHA256(String plaintext) throws ServiceException;
    byte[] hMacSHA256Digest(byte[] key, String plaintext) throws ServiceException;
    String hMacSHA256Hex(byte[] key, String plaintext) throws ServiceException;
    String hMacSHA256(String plaintext) throws ServiceException;
    String hMacSHA256(String plaintext, String secretKey) throws ServiceException;
    byte[] aesEncrypt(String plaintext) throws ServiceException;
    byte[] aesEncrypt(String plaintext, String secretKey) throws ServiceException;
    String aesDecrypt(byte[] encryptedText) throws Exception;
    String aesDecrypt(byte[] encryptedText, String secretKey) throws Exception;
}
