package com.prateekgupta.DocumentGenerator.controller;

import com.prateekgupta.DocumentGenerator.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/word")
public class WordController {
    @Autowired
    WordService wordService;

    @GetMapping("create_document")
    ResponseEntity<?> createArticle(HttpServletRequest request, HttpServletResponse response){
        try{
            ByteArrayInputStream content = wordService.createDocument();
            response.setHeader("Content-Disposition",
                    "attachment; filename=WordDocument.docx");
            InputStreamResource resource = new InputStreamResource(content);

            InputStream is = resource.getInputStream();

            OutputStream output = response.getOutputStream();

            byte[] bytes = new byte[1024];

            // Read in the bytes
            int numRead;
            while ((numRead = is.read(bytes)) != -1) {
                output.write(bytes, 0, numRead);
            }
            // Ensure all the bytes have been read in
            is.close();
            output.flush();
            output.close();
        }catch (Exception e){
            return new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

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
