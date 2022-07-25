package com.prateekgupta.DocumentGenerator.controller;

import com.prateekgupta.DocumentGenerator.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/word")
public class WordController {
    @Autowired
    WordService wordService;

    @GetMapping("/create_report")
    ResponseEntity<?> createReport(){
        byte[] content = (byte[]) wordService.createReport();
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok().contentLength(content.length)
                .header("Content-type",
                        "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=WordFile.doc")
                .body(resource);
    }
}
