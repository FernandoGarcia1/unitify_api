package com.tt.unitify.modules.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.tt.unitify.modules.pdf.dto.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.IncomeStatementDto;
import com.tt.unitify.modules.utils.CONSTANS;
import com.tt.unitify.modules.utils.FirebaseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Date;

@Service
@Log4j2
public class PdfService {

    public final String CREATE_FILE_ERROR = "Error generating PDF document. ";

    public void createPdf() {
        Document myPDFDoc = new Document();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(myPDFDoc, byteArrayOutputStream);
            myPDFDoc.open();
            myPDFDoc.add(new Paragraph("This is a simple PDF created with openPDF"));
            myPDFDoc.close(); //5) Close the Document
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            FirebaseUtil.uploadFile(pdfBytes, "sample1.pdf");
            log.info("PDF created successfully {}", pdfBytes);
            log.info("PDF created successfully");
        } catch (Exception e) {
            log.error(CREATE_FILE_ERROR+"{} - {}", e.getMessage(), e.getStackTrace());
        }
    }

    public void pdfExample(){
        Document myPDFDoc = new Document();

        try {
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./sample1.pdf"));

            final PdfWriter pdfWriter = PdfWriter.getInstance(myPDFDoc, pdfOutputFile);

            myPDFDoc.open();  // Open the Document




            /* End of the adding metadata section */
            // Create a Font object
            Font titleFont = new Font(Font.COURIER, 30f, Font.NORMAL, Color.BLUE);
            Paragraph title = new Paragraph(CONSTANS.UNITIFY,titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            Font subTitleFont = new Font(Font.COURIER, 22f, Font.ITALIC, Color.BLUE);
            Paragraph subTitle = new Paragraph((CONSTANS.U_H),subTitleFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);

            myPDFDoc.add(title);
            myPDFDoc.add(subTitle);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            myPDFDoc.add(linebreak);
            // Adding an empty line
            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            myPDFDoc.add(new Paragraph(CONSTANS.FOLIO.concat("1234")));
            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            // Include the text as content of the pdf
            myPDFDoc.add(new Paragraph(CONSTANS.BUILD.concat("2").concat("-").concat(CONSTANS.DEPARTAMENT).concat("2-A")));
            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            myPDFDoc.add(new Paragraph(CONSTANS.PAY_DATE.concat(new Date().toString())));
            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            myPDFDoc.add(new Paragraph(CONSTANS.AMOUNT.concat("2,000")));
            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            myPDFDoc.add(linebreak);
            myPDFDoc.close();
            pdfWriter.close();
            log.info("PDF created successfully");
        } catch (Exception e) {
            log.error(CREATE_FILE_ERROR+"{} - {}", e.getMessage(), e.getStackTrace());
        }
    }

    public void incomeStatement(IncomeStatementDto data){
        Document doc = new Document();

        try {
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./income_report.pdf"));
            final PdfWriter pdfWriter = PdfWriter.getInstance(doc, pdfOutputFile);
            doc.open();

            String title = "Reporte de ingresos correspondiente al mes de ".concat(data.getMonth()).concat(" de ").concat(data.getYear());
            Font titleFont = new Font(Font.ITALIC, 16, Font.UNDERLINE, Color.BLACK);
            Paragraph titleParagraph = new Paragraph(title,titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(titleParagraph);
            doc.add(new Paragraph(Chunk.NEWLINE));
            doc.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[] {16.66F, 10, 13.66F, 22.99F, 13.66F, 22.99F});
            PdfPCell title1 = new PdfPCell(new Paragraph("FOLIO"));
            PdfPCell title2 = new PdfPCell(new Paragraph("EDIF"));
            PdfPCell title3 = new PdfPCell(new Paragraph("DEPTO"));
            PdfPCell title4 = new PdfPCell(new Paragraph("MES PAGADO "));
            PdfPCell title5 = new PdfPCell(new Paragraph("CANTIDAD"));
            PdfPCell title6 = new PdfPCell(new Paragraph("FECHA"));
            title1.setBorder(Rectangle.NO_BORDER);
            title2.setBorder(Rectangle.NO_BORDER);
            title3.setBorder(Rectangle.NO_BORDER);
            title4.setBorder(Rectangle.NO_BORDER);
            title5.setBorder(Rectangle.NO_BORDER);
            title6.setBorder(Rectangle.NO_BORDER);


            table.addCell(title1);
            table.addCell(title2);
            table.addCell(title3);
            table.addCell(title4);
            table.addCell(title5);
            table.addCell(title6);

            for (IncomeStatementDataDto incomeStatementDataDto : data.getData()) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(incomeStatementDataDto.getInvoice()));
                PdfPCell cell2 = new PdfPCell(new Paragraph(incomeStatementDataDto.getBuilding()));
                PdfPCell cell3 = new PdfPCell(new Paragraph(incomeStatementDataDto.getDepartment()));
                PdfPCell cell4 = new PdfPCell(new Paragraph(incomeStatementDataDto.getMonthPaid()));
                PdfPCell cell5 = new PdfPCell(new Paragraph(incomeStatementDataDto.getAmount()));
                PdfPCell cell6 = new PdfPCell(new Paragraph(incomeStatementDataDto.getDate()));

                cell1.setBorder(Rectangle.NO_BORDER);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell3.setBorder(Rectangle.NO_BORDER);
                cell4.setBorder(Rectangle.NO_BORDER);
                cell5.setBorder(Rectangle.NO_BORDER);
                cell6.setBorder(Rectangle.NO_BORDER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
            }




            //Add contentm to the document using Table objects.
            doc.add(table);
            doc.close();
            pdfWriter.close();
        }catch (Exception e) {
            log.error("Error generating PDF document. {} - {}", e.getMessage(), e.getStackTrace());
        }


    }

}
