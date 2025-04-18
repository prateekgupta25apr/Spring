package prateek_gupta.SampleProject.aws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import prateek_gupta.SampleProject.aws.service.AWSService;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AWSServiceImpl implements AWSService {
    @Autowired
    private S3Client s3Client;

    private final String bucketName = "pg25";

    @Override
    public String uploadFile(MultipartFile file) {
        String keyName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return response.eTag();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    @Override
    public String deleteFile(String fileName) {
        try{
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return "Success";
        }catch (Exception e){
            throw new RuntimeException("Error deleting file to S3", e);
        }
    }
}

