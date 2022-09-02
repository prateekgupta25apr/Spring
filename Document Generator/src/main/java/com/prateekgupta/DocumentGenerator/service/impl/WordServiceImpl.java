package com.prateekgupta.DocumentGenerator.service.impl;

import com.prateekgupta.DocumentGenerator.service.WordService;
import com.prateekgupta.DocumentGenerator.util.Util;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * For the tables in the Apache POI we use XML and these XML use the schema definitions/
 * file formats named as xsd. And xsd schema definitions define xsd:complexType. So CT
 * in Java Classes stand for Complex Type.
 * TC -> Table Cell
 * Tbl -> Table
 * Pr -> Properties
 * W -> Width
 * ST -> SimpleType
 */

@Service
public class WordServiceImpl implements WordService {

    @Override
    public ByteArrayInputStream createDocument() {
        // Object to hold Document in bytes
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            // Object to hold Document
            XWPFDocument document = new XWPFDocument();

            // Object to hold the Logo
            XWPFParagraph companyLogo = document.createParagraph();

            // Aligning the Logo to center
            companyLogo.setAlignment(ParagraphAlignment.CENTER);

            // Retrieving the Logo image
            InputStream inputStream= new URL("https://s3-us-west-2.amazonaws.com/ws.ca.prod.attachments/1_CA/COMPANY_LOGO/05082019_162256503_1_logo-broadcom.png").openStream();

            // Object to execute configuration for the Logo
            XWPFRun companyLogoRun = companyLogo.createRun();

            // Setting space after the Logo
            companyLogoRun.setTextPosition(20);


            // Adding the Logo to the document
            companyLogoRun.addPicture(inputStream,
                    XWPFDocument.PICTURE_TYPE_PNG, "file",
                    Units.toEMU(200), Units.toEMU(70));

            // Creating data for tables
            Map<String,String> data=new HashMap<>();
            data.put("Header 1","Value 1");
            data.put("Header 2","Value 2");
            data.put("Header 3","Value 3");

            // Create table with borders
            createTable(document,true,data);

            // Creating a Paragraph for space
            XWPFParagraph spacing=document.createParagraph();
            spacing.setSpacingAfter(200);

            // Create table without borders
            createTable(document,false,data);

            // Creating object to hold HTML Content heading
            XWPFParagraph descriptionHeading = document.createParagraph();

            // Setting space before the HTML Content heading
            descriptionHeading.setSpacingBefore(500);

            // Object to execute configuration for the HTML Content heading
            XWPFRun descriptionHeadingRun = descriptionHeading.createRun();

            // Setting text for heading for the HTML Content heading
            descriptionHeadingRun.setText("HTML Content" + " : ");

            // Setting the text style for the Description heading to bold
            descriptionHeadingRun.setBold(true);

            // Setting size for the text for the heading for the HTML Content heading
            descriptionHeadingRun.setFontSize(16);

            // HTML Content
            String HTMLContent="<div>Test</div><br><img src='https://s3-us-west-2.amazonaws.com/ws.ca.prod.attachments/1_CA/COMPANY_LOGO/05082019_162256503_1_logo-broadcom.png'>";

            // Adding the HTML Content to the Document
            setHTMLToWord(document, Util.HTMLPreProcessor(HTMLContent,"word"));

            document.write(out);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    void createTable(XWPFDocument document, boolean withBorders, Map<String,String> data){

        // Creating an object to hold the Table with specific number of rows and columns
        XWPFTable table;
        int dataSize;
        int numberOfColumns;
        if (withBorders) {
            dataSize=data.size();
            numberOfColumns=2;
            // Adding table title
            addTableTitle(document, true,numberOfColumns);
            table = document.createTable(dataSize, numberOfColumns);
        }
        else {
            numberOfColumns=3;
            dataSize=(data.size() % numberOfColumns == 0) ?
                    data.size() / numberOfColumns : (data.size() / numberOfColumns + 1);
            // Adding table title
            addTableTitle(document, false,numberOfColumns);
            table = document.createTable(dataSize, numberOfColumns);
        }


        // Setting width of the Table
        table.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(9400));

        // Setting width of the Table to fixed so that the table width won't vary according to size of the data
        table.getCTTbl().getTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);



        // Adding data to the table

