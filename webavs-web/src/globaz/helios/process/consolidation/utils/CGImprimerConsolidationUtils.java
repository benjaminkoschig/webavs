package globaz.helios.process.consolidation.utils;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CGImprimerConsolidationUtils {

    /**
     * Ajout d'un header et d'un footer au document excel.
     * 
     * @param sheet
     * @param exerciceComptable
     * @param numeroReference
     */
    public static void addHeaderFooter(BSession session, HSSFSheet sheet,
            CGExerciceComptableViewBean exerciceComptable, String titleLabel, String numeroReference) {
        HSSFHeader header = sheet.getHeader();
        header.setCenter(session.getLabel(titleLabel) + " "
                + exerciceComptable.getDateDebut().substring(exerciceComptable.getDateDebut().lastIndexOf(".") + 1));

        HSSFFooter footer = sheet.getFooter();
        footer.setRight(session.getLabel("PAGE") + " " + HSSFFooter.page() + "/" + HSSFFooter.numPages());
        footer.setLeft(session.getLabel("REFERENCE") + " : " + numeroReference);
    }

    /**
     * Ajout des informations sur les paramètres (mandat et date).
     * 
     * @param wb
     * @param sheet
     * @param exerciceComptable
     */
    public static void addParamsInfos(BSession session, HSSFWorkbook wb, HSSFSheet sheet,
            CGExerciceComptableViewBean exerciceComptable) {
        HSSFFont font = wb.createFont();
        font.setItalic(true);
        HSSFCellStyle styleParam = wb.createCellStyle();
        styleParam.setFont(font);

        HSSFRow rowParam = sheet.createRow((short) 0);
        HSSFCell cellParam = rowParam.createCell((short) 0);
        cellParam.setCellValue(session.getLabel("MANDAT") + " :");

        cellParam = rowParam.createCell((short) 1);
        cellParam.setCellValue(exerciceComptable.getMandat().getLibelle());
        cellParam.setCellStyle(styleParam);

        rowParam = sheet.createRow((short) 1);
        cellParam = rowParam.createCell((short) 0);
        cellParam.setCellValue(session.getLabel("DATE") + " :");

        cellParam = rowParam.createCell((short) 1);
        cellParam.setCellValue(JACalendar.todayJJsMMsAAAA());
        cellParam.setCellStyle(styleParam);
    }

    /**
     * Créer le fichier output.
     * 
     * @param wb
     * @param output
     * @throws Exception
     */
    public static void createFile(HSSFWorkbook wb, String output) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(output);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * Mise à jour de la taille des colonnes du contenu. Calculé sur 1 milliard.
     * 
     * @param sheet
     * @param fromColum
     * @param toColumn
     */
    public static void setContentColumnWidth(HSSFSheet sheet, int fromColum, int toColumn) {
        for (int i = fromColum; i <= toColumn; i++) {
            sheet.setColumnWidth((short) i, (short) ("1'000'000'000.00".length() * 256));
        }
    }

}
