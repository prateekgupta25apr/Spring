package prateek_gupta.SampleProject.prateek_gupta;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import javax.servlet.http.HttpServletResponse;

public interface AWS {
    void getFile(String fileName, HttpServletResponse response) throws ServiceException;
    String uploadFile(MultipartFile file,String fileName) throws ServiceException;
    void deleteFile(String fileName) throws ServiceException;
    boolean fileExists(String fileName) throws ServiceException;
    HeadObjectResponse getFileDetails(String fileName) throws ServiceException;
    String generatePreSignedUrl(
            String key, String method) throws ServiceException;
    String updateFileName(String fileName,String prefix);
    String updateFileName(String fileName);
}
