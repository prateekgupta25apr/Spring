package com.prateekgupta.DocumentGenerator.service.impl;

import com.prateekgupta.DocumentGenerator.service.WordService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class WordServiceImpl implements WordService {
    @Override
    public byte[] createReport() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            XWPFDocument doc = new XWPFDocument();
            XWPFTable table = doc.createTable(1, 2);

            table.getRow(0).getCell(0).setText("First Column");
            XWPFParagraph image = table.getRow(0).getCell(1).getParagraphs().get(0);
            XWPFRun imageRun = image.createRun();
            imageRun.setTextPosition(20);
            //InputStream inputStream= new URL("https://spring-boot-practice.s3.ap-south-1.amazonaws.com/1651681654131_Screenshot%20%2811%29.png?response-content-disposition=inline&X-Amz-Security-Token=IQoJb3JpZ2luX2VjELD%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCmFwLXNvdXRoLTEiRzBFAiEAwhOO3o6Eyzv6OQv3M5dor2oWQTfXXAWDa%2F3baAeVwUgCIBdFuaGwBnGKXdbsfuNN4irgxbnyFfKt%2FpbC3slLoHe1KuQCCHkQARoMODc3ODI0MzQ1NTUyIgxqq6atxINOtU7uODUqwQK5OC3lA0rUKk1T%2BPITVMg0Zl8SSCQvz4nPf2Xb6kVwQnXVcQHoVaHSWOx7hXk1jnpidZP%2FlbZ7CqUJ0KSjn5kk73WSDEuCuJEd8hE0S6u5E8%2F3Z6HDw5S3twY%2F4vxz9JBeu6C6B7VHmj2z3SbxTgTJfZg3f02M3obl29C26wAOrpJ9v30romzci5MKtiiRg%2FCnThpBDixViWXC29by480%2F%2F9aJYfdzlidKEBkM3LFHrc41QvjSDH36NGfxAsidcsysJUgRwKhqZsKoMz%2BepxmEe%2FYl4faCClS22%2FRcbIXHULDnzXZu2Qi3d3JKkOctq7fNnkWxB2XDirP4oN%2FYqllsM8fwusEuq5B1sgZR5hAwIWHXzNWGeZ7zSo%2BBc4xwDAPTJe4kL5vycolBtT9anRgwaQ2fwVIEIv8mx4dqpyKjT80w%2B8zKkwY6swIl37nrJ7vfb0fEmJvzf6b133HiIsEiO6F8ktp6sKap31BtGnDzj4f2bdYat6lN4A%2B2W84Pcp0jmMsuTZhPNZA6bm9dLnXcmEZ6CsPRKgV19S2Vu5z8jGseXdZhA88uhtWVNbfA%2B8SdvhWYWVVhGlZy%2FpFWk%2FR1WY12itimHyVa5W4xopc3isISs8PrGbq0H2WbwaiStCBDL82KFe8p9%2BXFXCVd%2Bw8qCsKcKvChNI6Go2qQaCAlPFoTzYIsCZROlFwkuWaDN1PaGVEQ%2FjWpcLBq9sRh1aa4BYlzX1nlizJ061Jd0F7QVyvqKu2ity8swPuTxy3FhS%2BgOt3reUGk4%2BAI3VN8hs%2FjU2D9Z8I9CAUOt482ohZRNfg6hhG8jCeegMX3bW1ofnvTNmHuHI7Tfc184cJ2&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220504T181657Z&X-Amz-SignedHeaders=host&X-Amz-Expires=7200&X-Amz-Credential=ASIA4YYTIQXICLFCWA4A%2F20220504%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Signature=8684d7d428c2db262ea68d2342f73da4ec7f586ddf7615622d301ded39345f5a").openStream();

           // imageRun.addPicture(inputStream,
             //       XWPFDocument.PICTURE_TYPE_PNG, "file",
               //     Units.toEMU(200), Units.toEMU(200));

            table.getRow(0).getCell(1).addParagraph();
            XWPFParagraph caption = table.getRow(0).getCell(1).getParagraphs().get(1);
            XWPFRun captionRun = caption.createRun();
            captionRun.setText("Second Column");
            table.setCellMargins(500,500,500,500);
            table.getRow(0).getCell(1).addParagraph();
            table.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcBorders().addNewBottom().setVal(STBorder.NIL);
            table.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcBorders().addNewRight().setVal(STBorder.NIL);
            table.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcBorders().addNewLeft().setVal(STBorder.NIL);
            table.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcBorders().addNewTop().setVal(STBorder.NIL);




            doc.write(byteArrayOutputStream);
        } catch ( IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
