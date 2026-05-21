package prateek_gupta.SampleProject.prateek_gupta;

public class PasswordUtils {
    public static byte[] encryptPassword(String password) throws ServiceException {
        return new CryptographyImpl().aesEncrypt(password);
    }

    public static boolean validPassword(
            String userPassword,byte[] actualPassword) throws Exception {
        String actualPasswordStr=new CryptographyImpl().aesDecrypt(actualPassword);
        return actualPasswordStr.equals(userPassword);
    }
}
