package prateek_gupta.sample_project.aws.service;

import org.springframework.web.multipart.MultipartFile;

public interface AWSService {
    String uploadFile(MultipartFile file);
}
