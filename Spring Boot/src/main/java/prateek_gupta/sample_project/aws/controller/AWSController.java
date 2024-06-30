package prateek_gupta.sample_project.aws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import prateek_gupta.sample_project.aws.service.AWSService;

@RestController
@RequestMapping("/aws")
public class AWSController {
    @Autowired
    private AWSService awsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String eTag = awsService.uploadFile(file);
        return ResponseEntity.ok("File uploaded successfully. ETag: " + eTag);
    }
}
