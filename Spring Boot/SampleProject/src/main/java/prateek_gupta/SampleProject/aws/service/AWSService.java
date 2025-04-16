package prateek_gupta.SampleProject.aws.service;

import org.springframework.web.multipart.MultipartFile;

public interface AWSService {
    String uploadFile(MultipartFile file);
    public String deleteFile(String fileName);
}
