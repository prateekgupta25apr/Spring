package com.prateekgupta.DocumentGenerator.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.prateekgupta.DocumentGenerator.service.PDFService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PDFServiceImpl implements PDFService {
    @Override
    public ByteArrayInputStream createDocument() {
        com.itextpdf.text.Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Creating object to hold the Logo image
            Image logoImage = Image.getInstance("https://s3-us-west-2.amazonaws.com/ws.ca.prod.attachments/1_CA/COMPANY_LOGO/05082019_162256503_1_logo-broadcom.png");

            // Setting size for the Logo image
            logoImage.scaleToFit(200, 200);

            // Setting alignment for the Logo Image
            logoImage.setAlignment(Image.MIDDLE);

            // Adding Logo image to the document
            document.add(logoImage);

            // Creating Table 1
            createTableWithBorders(document);

            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    void createTableWithBorders(Document document){
        try{
            // Creating a Table with 2 columns
            PdfPTable table=new PdfPTable(2);

            // Setting width of the Table
            table.setWidthPercentage(99);

            // Setting width of the 2 columns in the Table
            table.setWidths(new int[] { 3, 7 });

            // Setting space before the Table
            table.setSpacingBefore(20);

            // Setting space after the Table
            table.setSpacingAfter(20);

            // Creating the Title Cell for the Table
            PdfPCell tableTitle = new PdfPCell(new Phrase("Table Title",
                    new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, GrayColor.BLACK)));

            // Setting background color for the Table Title Cell
            tableTitle.setBackgroundColor(GrayColor.LIGHT_GRAY);

            // Setting Horizontal alignment for the Table Title Cell
            tableTitle.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Setting for how many columns the Table Title Cell should extend
            tableTitle.setColspan(2);

            // Setting padding for the Table Title Cell
            tableTitle.setPadding(5);

            // Adding the Table Title Cell to the table
            table.addCell(tableTitle);

            // Adding data to the table
            addCellsToTableWithBorders(table,"header 1","value 1");
            addCellsToTableWithBorders(table,"header 2","value 2");

            // Adding the Table to the document
            document.add(table);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void addCellsToTableWithBorders(PdfPTable table,String headColumnValue,String columnValue){

        // Creating cell for header value
        PdfPCell headerCell=new PdfPCell();

        // Setting text for header cell
        headerCell.addElement(new Chunk(headColumnValue,FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

        // Setting padding for the header cell
        headerCell.setPadding(5);

        // Adding the header cell to the table
        table.addCell(headerCell);

        // Creating cell for the normal value
        PdfPCell normalCell=new PdfPCell();

        // Setting text for the normal cell
        normalCell.addElement(new Chunk(columnValue,FontFactory.getFont(FontFactory.HELVETICA)));

        // Setting padding for the normal cell
        normalCell.setPadding(5);

        // Adding the normal cell in the table
        table.addCell(normalCell);
    }

    @Override
    public byte[] createReport() {
        com.itextpdf.text.Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Creating Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(99);
            table.setWidths(new int[]{4,6});

            // Adding a table heading
            PdfPCell tableHeading=new PdfPCell(new Phrase("Table Heading"));
            tableHeading.setColspan(2); // to expand the column to 2 columns
            tableHeading.setBackgroundColor(GrayColor.LIGHT_GRAY);
            tableHeading.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeading.setPadding(5);
            table.addCell(tableHeading);

            // Creating a cell for adding Image
//            PdfPCell imageCell = new PdfPCell();
            // Retrieving the image
//            Image img;
//            Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.jpg").toURI());
//            img = Image.getInstance(path.toAbsolutePath().toString());
//            img = Image.getInstance("https://spring-boot-practice.s3.ap-south-1.amazonaws.com/1651169581558_Screenshot%20%289%29.png?response-content-disposition=inline&X-Amz-Security-Token=IQoJb3JpZ2luX2VjELD%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCmFwLXNvdXRoLTEiRzBFAiEAwhOO3o6Eyzv6OQv3M5dor2oWQTfXXAWDa%2F3baAeVwUgCIBdFuaGwBnGKXdbsfuNN4irgxbnyFfKt%2FpbC3slLoHe1KuQCCHkQARoMODc3ODI0MzQ1NTUyIgxqq6atxINOtU7uODUqwQK5OC3lA0rUKk1T%2BPITVMg0Zl8SSCQvz4nPf2Xb6kVwQnXVcQHoVaHSWOx7hXk1jnpidZP%2FlbZ7CqUJ0KSjn5kk73WSDEuCuJEd8hE0S6u5E8%2F3Z6HDw5S3twY%2F4vxz9JBeu6C6B7VHmj2z3SbxTgTJfZg3f02M3obl29C26wAOrpJ9v30romzci5MKtiiRg%2FCnThpBDixViWXC29by480%2F%2F9aJYfdzlidKEBkM3LFHrc41QvjSDH36NGfxAsidcsysJUgRwKhqZsKoMz%2BepxmEe%2FYl4faCClS22%2FRcbIXHULDnzXZu2Qi3d3JKkOctq7fNnkWxB2XDirP4oN%2FYqllsM8fwusEuq5B1sgZR5hAwIWHXzNWGeZ7zSo%2BBc4xwDAPTJe4kL5vycolBtT9anRgwaQ2fwVIEIv8mx4dqpyKjT80w%2B8zKkwY6swIl37nrJ7vfb0fEmJvzf6b133HiIsEiO6F8ktp6sKap31BtGnDzj4f2bdYat6lN4A%2B2W84Pcp0jmMsuTZhPNZA6bm9dLnXcmEZ6CsPRKgV19S2Vu5z8jGseXdZhA88uhtWVNbfA%2B8SdvhWYWVVhGlZy%2FpFWk%2FR1WY12itimHyVa5W4xopc3isISs8PrGbq0H2WbwaiStCBDL82KFe8p9%2BXFXCVd%2Bw8qCsKcKvChNI6Go2qQaCAlPFoTzYIsCZROlFwkuWaDN1PaGVEQ%2FjWpcLBq9sRh1aa4BYlzX1nlizJ061Jd0F7QVyvqKu2ity8swPuTxy3FhS%2BgOt3reUGk4%2BAI3VN8hs%2FjU2D9Z8I9CAUOt482ohZRNfg6hhG8jCeegMX3bW1ofnvTNmHuHI7Tfc184cJ2&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220504T161528Z&X-Amz-SignedHeaders=host&X-Amz-Expires=7200&X-Amz-Credential=ASIA4YYTIQXICLFCWA4A%2F20220504%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Signature=983d87d2646b046564c65af652404ba7e4e32f8a298cda31e038b44a96de9c6e");
//            img.scalePercent((float) 10);
//
//            // Adding image
//            imageCell.addElement(img);
            // Adding caption
            PdfPCell hCell=new PdfPCell(new Phrase("test",new Font(Font.FontFamily.HELVETICA,16,Font.BOLD)));
            hCell.setPadding(5);
            hCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(hCell);
            PdfPCell vCell=new PdfPCell(new Phrase("test",new Font(Font.FontFamily.HELVETICA,16,Font.BOLD)));
            vCell.setPadding(5);
            table.addCell(vCell);
            // Adding the table to document
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
