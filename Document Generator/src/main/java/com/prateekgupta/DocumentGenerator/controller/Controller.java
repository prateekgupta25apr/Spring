package com.prateekgupta.DocumentGenerator.controller;

import com.prateekgupta.DocumentGenerator.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    Service service;

    @GetMapping(value = "/pdf")
    ResponseEntity<ByteArrayResource> PDFGenerator() {
        byte[] content = (byte[]) service.PDFGenerator();
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok().contentLength(content.length)
                .header("Content-type",
                        "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=PdfFile.pdf")
                .body(resource);
    }


    @GetMapping(value = "/word")
    ResponseEntity<ByteArrayResource> wordGenerator() {
        byte[] content = (byte[]) service.wordGenerator();
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok().contentLength(content.length)
                .header("Content-type",
                        "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=WordFile.doc")
                .body(resource);
    }

}
