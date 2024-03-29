package com.prateekgupta.DocumentGenerator.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.prateekgupta.DocumentGenerator.entities.TabularContentMaster;
import com.prateekgupta.DocumentGenerator.repository.Repository;
import com.prateekgupta.DocumentGenerator.service.PDFService;
import com.prateekgupta.DocumentGenerator.util.Util;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFServiceImpl implements PDFService {
    @Autowired
    Repository repository;
    /**
     * Method to create a PDF document
     * @return an input stream of byte array
     */
    @Override
    public ByteArrayInputStream createDocument() {
        com.itextpdf.text.Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Object to convert document object to bytes
            PdfWriter writer=PdfWriter.getInstance(document, byteArrayOutputStream);

            // Setting border for all the pages in document
            writer.setPageEvent(new PDFDocumentBorder());

            // Setting page number for all the pages in the document
            writer.setPageEvent(new PDFDocumentPagination());

            // To start adding elements to the documents
            document.open();

            // Creating object to hold the Logo image
            Image logoImage = Image.getInstance("" +
                    "https://s3-us-west-2.amazonaws.com/ws.ca.prod.attachments/" +
                    "1_CA/COMPANY_LOGO/05082019_162256503_1_logo-broadcom.png");

            // Setting size for the Logo image
            logoImage.scaleToFit(200, 200);

            // Setting alignment for the Logo Image
            logoImage.setAlignment(Image.MIDDLE);

            // Adding Logo image to the document
            document.add(logoImage);

            // Creating data for tables
            List<String>headerValue=new ArrayList<>();
            List<String>value=new ArrayList<>();
            for (TabularContentMaster tabularContentMaster:repository.getTabularContent(1)){
                headerValue.add(tabularContentMaster.getHeaderValue());
                value.add(tabularContentMaster.getNormalValue());
            }

            // Creating Table with borders
            createTable(document,true,headerValue,value);

            // Creating Table without borders
            createTable(document,false,headerValue,value);

            // Creating an object to hold HTML Content Heading
            Paragraph htmlContentHeading=new Paragraph(
                    new Phrase("HTML Content :",
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16,
                                    BaseColor.BLACK)));

            // Setting space before HTML Content Heading
            htmlContentHeading.setSpacingBefore(25);

            // Adding HTML Content Heading to document
            document.add(htmlContentHeading);

            // HTML Content
            String HTMLContent=repository.getHTMLContent(1).getHtmlContent();

            // Creating an object to hold HTML Content Value
            Paragraph htmlContentValue=new Paragraph();

            // Converting HTML tags to Elements
            ElementList tags= XMLWorkerHelper.parseToElementList(Util.HTMLPreProcessor(HTMLContent,"pdf"), null);

            // Setting space before the HTML Content Value
            htmlContentValue.setSpacingBefore(5);

            // Adding all the Elements(created from HTML) to the htmlContentValue object
            htmlContentValue.addAll(tags);

            // Adding HTMLContentValue to the Document
            document.add(htmlContentValue);

            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * Method to add a table to the document
     * @param document: reference to the document to which the table needs to be added
     * @param withBorders: specifies whether the table should have borders or not
     * @param headerValues: header value to be added to the table
     * @param values: normal value to be added to the table
     */
    void createTable(Document document, boolean withBorders,
                     List<String> headerValues,List<String> values){
        try{
            PdfPTable table;
            int[] colSizes;
            // Creating a Table with 2 columns
            if (withBorders) {
                table = new PdfPTable(2);
                colSizes = new int[]{3, 7};
            }
            else {
                int colNum=headerValues.size();
                if (headerValues.size()%3==1) {
                    colNum += 2;
                    headerValues.add("");
                    headerValues.add("");
                    values.add("");
                    values.add("");
                }
                else if (headerValues.size()%3==2) {
                    colNum += 1;
                    headerValues.add("");
                    values.add("");
                }
                System.out.println(colNum);
                table = new PdfPTable(colNum);
                colSizes=new int[]{1, 1, 1};
            }

            // Setting width of the Table
            table.setWidthPercentage(99);

            // Setting width of the columns in the Table
            table.setWidths(colSizes);

            // Setting space before the Table
            table.setSpacingBefore(20);

            // Setting space after the Table
            table.setSpacingAfter(20);

            // Adding table title
            addTableTitle(table,withBorders,colSizes.length);

            // Adding data to the table
            if (withBorders) {
                for (int i=0;i<headerValues.size();i++)
                    addTableCellsWithBorders(table, headerValues.get(i), values.get(i));
            } else {
                for (int i=0;i<headerValues.size();i++)
                    addTableCellsWithoutBorders(table, headerValues.get(i), values.get(i));
            }

            // Adding the Table to the document
            document.add(table);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to add the title table cell to the table
     * @param table: table to which the title cell needs to be added
     * @param withBorder: whether to set border or not
     * @param colSpan: number of columns the title cell should expand
     */
    void addTableTitle(PdfPTable table, boolean withBorder,int colSpan){
        // Creating the Title Cell for the Table
        PdfPCell tableTitle = new PdfPCell(new Phrase("Table Title",
                new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, GrayColor.BLACK)));

        // Setting background color for the Table Title Cell
        tableTitle.setBackgroundColor(GrayColor.LIGHT_GRAY);

        // Setting Horizontal alignment for the Table Title Cell
        tableTitle.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Setting for how many columns the Table Title Cell should extend
        tableTitle.setColspan(colSpan);

        // Setting padding for the header cell
        tableTitle.setPaddingTop(5);
        tableTitle.setPaddingBottom(5);

        // Setting borders
        if (!withBorder)
            tableTitle.setBorder(Rectangle.NO_BORDER);

        // Adding the Table Title Cell to the table
        table.addCell(tableTitle);
    }

    /**
     * Method to add cells to the table without borders
     * @param table: Reference to the table in which the cells need to be added
     * @param headColumnValue: header value to be added
     * @param columnValue: normal value to be added
     */
    void addTableCellsWithBorders(PdfPTable table, String headColumnValue,
                                  String columnValue){

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

    /**
     * Method to add cells to the table without borders
     * @param table: Reference to the table in which the cells need to be added
     * @param headColumnValue: header value to be added
     * @param columnValue: normal value to be added
     */
    void addTableCellsWithoutBorders(PdfPTable table, String headColumnValue,
                                     String columnValue) {

        // Object to hold the table cell
        PdfPCell cell = new PdfPCell();

        if (!headColumnValue.equals("") && !headColumnValue.equals(" ")) {
            // Object to hold the text value of the table cell
            Phrase value = new Phrase();

            // Adding heading to the table cell text
            value.add(new Chunk(headColumnValue + " : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            // Adding value to the table cell text
            value.add(new Chunk(columnValue, FontFactory.getFont(FontFactory.HELVETICA)));

            // Adding table cell text to table cell
            cell.addElement(value);
        }

        // To stop extending the table cell text to multiple lines
        cell.setNoWrap(true);

        // Setting padding for the table cell
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);

        // To remove the border of the table cell
        cell.setBorder(Rectangle.NO_BORDER);

        // Adding the table cell to table
        table.addCell(cell);

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


    /**
     * Class for the border in the PDF file.
     */
    public static class PDFDocumentBorder extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            // Retrieving content of the Document in bytes
            PdfContentByte pdfContentByte = writer.getDirectContent();

            // Creating a Rectangle with the size of the page
            Rectangle rect = document.getPageSize();

            // Setting border style
            rect.setBorder(Rectangle.BOX);

            // Setting border
            rect.setBorderWidth(2);

            // Setting border color
            rect.setBorderColor(BaseColor.BLACK);

            // Adding the rectangle to the Document
            pdfContentByte.rectangle(rect);
        }
    }

    /**
     * Class for the pagination in the PDF file.
     */
    public static class PDFDocumentPagination extends PdfPageEventHelper {

        public void onStartPage(PdfWriter writer, Document document) {
            // Adding content to page top left
            // ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Left"), 30, 800, 0);

            // Adding content to page top right
            //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800, 0);
        }

        public void onEndPage(PdfWriter writer, Document document) {
            //try {
            // Adding content to page bottom left
            //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Powered By "), 40, 15, 0);

            // Creating an object to store Footer image
            // Image footerImage = Image.getInstance("https://brdcmitsmbst.wolkenservicedesk.com/assets/images/footer.png");

            // Setting the footer image to an absolute position
            //footerImage.setAbsolutePosition(105, 12);

            // Setting size of the footer image
            //footerImage.scalePercent(75,75);

            // Adding the footer image to the Document
            //writer.getDirectContent().addImage(footerImage);


            //} catch (IOException | DocumentException e) {
            //e.printStackTrace();
            //}
            // Adding content to page bottom right
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(String.valueOf(document.getPageNumber()),new Font(Font.FontFamily.HELVETICA,10)), 550, 15, 0);
        }

    }
}
