package prateek_gupta.SampleProject.prateek_gupta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.DeleteObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Calendar;


public class AWSImpl implements AWS {

    private final Logger log = LoggerFactory.getLogger(AWSImpl.class);

    private S3Client s3Client;

    private final String bucketName;

    private final S3Presigner preSigner;

    public AWSImpl(
            String accessKey, String secretKey, String bucketName, String regionName)
            throws ServiceException {

        DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
        if (credentialsProvider.resolveCredentials() != null) {


            s3Client = S3Client.builder()
                    .credentialsProvider(credentialsProvider)
                    .region(Region.of(regionName))
                    .build();
        }
//        if (s3Client == null && StringUtils.isNotBlank(accessKey)
//                && StringUtils.isNotBlank(secretKey))
        s3Client = S3Client.builder()
                .region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        System.out.println("Access Key: " +
                credentialsProvider.resolveCredentials().accessKeyId());
        if (s3Client == null)
            throw new ServiceException("Error while creating an instance of S3 Client");

        this.bucketName = bucketName;
        preSigner = S3Presigner.builder()
                .region(Region.of(regionName))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public void getFile(String fileName, HttpServletResponse response)
            throws ServiceException {
        log.info("Entering getFile()");
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object =
                    s3Client.getObject(getObjectRequest);


            response.setContentType(s3Object.response().contentType());
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + fileName);

            // Write content to response output stream
            OutputStream output = response.getOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = s3Object.read(buffer)) != -1)
                output.write(buffer, 0, bytesRead);


            s3Object.close();
            output.flush();

        } catch (Exception e) {
            throw new ServiceException("Error while getting file from S3");
        }
        log.info("Exiting getFile()");
    }

    @Override
    public String uploadFile(MultipartFile file,String fileKey) throws ServiceException {
        log.info("Entering uploadFile()");
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException("Error uploading file to S3");
        }
        log.info("Exiting uploadFile()");
        return fileKey;
    }

    @Override
    public void deleteFile(String fileName) throws ServiceException {
        log.info("Entering deleteFile()");
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

        } catch (Exception e) {
            throw new ServiceException("Error deleting file to S3");
        }
        log.info("Exiting deleteFile()");
    }

    @Override
    public boolean fileExists(String fileName) {
        log.info("Entering fileExists()");
        boolean exists = false;
        try {
            exists = getFileDetails(fileName)==null;
        } catch (Exception e) {
            ServiceException.logException(e);
        }
        log.info("Exiting fileExists()");
        return exists;
    }

    public HeadObjectResponse getFileDetails(String fileName) {
        log.info("Entering getFileDetails()");
        HeadObjectResponse response=null;
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            response=s3Client.headObject(headObjectRequest);

        } catch (Exception e) {
            ServiceException.logException(e);
        }
        log.info("Exiting getFileDetails()");
        return response;
    }

    @Override
    public String generatePreSignedUrl(String key, String method) throws ServiceException {
        log.info("Entering generatePreSignedUrl()");
        Duration duration = Duration.ofHours(1);
        String url;
        try {
            switch (SdkHttpMethod.valueOf(method)) {
                case GET:
                    GetObjectRequest getRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    GetObjectPresignRequest getPreSign = GetObjectPresignRequest.builder()
                            .getObjectRequest(getRequest)
                            .signatureDuration(duration)
                            .build();

                    url = preSigner.presignGetObject(getPreSign).url().toString();
                    break;

                case PUT:
                    PutObjectRequest putRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    PutObjectPresignRequest putPreSign = PutObjectPresignRequest.builder()
                            .putObjectRequest(putRequest)
                            .signatureDuration(duration)
                            .build();

                    url = preSigner.presignPutObject(putPreSign).url().toString();
                    break;

                case DELETE:
                    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    DeleteObjectPresignRequest deletePreSign =
                            DeleteObjectPresignRequest.builder()
                                    .deleteObjectRequest(deleteRequest)
                                    .signatureDuration(duration)
                                    .build();

                    url = preSigner.presignDeleteObject(deletePreSign).url().toString();
                    break;

                default:
                    throw new ServiceException(
                            "Unsupported HTTP method for preSigned URL: " + method);
            }
        } catch (Exception e) {
            throw new ServiceException("Error occurred while generating preSigned url");
        }
        log.info("Exiting generatePreSignedUrl()");
        return url;
    }

    @Override
    public String updateFileName(String fileName, String prefix) {
        int dotIndex = fileName.lastIndexOf('.');
        String name = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String ext  = (dotIndex == -1) ? ""       : fileName.substring(dotIndex);
        return (prefix+name+"_"+Calendar.getInstance().getTimeInMillis()+ext);
    }

    @Override
    public String updateFileName(String fileName) {
        return updateFileName(fileName,"");
    }
}

