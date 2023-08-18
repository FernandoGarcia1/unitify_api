package com.tt.unitify.modules.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MonthlyReportDto;
import com.tt.unitify.modules.utils.CONSTANS;
import com.tt.unitify.modules.utils.FirebaseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

    //TODO: El total de ingresos debe registrarse en base de datos para el siguiente mes
    public double monthlyReport(MonthlyReportDto data) throws FileNotFoundException {
        Document doc = new Document();

        try {
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./monthly_report.pdf"));
            final PdfWriter pdfWriter = PdfWriter.getInstance(doc, pdfOutputFile);
            doc.open();

            String title = "Reporte del mes de ".concat(data.getMonth()).concat(" de ").concat(data.getYear());
            Font titleFont = new Font(Font.ITALIC, 18, Font.UNDERLINE, Color.BLACK);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(titleParagraph);
            doc.add(new Paragraph(Chunk.NEWLINE));
            doc.add(new Paragraph(Chunk.NEWLINE));

            Font textFont = new Font(Font.HELVETICA, 12);
            String incomeTitle = "Ingresos";
            Paragraph incomeParagraph = new Paragraph(incomeTitle, textFont);
            doc.add(incomeParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            String textFolios = "Con folios de ".concat(data.getFolioStart()).concat(" a ").concat(data.getFolioEnd());
            Paragraph foliosParagraph = new Paragraph(textFolios, textFont);
            doc.add(foliosParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            String textAmount = "Con un monto total cobrado de $".concat(String.format("%.2f", Double.parseDouble(data.getTotalAmount())));
            Paragraph amountParagraph = new Paragraph(textAmount, textFont);
            doc.add(amountParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            String textFund = "Con un fondo del mes anterior de $".concat(String.format("%.2f",Double.parseDouble(data.getTotalFund())));
            Paragraph fundParagraph = new Paragraph(textFund, textFont);
            doc.add(fundParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            double incomeTotal = Double.parseDouble(data.getTotalAmount()) + Double.parseDouble(data.getTotalFund());
            String textIncomeTotal = "Total de ingresos: $".concat(String.format("%.2f",incomeTotal));
            Paragraph incomeTotalParagraph = new Paragraph(textIncomeTotal, textFont);
            doc.add(incomeTotalParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            LineSeparator separator = new LineSeparator();
            separator.setPercentage(100);
            Chunk linebreak = new Chunk(separator);
            doc.add(linebreak);
            doc.add(new Paragraph(Chunk.NEWLINE));

            String expensesTitle = "Egresos";
            Paragraph expensesParagraph = new Paragraph(expensesTitle, textFont);
            doc.add(expensesParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            String textFirstPayroll = "Nomina de la 1° quincena: $".concat(String.format("%.2f",Double.parseDouble(data.getFirstPayroll())));
            Paragraph firstPayrollParagraph = new Paragraph(textFirstPayroll, textFont);
            doc.add(firstPayrollParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            String textSecondPayroll = "Nomina de la 2° quincena: $".concat(String.format("%.2f",Double.parseDouble(data.getSecondPayroll())));
            Paragraph secondPayrollParagraph = new Paragraph(textSecondPayroll, textFont);
            doc.add(secondPayrollParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            double payrollTotal = Double.parseDouble(data.getFirstPayroll()) + Double.parseDouble(data.getSecondPayroll());
            String textPayrollTotal = "Total de nomina: $".concat(String.format("%.2f",payrollTotal));
            Paragraph payrollTotalParagraph = new Paragraph(textPayrollTotal, textFont);
            doc.add(payrollTotalParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            String otherExpensesTitle = "Otros gastos: $".concat(String.format("%.2f",Double.parseDouble(data.getOthersPayments().getTotalAmount())));
            Paragraph otherExpensesParagraph = new Paragraph(otherExpensesTitle, textFont);
            doc.add(otherExpensesParagraph);
            doc.add(new Paragraph(Element.CHUNK));
            double otherPaymentsTotal = Double.parseDouble(data.getOthersPayments().getTotalAmount());

            String descriptionOtherExpenses = data.getOthersPayments().getDescription();
            Paragraph descriptionOtherExpensesParagraph = new Paragraph(descriptionOtherExpenses, textFont);
            doc.add(descriptionOtherExpensesParagraph);
            doc.add(new Paragraph(Element.CHUNK));

            double totalExpenses = otherPaymentsTotal + payrollTotal;
            String textTotalExpenses = "Total de egresos: $".concat(String.format("%.2f",totalExpenses));
            Paragraph totalExpensesParagraph = new Paragraph("("+textTotalExpenses+")", textFont);
            doc.add(totalExpensesParagraph);
            doc.add(linebreak);
            doc.add(new Paragraph(Chunk.NEWLINE));

            double total = incomeTotal - totalExpenses;
            String textTotal = "Resultante/Faltante: $".concat(String.format("%.2f",total));
            Paragraph totalParagraph = new Paragraph(textTotal, textFont);
            doc.add(totalParagraph);

            if(total > 0){
                String fundNextMounth = "Queda fondo para siguiente mes: $".concat(String.format("%.2f",total));
                Paragraph debtParagraph = new Paragraph(fundNextMounth, textFont);
                doc.add(debtParagraph);
            }else if(total < 0){
                String debt = "Deuda para el siguiente mes: $".concat(String.format("%.2f",total));
                Paragraph debtParagraph = new Paragraph(debt, textFont);
                doc.add(debtParagraph);
            }
            doc.close();
            pdfWriter.close();
            return total;
        } catch (Exception e) {
            log.error("Error generating PDF document. {} - {}", e.getMessage(), e.getStackTrace());
            throw e;
        }
    }
}
