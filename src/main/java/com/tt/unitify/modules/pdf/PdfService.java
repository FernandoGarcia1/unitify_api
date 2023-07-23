package com.tt.unitify.modules.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.tt.unitify.modules.utils.CONSTANS;
import com.tt.unitify.modules.utils.FirebaseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Log4j2
public class PdfService {


    public void createPdf() {
        Document myPDFDoc = new Document();

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //2) Create a FileOutputStream object with the path and name of the file
            //FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("/sample1.pdf"));

            //3) Now we get a file writer instance from the class com.lowagie.text.pdf.PdfWriter

            PdfWriter.getInstance(myPDFDoc, byteArrayOutputStream);  // Do this BEFORE document.open()


            //4) Once the Filewritter is set up we can open
            //   the document and start adding content
            myPDFDoc.open();  // Open the Document

            // Add a text within a Paragraph
            // (we can add objects from classes implementing the interface com.lowagie.text.Element )
            myPDFDoc.add(new Paragraph("This is a simple PDF created with openPDF"));


            myPDFDoc.close(); //5) Close the Document
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            FirebaseUtil.uploadFile(pdfBytes, "sample1.pdf");
            log.info("PDF created successfully {}", pdfBytes);
            //pdfWriter.close();//6) close the File writer

            log.info("PDF created successfully");

        } catch (Exception e) {
            log.error("Error generating PDF document. {} - {}", e.getMessage(), e.getStackTrace());
        }
    }
}
