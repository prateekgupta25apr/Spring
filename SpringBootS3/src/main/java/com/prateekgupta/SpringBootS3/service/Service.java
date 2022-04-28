package com.prateekgupta.SpringBootS3.service;

import org.springframework.web.multipart.MultipartFile;

public interface Service {
    String uploadFile(MultipartFile file);

    byte[] downloadFile(String fileName);

    String deleteFile(String fileName);
}
