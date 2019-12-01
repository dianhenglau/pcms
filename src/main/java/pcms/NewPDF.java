package pcms;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
public class NewPDF {
    
    public static void main(String args[]) throws IOException {
        new NewPDF().createPdf();
    }
    
    public void createPdf() throws IOException {
        /** Set PDF document path. */
        final String dest = "pdf/Depavali.pdf";
        File file = new File(dest);
        file.getParentFile().mkdirs();

 
        /** Initialize PDF document. */
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdf);
        
        /** Set default font. */
        PdfFont norm = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
        
        /** Display catalog info. */
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{2,5}), true);
        String path = "data/main/product_images/P00003-throw_pillow.jpg";
        String festival = "Deepavali";
        String seasonStartDate = "2019-11-21";
        String seasonEndDate = "2019-12-21";
        String info = "Price List of " + festival + " Sales 2019\n" + "Effective on: " + seasonStartDate + "-" + seasonEndDate;
        infoTable.addCell(getInfoCell(path));
        infoTable.addCell(getInfoCell(info, bold));
        doc.add(infoTable);
        
        float[] colWidths=new float[]{1,1,4,1,1,1};
        Table productTable = new Table(UnitValue.createPercentArray(colWidths), true);
        
        /** Add header to table. */
        String[] header = {"Product ID", "Image", "Name", "Retail Price", "Discount", "Discount Price"};
        for (String i : header) {
            productTable.addHeaderCell(new Cell()
                    .setKeepTogether(true)
                    .add(new Paragraph(new Text(i).setFont(bold)))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
 
        doc.add(productTable);
        
        String[] product = {
            "P00001", "data/main/product_images/P00001-pillow2.jpeg", "Jean Perry 100% Natural Cotton Pillow", "84.8", "20%", "67.84",
            "P00002", "data/main/product_images/P00003-throw_pillow.jpg", "Rivet Mudcloth-Inspired Decorative Throw Pillow, 12\" x 24\", Navy", "39.9", "20%", "31.92"};
        
        for (int i = 0; i < product.length; i++) {
            if (i % 6 == 0) {
                productTable.flush();
            } 
            
            if (i % 6 == 1){
                Image img = new Image(ImageDataFactory.create(product[i])); 
                productTable.addCell(img.setAutoScale(true));
            } else{
                productTable.addCell(getCell(product[i], norm));
            }   
        }
 
        productTable.complete();
 
        doc.close();
        
    } 
    public Cell getInfoCell(String text, PdfFont font){
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph(new Text(text).setFont(font));
        cell.add(paragraph.setTextAlignment(TextAlignment.LEFT));
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBorder(Border.NO_BORDER);
        cell.setKeepTogether(true);
        return cell;
    }
    
    public Cell getInfoCell(String path) throws MalformedURLException {
        Cell cell = new Cell()
                .add(new Image(ImageDataFactory.create(path))
                        .setHeight(100)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER))
                .setBorder(Border.NO_BORDER);
        return cell;
    }
    
    public Cell getCell(String text, PdfFont font){
        Cell cell = new Cell().add(new Paragraph(new Text(text).setFont(font)));
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setMargins(0, 0, 0, 0);
        cell.setKeepTogether(true);
        return cell;
    }
}