        if (withBorders) {
            int i=0;
            for (String key : data.keySet())
                addTableCellsWithBorders(table, i++, key, data.get(key));
        }
        else {
            int columnsAdded=0;
            for (String key : data.keySet()) {
                addTableCellsWithoutBorders(table,Math.max(columnsAdded / 3, 0),columnsAdded,key,data.get(key));
                columnsAdded++;
            }
            if ((dataSize-1) % numberOfColumns > 0)
                for (int i=1;i<numberOfColumns;i++)
                removeCellBorders(table.getRow(dataSize-1).getCell(i));
        }


    }

    void removeCellBorders(XWPFTableCell cell){
        cell.getCTTc().addNewTcPr().addNewTcBorders().addNewTop().setVal(STBorder.NIL);
        cell.getCTTc().addNewTcPr().addNewTcBorders().addNewRight().setVal(STBorder.NIL);
        cell.getCTTc().addNewTcPr().addNewTcBorders().addNewLeft().setVal(STBorder.NIL);
        cell.getCTTc().addNewTcPr().addNewTcBorders().addNewBottom().setVal(STBorder.NIL);
    }

    void addTableTitle(XWPFDocument document,boolean withBorders, int numberOfColumns){
        // Creating a separate Table for Title
        XWPFTable table=document.createTable(1,numberOfColumns);

        // Setting width of the Table
        table.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(9400));

        // Setting width of the Table to fixed so that the table width won't vary according to size of the data
        table.getCTTbl().getTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);

        // Adding a paragraph(Title) to the 1st row's 1st column(Title Column) of the table
        XWPFParagraph tableTitle=table.getRow(0).getCell(0).addParagraph();

        // Setting alignment of Title
        tableTitle.setFontAlignment(ParagraphAlignment.CENTER.getValue());

        // Setting space after the Title to be 0
        table.getRow(0).getCell(0).getParagraphs().get(0).setSpacingAfter(0);

        // Object to execute configuration for the Title
        XWPFRun tableTitleRun=tableTitle.createRun();

        // Setting text for Title
        tableTitleRun.setText("Table Title");

        // Setting font style for Title to be bold
        tableTitleRun.setBold(true);

        // Setting font size for Title
        tableTitleRun.setFontSize(22);

        // Setting height for the Title's row in the Table
        table.getRow(0).setHeight(1000);

        // Adding a table row property to set the height of the Table's 1st row to exact
        // value as specified
        table.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(
                STHeightRule.EXACT);

        // Setting background color for the Table's 1st row's both the columns
        table.getRow(0).getCell(0).setColor("D3D3D3");
        table.getRow(0).getCell(1).setColor("D3D3D3");

        if (!withBorders){
            // Removing borders of the first cell
            removeCellBorders(table.getRow(0).getCell(0));
        }

        // Creating an object of CTHMerge(which is used to merge 2 columns)
        CTHMerge cthMerge = CTHMerge.Factory.newInstance();

        // Starting column merge
        cthMerge.setVal(STMerge.RESTART);

        // Column from which merge starts
        table.getRow(0).getCell(0).getCTTc().getTcPr().setHMerge(cthMerge);

        // Continuing column merge
        cthMerge.setVal(STMerge.CONTINUE);

        // Next columns to be merged
        for (int i=1;i<=(numberOfColumns-1);i++){
            if (!withBorders)
            removeCellBorders(table.getRow(0).getCell(i));
            table.getRow(0).getCell(i).getCTTc().getTcPr().setHMerge(cthMerge);
        }
    }

    void addTableCellsWithBorders(XWPFTable table,int rowNumber, String headerValue, String normalValue){
        // Setting height for the row in the Table
        table.getRow(rowNumber).setHeight(500);

        // Setting padding for Table cells
        table.setCellMargins(150,150,0,0);

        // Adding a table row property to set the height of the Table's 1st row to exact
        // value as specified
        table.getRow(rowNumber).getCtRow().getTrPr().getTrHeightArray(0).setHRule(
                STHeightRule.EXACT);

        // Adding header value
        XWPFRun cellParagraphRun=table.getRow(rowNumber).getCell(0).getParagraphs().get(0).createRun();
        cellParagraphRun.setText(headerValue);
        cellParagraphRun.setBold(true);


        // Adding normal value
        XWPFRun cellParagraphRun1=table.getRow(rowNumber).getCell(1).getParagraphs().get(0).createRun();
        cellParagraphRun1.setText(normalValue);
    }

    void addTableCellsWithoutBorders(XWPFTable table,int rowNumber, int columnsAdded, String headerValue, String normalValue){

        // Setting height for the row in the Table
        table.getRow(rowNumber).setHeight(500);

        // Setting padding for Table cells
        table.setCellMargins(150,150,0,0);

        // Adding a table row property to set the height of the Table's 1st row to exact
        // value as specified
        table.getRow(rowNumber).getCtRow().getTrPr().getTrHeightArray(0).setHRule(
                STHeightRule.EXACT);

        // Retrieving table row from the table
        XWPFTableRow row=table.getRow(rowNumber);

        // Retrieving table cell from the table row
        XWPFTableCell cell = row.getCell(columnsAdded % 3);

        // Removing borders
        removeCellBorders(cell);

        // Retrieving a paragraph from the table cell
        XWPFParagraph cellParagraph = cell.getParagraphs().get(0);

        // Object to execute configuration for the table cell heading
        XWPFRun cellParagraphRun = cellParagraph.createRun();

        // Setting text for table cell heading
        cellParagraphRun.setText(headerValue + " : ");

        // Setting the text style for the table cell heading to bold
        cellParagraphRun.setBold(true);

        // Object to execute configuration for the table cell value
        cellParagraphRun = cellParagraph.createRun();

        // Setting text for table cell value
        cellParagraphRun.setText(normalValue);
    }

    int HTMLDocId=0;

    /**
     * Method to add an HTML String to WordDoc (*.docx) file
     * @param document: reference to the WordDoc (*.docx) file
     * @param strHTML : HTML content in String format
     */
    void setHTMLToWord(XWPFDocument document, String strHTML) throws Exception {
        // Creating an HTMLDoc (/word/*.html) in the WordDoc (*.docx) file
        MyPOIXMLDocumentPart myPOIXMLDocumentPart=createHTMLPartForWordFile(document,"HTMLDoc" + HTMLDocId++);

        // Setting HTML content in the HTMLDoc (/word/*.html)
        myPOIXMLDocumentPart.setHtml(myPOIXMLDocumentPart.getHtml().replace("<body></body>", "<body>" + strHTML + "</body>"));

        // Adding HTMLDoc (/word/*.html) in the WordDoc (*.docx) file
        document.getDocument().getBody().addNewAltChunk().setId(myPOIXMLDocumentPart.getId());
    }

    /**
     * Method to create an HTMLDoc (/word/*.html) in the WordDoc (*.docx) file
     * @param document : reference to the WordDoc (*.docx) file
     * @param id : id for the HTMLDoc (/word/*.html)
     * @return : reference to the HTMLDoc (/word/*.html) as object of {@link MyPOIXMLDocumentPart}
     */
    MyPOIXMLDocumentPart createHTMLPartForWordFile(XWPFDocument document, String id) throws Exception {
        // Retrieving a Package from the Document
        OPCPackage oPCPackage = document.getPackage();

        // Naming retrieved Package's part
        PackagePartName partName = PackagingURIHelper.createPartName("/word/" + id + ".html");

        // Creating a Package Part(HTMLDoc (/word/*.html)) with provided name
        PackagePart part = oPCPackage.createPart(partName, "text/html");

        // Creating an object to hold the provided Package Part(HTMLDoc (/word/*.html)) with the provided id
        MyPOIXMLDocumentPart myPOIXMLDocumentPart = new MyPOIXMLDocumentPart(part, id);

        // Adding a relation the Package Part(HTMLDoc (/word/*.html)) and the Document (WordDoc (*.docx) file)
        document.addRelation(myPOIXMLDocumentPart.getId(), new MyPOIXMLRelation(), myPOIXMLDocumentPart);
        return myPOIXMLDocumentPart;
    }


    /**
     * Class for the  HTMLDoc (/word/*.html) in the WordDoc (*.docx) file which provides
     * methods for manipulating the HTML.
     * Note: We should NOT use String methods for manipulating HTML!
     */
    private static class MyPOIXMLDocumentPart extends POIXMLDocumentPart {

        private String html;
        private final String id;

        private MyPOIXMLDocumentPart(PackagePart part, String id) {
            super(part);
            this.html = "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"><style></style><title>HTML import</title></head><body></body>";
            this.id = id;
        }

        private String getId() {
            return id;
        }

        private String getHtml() {
            return html;
        }

        private void setHtml(String html) {
            this.html = html;
        }

        @Override
        protected void commit() throws IOException {
            PackagePart part = getPackagePart();
            OutputStream out = part.getOutputStream();
            Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(html);
            writer.close();
            out.close();
        }

    }

    /**
     * Class to provide relation between a  WordDoc (*.docx) file and HTMLDoc
     * (/word/*.html) file
     */
    private final static class MyPOIXMLRelation extends POIXMLRelation {
        private MyPOIXMLRelation() {
            super(
                    "text/html",
                    "http://schemas.openxmlformats.org/officeDocument/2006/relationships/aFChunk",
                    "/word/htmlDoc#.html");
        }
    }

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
