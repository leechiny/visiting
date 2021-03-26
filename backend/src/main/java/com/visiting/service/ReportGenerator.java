package com.visiting.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.visiting.model.VisitorEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ReportGenerator {

  @Autowired
  VisitingService visitingService;

  SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
  SimpleDateFormat asAtDateFormat = new SimpleDateFormat("d MMM yyyy");
  SimpleDateFormat asAtTimeFormat = new SimpleDateFormat("hh:mm a");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  Font font = FontFactory.getFont(FontFactory.TIMES, 11, BaseColor.BLACK);

  public File writeFile(Timestamp generateDate) throws IOException, DocumentException {

    File tempFile = File.createTempFile("visitor", ".tmp");

    Document document = new Document(PageSize.A4, 10, 10, 10, 20);
    PdfWriter.getInstance(document, new FileOutputStream(tempFile));

    document.open();
    Paragraph title = new Paragraph("Visitor Log for " + asAtDateFormat.format(new Date()) + " (as at " + asAtTimeFormat.format(new Date()) + ")");
    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(20f);
    title.setSpacingBefore(20f);
    document.add(title);

    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(100f);
    table.setWidths( new float[]  {1,3,4,2,2} );
    addPdfHeader(table);

    List<VisitorEntry> visits;

    int i = 0;
    int cnt = 0;
    do {
      visits = visitingService.getVisitForDate(generateDate, i++, 10);

      for (VisitorEntry v : visits) {
        cnt++;
        addPdfVisitLog(table, v, cnt);
      }
    } while (!visits.isEmpty());

    document.add(table);
    document.close();

    PdfReader reader = new PdfReader(new FileInputStream(tempFile.getAbsolutePath()));
    File resourceFile = File.createTempFile("vis-", ".pdf");
    int n = reader.getNumberOfPages();
    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(resourceFile));
    PdfContentByte pagecontent;
    for (i = 0; i < n; ) {
      pagecontent = stamper.getOverContent(++i);
      ColumnText.showTextAligned(pagecontent, Element.ALIGN_LEFT, new Phrase(String.format("Page %s of %s", i, n)), 9, 6, 0);
    }
    stamper.close();
    reader.close();

    tempFile.delete();
    resourceFile.deleteOnExit();

    return resourceFile;
  }

  private void addPdfHeader(PdfPTable pdfPTable) {
    Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, BaseColor.BLACK);
    String[] title = { "No", "Entry Date/Time", "Name", "NRIC (Last 4)", "Phone Number"};

    IntStream.range(0, title.length).forEach(i -> {
      PdfPCell header = new PdfPCell();
      header.setBackgroundColor(BaseColor.LIGHT_GRAY);
      Phrase phrase = new Phrase(title[i],
        font);
      header.setPhrase(phrase);
      pdfPTable.addCell(header);
    });
  }

  private void addPdfVisitLog(PdfPTable pdfPTable, VisitorEntry visit, int idx) {
    PdfPCell cell = new PdfPCell();
    cell.setMinimumHeight(30);
    cell.setPhrase( new Phrase(Integer.toString(idx), font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pdfPTable.addCell(cell);

    cell = new PdfPCell();
    cell.setPhrase( new Phrase(dateFormat.format(visit.getTimestamp()), font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pdfPTable.addCell(cell);

    cell = new PdfPCell();
    cell.setPhrase( new Phrase(visit.getVisitor().getName(), font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pdfPTable.addCell(cell);

    cell = new PdfPCell();
    cell.setPhrase( new Phrase(visit.getVisitor().getMaskedNric(), font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pdfPTable.addCell(cell);

    cell = new PdfPCell();
    cell.setPhrase( new Phrase(visit.getVisitor().getPhoneNo(), font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pdfPTable.addCell(cell);

    pdfPTable.completeRow();
  }

}
