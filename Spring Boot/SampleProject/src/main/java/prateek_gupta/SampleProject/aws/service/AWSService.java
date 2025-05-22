package prateek_gupta.SampleProject.aws.service;

import org.springframework.web.multipart.MultipartFile;
import prateek_gupta.SampleProject.base.ServiceException;

public interface AWSService {
    String uploadFile(MultipartFile file) throws ServiceException;
    public String deleteFile(String fileName);
}
