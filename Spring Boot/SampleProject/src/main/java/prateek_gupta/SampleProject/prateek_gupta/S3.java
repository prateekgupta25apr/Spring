package prateek_gupta.SampleProject.prateek_gupta;

import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

public interface S3 {
    byte[] getFileContentInBytes(String fileName) throws ServiceException;
    String uploadFile(byte[] fileContent,String fileKey,String contentType) throws ServiceException;
    void deleteFile(String fileName) throws ServiceException;
    boolean fileExists(String fileName) throws ServiceException;
    HeadObjectResponse getFileDetails(String fileName) throws ServiceException;
    String generatePreSignedUrl(
            String key, String method) throws ServiceException;
    String updateFileName(String fileName,String prefix);
    String updateFileName(String fileName);
    String extractFileName(String urlOrFileKey,boolean onlyFileName);
}
