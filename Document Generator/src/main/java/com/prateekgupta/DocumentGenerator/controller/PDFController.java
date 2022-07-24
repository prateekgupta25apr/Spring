package com.prateekgupta.DocumentGenerator.controller;

import com.prateekgupta.DocumentGenerator.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    PDFService service;

    @GetMapping("create_article")
    ResponseEntity<ByteArrayResource> createArticle(){
        byte[] content = (byte[]) service.createArticle();
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok().contentLength(content.length)
                .header("Content-type",
                        "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=PdfFile.pdf")
                .body(resource);
    }

    @GetMapping("create_report")
    ResponseEntity<ByteArrayResource> createReport(){
        byte[] content = (byte[]) service.createArticle();
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok().contentLength(content.length)
                .header("Content-type",
                        "application/octet-stream")
                .header("Content-disposition",
                        "attachment; filename=PdfFile.pdf")
                .body(resource);
    }
}
