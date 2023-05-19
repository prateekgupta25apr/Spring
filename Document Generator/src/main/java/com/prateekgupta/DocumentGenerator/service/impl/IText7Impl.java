package com.prateekgupta.DocumentGenerator.service.impl;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.text.DocumentException;
import com.prateekgupta.DocumentGenerator.entities.TabularContentMaster;
import com.prateekgupta.DocumentGenerator.repository.Repository;
import com.prateekgupta.DocumentGenerator.service.IText7;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class IText7Impl implements IText7 {
    @Autowired
    Repository repository;
    public ByteArrayInputStream createDocument() throws DocumentException, IOException {
        // Object to hold document in bytes
        java.io.ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Object to create Document
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument =
                new com.itextpdf.kernel.pdf.PdfDocument(
                        new com.itextpdf.kernel.pdf.PdfWriter(out));

        // Setting borders
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBorderClass());

        // Setting page numbers
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberClass());

        // Creating document
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(
                pdfDocument, com.itextpdf.kernel.geom.PageSize.A4);

        // Setting page margins
        document.setMargins(20,20,20,20);

        // Defining font for heading
        PdfFont headerFont= PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Defining font for normal text
        PdfFont normalFont= PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Adding logo
        com.itextpdf.layout.element.Image image=
                new com.itextpdf.layout.element.Image(ImageDataFactory.create(
                        "https://s3-us-west-2.amazonaws.com/ws.ca.prod.attachments/" +
                                "1_CA/COMPANY_LOGO/" +
                                "05082019_162256503_1_logo-broadcom.png"));
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        image.setMarginBottom(30);
        document.add(image);

        // Creating data for tables
        List<String>headerValue=new ArrayList<>();
        List<String>value=new ArrayList<>();
        for (TabularContentMaster tabularContentMaster:
                repository.getTabularContent(1)){
            headerValue.add(tabularContentMaster.getHeaderValue());
            value.add(tabularContentMaster.getNormalValue());
        }

        // Creating Table with borders
        createTable(document,true,headerValue,value);

        createTable(document,false,headerValue,value);

        // Adding HTML content heading
        com.itextpdf.layout.element.Paragraph paragraph=
                new com.itextpdf.layout.element.Paragraph("HTML Content : ")
                        .setFont(headerFont)
                        .setFontSize(16)
                        .setFontColor(ColorConstants.BLACK);
        document.add(paragraph);

        // Adding HTML content
        String html=repository.getHTMLContent(1).getHtmlContent();;
        List<IElement> htmlData= HtmlConverter.convertToElements(html);
        for (IElement element : htmlData)
            document.add((IBlockElement)element);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void createTable(Document document, boolean withBorders,
                             List<String> headerValue, List<String> value)
            throws IOException {
        float[] colSizes;
        // Defining font for heading
        PdfFont headerFont= PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Defining font for normal text
        PdfFont normalFont= PdfFontFactory.createFont(StandardFonts.HELVETICA);

        if (withBorders){
            colSizes= new float[]{1, 2};
        }
        else
            colSizes=new float[]{1,1,1};

        // Creating a table
        Table table =
                new Table(UnitValue.createPercentArray(colSizes)).useAllAvailableWidth();

        Border border;
        if (withBorders){
            border=new SolidBorder(0.8f);
        }
        else
            border=Border.NO_BORDER;

        table.setBorder(border);

        // Adding table header
        table.addHeaderCell(new Cell(1, 3)
                .add(new com.itextpdf.layout.element.Paragraph("Table Title")
                .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY).setBorder(border));

        if (withBorders) {
            // Adding header cell
            for (String header : headerValue)
                table.addHeaderCell(new Cell().add(
                        new com.itextpdf.layout.element.Paragraph(header))
                        .setPaddingLeft(5));

            // Adding normal value
            for (String normalValue : value)
                table.addCell(new Cell()
                        .add(new com.itextpdf.layout.element.Paragraph(normalValue))
                        .setPaddingLeft(5));
        }
        else{
            // Adding header cell
            for (int i=0; i<headerValue.size();i++) {


                table.addHeaderCell(new Cell().add(new Paragraph(
                                new Text(headerValue.get(i) + " : ").setFont(headerFont)
                        ).add(new Text(value.get(i)).setFont(normalFont)))
                        .setPaddingLeft(5).setBorder(Border.NO_BORDER));
            }
        }

        table.setMarginBottom(50);
        document.add(table);
    }

    public static class PageNumberClass implements IEventHandler {
        @Override
        public void handleEvent(com.itextpdf.kernel.events.Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            com.itextpdf.kernel.pdf.PdfPage page = docEvent.getPage();
            int pageNum = docEvent.getDocument().getPageNumber(page);

            PdfCanvas canvas = new PdfCanvas(page);

            PdfFont pdfFont;
            try {
                pdfFont= PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Creates header text content
            canvas.beginText();
            canvas.setFontAndSize(pdfFont, 12);
            canvas.beginMarkedContent(PdfName.Artifact);
            canvas.moveText(550, 20);
            canvas.showText(String.valueOf(pageNum));
            canvas.endText();
            canvas.stroke();
            canvas.endMarkedContent();
            canvas.release();
        }
    }

    public static class PageBorderClass implements IEventHandler {
        @Override
        public void handleEvent(com.itextpdf.kernel.events.Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
            com.itextpdf.kernel.geom.Rectangle rect = docEvent.getPage().getPageSize();

            canvas
                    .setLineWidth(2)
                    .setStrokeColor(ColorConstants.BLACK)
                    .rectangle(rect)
                    .stroke();
        }
    }

}
