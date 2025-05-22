package prateek_gupta.SampleProject.prateek_gupta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


public class AWSImpl implements AWS {

    private final Logger log = LoggerFactory.getLogger(AWSImpl.class);

    private S3Client s3Client;

    private final String bucketName;

    public AWSImpl(
            String accessKey, String secretKey, String bucketName,String regionName)
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
        if (s3Client==null)
            throw new ServiceException("Error while creating an instance of S3 Client");

        this.bucketName=bucketName;
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
    public String uploadFile(MultipartFile file) throws ServiceException {
        log.info("Entering uploadFile()");
        String fileName;
        try {
            fileName=file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException("Error uploading file to S3");
        }
        log.info("Exiting uploadFile()");
        return fileName;
    }

    @Override
    public void deleteFile(String fileName) throws ServiceException {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

        } catch (Exception e) {
            throw new ServiceException("Error deleting file to S3");
        }
    }

    @Override
    public boolean fileExists(String fileName) {
        boolean exists = false;
        try{
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.headObject(headObjectRequest);
            exists= true;

        }catch (Exception e){
            ServiceException.logException(e);
        }
        return exists;
    }
}

