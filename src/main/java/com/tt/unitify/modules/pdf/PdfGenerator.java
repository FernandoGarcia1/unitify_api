package com.tt.unitify.modules.pdf;

import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.AnnualPaymentReportDto;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.DepartmentDataDto;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.PaymentReportDto;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.PaymentReportDtoComparator;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlydepartmentreport.MonthlyDepartmentReportDto;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MonthlyReportDto;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollData;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollReportDto;
import com.tt.unitify.modules.utils.CONSTANS;
import com.tt.unitify.modules.utils.FirebaseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

@Service
@Log4j2
public class PdfGenerator {

    public static final String CREATE_FILE_ERROR = "Error generating PDF document. ";

    public void createPdf() {
        Document document = new Document();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.add(new Paragraph("This is a simple PDF created with openPDF"));
            document.close(); //5) Close the Document
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            FirebaseUtil.uploadFile(pdfBytes, "sample1.pdf");
            log.info("PDF created successfully {}", pdfBytes);
            log.info("PDF created successfully");
        } catch (Exception e) {
            log.error(CREATE_FILE_ERROR+"{} - {}", e.getMessage(), e.getStackTrace());
        }
    }

    public void monthlyDepartmentReport(MonthlyDepartmentReportDto data){

        Document document = new Document(PageSize.A6.rotate());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data.getCorrespondingDate());

        try {
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./sample1.pdf"));

            final PdfWriter pdfWriter = PdfWriter.getInstance(document, pdfOutputFile);

            document.open();  // Open the Document


            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
            String correspondingDateFormat = dateFormat.format(data.getCorrespondingDate());

            SimpleDateFormat completeDateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es", "ES"));
            /* End of the adding metadata section */
            // Create a Font object
            Font titleFont = new Font(Font.COURIER, 12f, Font.NORMAL, Color.BLUE);
            Paragraph title = new Paragraph(CONSTANS.UNITIFY,titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            Font subTitleFont = new Font(Font.COURIER, 12f, Font.ITALIC, Color.BLUE);
            Paragraph subTitle = new Paragraph((CONSTANS.U_H),subTitleFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);


            Font textFont = new Font(Font.COURIER, 10f);

            document.add(title);
            document.add(subTitle);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            document.add(linebreak);
            // Adding an empty line

            document.add(new Paragraph(CONSTANS.FOLIO.concat(data.getFolio()),textFont));


            // Include the text as content of the pdf
            document.add(new Paragraph(CONSTANS.BUILD.concat(data.getBuilding()).concat(" ").concat(CONSTANS.DEPARTAMENT).concat(data.getDepartment()), textFont));


            document.add(new Paragraph(CONSTANS.PAY_DATE.concat(correspondingDateFormat), textFont));


            document.add(new Paragraph(CONSTANS.COMPLETED_DATE.concat(completeDateFormat.format(data.getCompletedDate())), textFont));

            document.add(new Paragraph(CONSTANS.CONCEPT.concat(data.getDescription()), textFont));


            document.add(new Paragraph(CONSTANS.AMOUNT.concat(data.getAmount()), textFont));
            document.add(new Paragraph(Chunk.NEWLINE));

            document.close();
            pdfWriter.close();
            log.info("PDF created successfully");
        } catch (Exception e) {
            log.error(CREATE_FILE_ERROR+"{} - {}", e.getMessage(), e.getStackTrace());
        }
    }

    public void incomeStatement(IncomeStatementDto data){
        Document doc = new Document();

        try {
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./income_report"+data.getMonth()+"_"+data.getYear()+".pdf"));
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
                String correspondingDateFormat = dateFormat.format(incomeStatementDataDto.getMonthPaid());
                String paymentDate = dateFormat.format(incomeStatementDataDto.getDate());

                PdfPCell cell1 = new PdfPCell(new Paragraph(incomeStatementDataDto.getInvoice()));
                PdfPCell cell2 = new PdfPCell(new Paragraph(incomeStatementDataDto.getBuilding()));
                PdfPCell cell3 = new PdfPCell(new Paragraph(incomeStatementDataDto.getDepartment()));
                PdfPCell cell4 = new PdfPCell(new Paragraph(correspondingDateFormat.toUpperCase()));
                PdfPCell cell5 = new PdfPCell(new Paragraph(incomeStatementDataDto.getAmount()));
                PdfPCell cell6 = new PdfPCell(new Paragraph(paymentDate.toUpperCase()));

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
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./monthly_report_"+data.getMonth()+"_"+data.getYear()+".pdf"));
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

    /*public void annualPaymentReport(AnnualPaymentReportDto data) throws FileNotFoundException {
        Document doc = new Document();
        doc.setPageSize(PageSize.A4.rotate());
        try {

            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./annual_report_building_"+data.getBuilding()+"_"+data.getYear()+".pdf"));
            final PdfWriter pdfWriter = PdfWriter.getInstance(doc, pdfOutputFile);
            doc.open();

            String title = "EDIFICIO ".concat(data.getBuilding());
            Font titleFont = new Font(Font.ITALIC, 12, Font.UNDERLINE, Color.BLACK);
            Paragraph titleParagraph = new Paragraph(title,titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(titleParagraph);
            doc.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table = new PdfPTable(14);
            table.setWidthPercentage(100);
            table.setWidths(new float[] {4.142F, 10.142F, 7.142F,7.142F, 7.142F,7.142F, 7.142F,7.142F, 7.142F,7.142F, 7.142F,7.142F, 7.142F,7.142F});

            Font titleColumn = new Font(1, 6);

            PdfPCell title1 = new PdfPCell(new Paragraph("DEPTO",titleColumn));
            PdfPCell title2 = new PdfPCell(new Paragraph("NOMBRE",titleColumn));
            PdfPCell title3 = new PdfPCell(new Paragraph("ENERO",titleColumn));
            PdfPCell title4 = new PdfPCell(new Paragraph("FEBRERO",titleColumn));
            PdfPCell title5 = new PdfPCell(new Paragraph("ABRIL",titleColumn));
            PdfPCell title6 = new PdfPCell(new Paragraph("MARZO",titleColumn));
            PdfPCell title7 = new PdfPCell(new Paragraph("MAYO",titleColumn));
            PdfPCell title8 = new PdfPCell(new Paragraph("JUNIO",titleColumn));
            PdfPCell title9 = new PdfPCell(new Paragraph("JULIO",titleColumn));
            PdfPCell title10 = new PdfPCell(new Paragraph("AGOSTO",titleColumn));
            PdfPCell title11 = new PdfPCell(new Paragraph("SEPTIEMBRE",titleColumn));
            PdfPCell title12 = new PdfPCell(new Paragraph("OCTUBRE",titleColumn));
            PdfPCell title13 = new PdfPCell(new Paragraph("NOVIEMBRE",titleColumn));
            PdfPCell title14 = new PdfPCell(new Paragraph("DICIEMBRE",titleColumn));


            table.addCell(title1);
            table.addCell(title2);
            table.addCell(title3);
            table.addCell(title4);
            table.addCell(title5);
            table.addCell(title6);
            table.addCell(title7);
            table.addCell(title8);
            table.addCell(title9);
            table.addCell(title10);
            table.addCell(title11);
            table.addCell(title12);
            table.addCell(title13);
            table.addCell(title14);
            Font cellFont = new Font(1, 6);

            for (DepartmentDataDto paymentData : data.getPaymentDataList()) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(paymentData.getDepartment(),cellFont));
                PdfPCell cell2 = new PdfPCell(new Paragraph(paymentData.getName(),cellFont));
                table.addCell(cell1);
                table.addCell(cell2);

                PdfPCell cell3 = pdfCell(paymentData.getPaymentDtoList().get(0),cellFont);
                PdfPCell cell4 = pdfCell(paymentData.getPaymentDtoList().get(1),cellFont);
                PdfPCell cell5 = pdfCell(paymentData.getPaymentDtoList().get(2),cellFont);
                PdfPCell cell6 = pdfCell(paymentData.getPaymentDtoList().get(3),cellFont);
                PdfPCell cell7 = pdfCell(paymentData.getPaymentDtoList().get(4),cellFont);
                PdfPCell cell8 = pdfCell(paymentData.getPaymentDtoList().get(5),cellFont);
                PdfPCell cell9 = pdfCell(paymentData.getPaymentDtoList().get(6),cellFont);
                PdfPCell cell10 = pdfCell(paymentData.getPaymentDtoList().get(7),cellFont);
                PdfPCell cell11 = pdfCell(paymentData.getPaymentDtoList().get(8),cellFont);
                PdfPCell cell12 = pdfCell(paymentData.getPaymentDtoList().get(9),cellFont);
                PdfPCell cell13 = pdfCell(paymentData.getPaymentDtoList().get(10),cellFont);
                PdfPCell cell14 = pdfCell(paymentData.getPaymentDtoList().get(11),cellFont);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
                table.addCell(cell7);
                table.addCell(cell8);
                table.addCell(cell9);
                table.addCell(cell10);
                table.addCell(cell11);
                table.addCell(cell12);
                table.addCell(cell13);
                table.addCell(cell14);
            }

            doc.add(table);
            doc.close();
            pdfWriter.close();

        } catch (Exception e) {
            log.error("Error generating PDF document. {} - {}", e.getMessage(), e.getStackTrace());
            throw e;
        }
    }*/

    public void annualPaymentReport(AnnualPaymentReportDto data) throws FileNotFoundException {
        Document doc = new Document();
        doc.setPageSize(PageSize.A4.rotate());
        try {
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./annual_report_building_"+data.getBuilding()+"_"+data.getYear()+".pdf"));
            final PdfWriter pdfWriter = PdfWriter.getInstance(doc, pdfOutputFile);
            doc.open();

            String title = "EDIFICIO ".concat(data.getBuilding());
            Font titleFont = new Font(Font.ITALIC, 12, Font.UNDERLINE, Color.BLACK);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(titleParagraph);
            doc.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table = new PdfPTable(14);
            table.setWidthPercentage(100);
            table.setWidths(new float[] {4.142F, 10.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F, 7.142F});

            Font titleColumn = new Font(1, 6);

            PdfPCell title1 = new PdfPCell(new Paragraph("DEPTO", titleColumn));
            PdfPCell title2 = new PdfPCell(new Paragraph("NOMBRE", titleColumn));
            PdfPCell title3 = new PdfPCell(new Paragraph("ENERO", titleColumn));
            PdfPCell title4 = new PdfPCell(new Paragraph("FEBRERO", titleColumn));
            PdfPCell title5 = new PdfPCell(new Paragraph("ABRIL", titleColumn));
            PdfPCell title6 = new PdfPCell(new Paragraph("MARZO", titleColumn));
            PdfPCell title7 = new PdfPCell(new Paragraph("MAYO", titleColumn));
            PdfPCell title8 = new PdfPCell(new Paragraph("JUNIO", titleColumn));
            PdfPCell title9 = new PdfPCell(new Paragraph("JULIO", titleColumn));
            PdfPCell title10 = new PdfPCell(new Paragraph("AGOSTO", titleColumn));
            PdfPCell title11 = new PdfPCell(new Paragraph("SEPTIEMBRE", titleColumn));
            PdfPCell title12 = new PdfPCell(new Paragraph("OCTUBRE", titleColumn));
            PdfPCell title13 = new PdfPCell(new Paragraph("NOVIEMBRE", titleColumn));
            PdfPCell title14 = new PdfPCell(new Paragraph("DICIEMBRE", titleColumn));

            table.addCell(title1);
            table.addCell(title2);
            table.addCell(title3);
            table.addCell(title4);
            table.addCell(title5);
            table.addCell(title6);
            table.addCell(title7);
            table.addCell(title8);
            table.addCell(title9);
            table.addCell(title10);
            table.addCell(title11);
            table.addCell(title12);
            table.addCell(title13);
            table.addCell(title14);

            Font cellFont = new Font(1, 6);

            for (DepartmentDataDto paymentData : data.getPaymentDataList()) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(paymentData.getDepartment(), cellFont));
                PdfPCell cell2 = new PdfPCell(new Paragraph(paymentData.getName(), cellFont));
                table.addCell(cell1);
                table.addCell(cell2);

                // Ordena la lista paymentDtoList basada en el atributo "month"
                if (paymentData.getPaymentDtoList() == null)
                    paymentData.setPaymentDtoList(new ArrayList<>());

                log.info("Before paymentData.getPaymentDtoList() {}", paymentData.getPaymentDtoList());
                Collections.sort(paymentData.getPaymentDtoList(), new PaymentReportDtoComparator());
                log.info("After paymentData.getPaymentDtoList() {}", paymentData.getPaymentDtoList());

                log.info("PaymentData name:  {}", paymentData.getName());
                /*for (int i = 0; i < 12; i++) {
                    if (i < paymentData.getPaymentDtoList().size()) {
                        PdfPCell cell = pdfCell(paymentData.getPaymentDtoList().get(i), cellFont);
                        table.addCell(cell);
                    } else {
                        // Agregar una celda vacía
                        PdfPCell emptyCell = new PdfPCell();
                        table.addCell(emptyCell);
                    }
                }*/


                boolean isMonthInTheArray = false;
                for (int i = 0; i < 12; i++) {
                    isMonthInTheArray = false;
                    for (int j=0; j < paymentData.getPaymentDtoList().size(); j++) {
                        if (paymentData.getPaymentDtoList().get(j).getMonthNumber() == i ) {
                            log.info("J = {} I = {}", paymentData.getPaymentDtoList().get(j).getMonthNumber(), i);
                            PdfPCell cell = pdfCell(paymentData.getPaymentDtoList().get(j), cellFont);
                            table.addCell(cell);
                            isMonthInTheArray = true;
                            break;
                        }
                    }
                    if(!isMonthInTheArray){
                        PdfPCell emptyCell = new PdfPCell();
                        table.addCell(emptyCell);
                    }
                }
                log.info("Table {}", table.size());
            }

            doc.add(table);
            doc.close();
            pdfWriter.close();
        } catch (Exception e) {
            log.error("Error generating PDF document. {} - {}", e.getMessage(), e.getStackTrace());
            throw e;
        }
    }

    private PdfPCell pdfCell(PaymentReportDto data, Font cellFont){
        Format dateFormat = new SimpleDateFormat("MM/dd/yy");

        if(data.isEnabled()){
            return new PdfPCell(new Paragraph("Folio: "+data.getFolio()+"\n"+"Fecha: "+data.getDatePayment(),cellFont));
        }else {
            return new PdfPCell(new Paragraph(""));
        }
    }

    public void payrollReport(PayrollReportDto data) throws FileNotFoundException {
        Document doc = new Document();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data.getDate());

        // Obtener el mes y el año por separado
        int mounth = calendar.get(Calendar.MONTH) + 1; //
        int year = calendar.get(Calendar.YEAR);

        try {
            String fortnight = data.isFirstFortnight()? "1": "2";
            FileOutputStream pdfOutputFile = new FileOutputStream(CONSTANS.DIRECTORY.concat("./quincena_"+fortnight+"_"+mounth+"_"+year+".pdf"));
            final PdfWriter pdfWriter = PdfWriter.getInstance(doc, pdfOutputFile);
            doc.open();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));

            // Formatear la fecha en español
            String dateFormatted = dateFormat.format(data.getDate());

            String payrollDate= data.isFirstFortnight()? "PRIMERA": "SEGUNDA";
            String title = "NÓMINA ".concat(payrollDate).concat(" QUINCENA").concat(" DE ").concat(dateFormatted .toUpperCase());
            Font titleFont = new Font(Font.ITALIC, 18, Font.UNDERLINE, Color.BLACK);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(titleParagraph);
            doc.add(new Paragraph(Chunk.NEWLINE));
            doc.add(new Paragraph(Chunk.NEWLINE));

            String paymentReport = "Recibo de pago";
            Font textFont = new Font(Font.HELVETICA, 12);
            Double totalAmount = 0.0;
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            if (data.getPayrollData() == null || data.getPayrollData().isEmpty()) {
                String payrollDescription = "No hay pagos registrados para esta quincena";
                Paragraph descriptionParagraph = new Paragraph(payrollDescription, textFont);
                doc.add(descriptionParagraph);
                doc.add(new Paragraph(Element.CHUNK));
            }
            else {
                for (PayrollData payrollData : data.getPayrollData()) {

                    Paragraph paymetParagraph = new Paragraph(paymentReport, textFont);
                    doc.add(paymetParagraph);
                    doc.add(new Paragraph(Element.CHUNK));

                    String payrollAmount = "Bueno por: $" + payrollData.getAmount().toString();
                    Paragraph amountParagraph = new Paragraph(payrollAmount, textFont);
                    doc.add(amountParagraph);
                    doc.add(new Paragraph(Element.CHUNK));

                    String name = payrollData.getName() != null ? payrollData.getName() : "(desconocido)";
                    String workstation = payrollData.getWorkstation() != null ? payrollData.getWorkstation() : "(desconocido)";
                    String payrollDescription = "Efectuado a ".concat(name).concat(" por desempeñar la posicion de ".concat(workstation));
                    Paragraph descriptionParagraph = new Paragraph(payrollDescription, textFont);
                    doc.add(descriptionParagraph);
                    doc.add(new Paragraph(Element.CHUNK));
                    totalAmount += payrollData.getAmount();
                    doc.add(linebreak);
                }
            }

            doc.add(linebreak);
            String totalAmountText = "Total pagado en la quincena: $"+totalAmount.toString();
            Paragraph totalAmountParagraph = new Paragraph(totalAmountText, textFont);
            totalAmountParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(totalAmountParagraph);

            doc.close();
            pdfWriter.close();
        }catch(Exception e) {
            log.error("Error generating PDF document. {} - {}", e.getMessage(), e.getStackTrace());
            throw e;
        }
    }
}


