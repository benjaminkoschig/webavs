/**
 * 
 */
package globaz.naos.listes.pdf.extraitDS;

import globaz.jade.common.Jade;
import globaz.naos.db.controleLpp.AFExtraitDS;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Classe permettant la création de l'extrait de salaire pour un affilié pour une année donnée
 * 
 * @author est
 */
public class AFListeExtraitDS {

    private static List<HSSFRow> listeRowSalarie = null;
    private static BaseFont font = null;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;

    public AFListeExtraitDS(List<AFExtraitDS> listeDS) {

        initFonts();

        wb = new HSSFWorkbook();
        sheet = wb.createSheet("Sheet");
        sheet.setColumnWidth((short) 1, (short) (256 * 27));

        fillHashMap(listeDS);

    }

    public void createListPDF() {
        try {
            createTableauSalarie("testExtraitSalaire");
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void initFonts() {
        try {
            font = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /***
     * Permet de remplir la hashmap avec une liste de AFExtraitDS
     * 
     * @param listeDS
     */
    private void fillHashMap(List<AFExtraitDS> listeDS) {
        listeRowSalarie = new ArrayList<HSSFRow>();

        for (int i = 0; i < listeDS.size(); i++) {
            HSSFRow row = sheet.createRow(i);
            row.setHeightInPoints(10 * 15);
            row.createCell((short) 0).setCellValue(listeDS.get(i).getNss());
            row.createCell((short) 1).setCellValue(listeDS.get(i).getNomSalarie());
            row.createCell((short) 2).setCellValue(listeDS.get(i).getMoisDebut() + " - " + listeDS.get(i).getMoisFin());
            row.createCell((short) 3).setCellValue(listeDS.get(i).getSalaire());
            row.createCell((short) 4).setCellValue(listeDS.get(i).getSeuilEntree());

            listeRowSalarie.add(i, row);
        }
    }

    /**
     * Creation du tanleau récapitulatif des salariés
     * 
     * @param fileName
     * @param listeSalarie
     * @throws DocumentException
     * @throws IOException
     */
    private static void createTableauSalarie(String fileName) throws DocumentException, IOException {
        // nouveau document pdf
        Document out = new Document(PageSize.LETTER.rotate());

        // // création des fonts, standard et header (gras)
        Font tableStandardFont = new Font(font);
        tableStandardFont.setSize(8);
        tableStandardFont.setColor(Color.BLACK);

        Font tableHeaderFont = new Font(font);
        tableHeaderFont.setSize(8);
        tableHeaderFont.setStyle("bold");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Construction nom fichier et path
        String path = Jade.getInstance().getHomeDir() + "work/" + fileName + ".pdf";

        PdfWriter.getInstance(out, new FileOutputStream(path));

        out.open();

        int cpt = 1;

        // Debut de la feuille , 1 feuille, une par années
        out.newPage();
        // *********** en tete
        // out.add(createEnteteFeuilleTable(tableHeaderFont, cpt));
        // *********** Titre feuille
        // out.add(createTitreFeuilleTable(tableHeaderFont));
        // *********** Bloc employeur
        // out.add(createBlocEmployeurFeuilleTable(tableHeaderFont, tableStandardFont, annee,
        // listeSalarie.get(annee)));
        // ********** Bloc salarié, 1 par année
        out.add(createBlocSalarieFeuilleTable(tableHeaderFont, tableStandardFont));
        cpt++;

        // ********** Fin de la feuille
        out.close();
    }

    /**
     * Création du bloc salarié
     * 
     * @param header
     * @param standard
     * @param listeSalarie
     * @return
     */
    private static PdfPTable createBlocSalarieFeuilleTable(Font header, Font standard) {

        // tableau de 8 colones
        PdfPTable tableau = new PdfPTable(8);
        tableau.setWidthPercentage(100.00f);
        // ligne employeur
        PdfPCell cell = new PdfPCell(new Phrase("Salarié(s):", header));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(8);
        tableau.addCell(cell);

        // en tete du tableau
        // nss
        cell = new PdfPCell(new Phrase("NSS", header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // nom
        cell = new PdfPCell(new Phrase("Nom salarié", header));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableau.addCell(cell);

        // Période
        cell = new PdfPCell(new Phrase("Période", header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // montant salaire
        cell = new PdfPCell(new Phrase("Salaire AVS", header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // Deduction
        cell = new PdfPCell(new Phrase("Seuil d'entrée dans la LPP", header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // Deduction
        cell = new PdfPCell(new Phrase("Déduction coodination", header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // montant soumis total
        cell = new PdfPCell(new Phrase("Montant soumis", header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // iteration sur les salarie

        for (int i = 0; i < listeRowSalarie.size(); i++) {
            HSSFRow ligne = listeRowSalarie.get(i);

            // String montant = new
            // String(ligne.getCell(ILPPConstantes.XLS_MONTANT_SALARIE_COLUMN).getStringCellValue());

            // nss - colonne 0
            HSSFCell nssCell = ligne.getCell((short) 0);

            // Si nss pas null
            if (nssCell != null) {
                cell = new PdfPCell(new Phrase(nssCell.getStringCellValue(), standard));
            } else {
                cell = new PdfPCell(new Phrase(""));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableau.addCell(cell);

            // nom - colonne 1
            cell = new PdfPCell(new Phrase(ligne.getCell((short) 1).getStringCellValue(), standard));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableau.addCell(cell);

            // période - colonne 2
            cell = new PdfPCell(new Phrase(ligne.getCell((short) 2).getStringCellValue(), standard));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableau.addCell(cell);

            // montant salaire - colonne 3
            cell = new PdfPCell(new Phrase(ligne.getCell((short) 3).getStringCellValue(), standard));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableau.addCell(cell);

            // seuil entrée LPP - colonne 4
            cell = new PdfPCell(new Phrase(ligne.getCell((short) 4).getStringCellValue(), standard));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableau.addCell(cell);

            // ? - colonne 5
            cell = new PdfPCell(new Phrase("", standard));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableau.addCell(cell);

            // ? - colonne 6
            cell = new PdfPCell(new Phrase("", standard));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableau.addCell(cell);

        }
        return tableau;
    }
}