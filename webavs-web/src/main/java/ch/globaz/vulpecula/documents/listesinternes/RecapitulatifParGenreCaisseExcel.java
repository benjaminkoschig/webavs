package ch.globaz.vulpecula.documents.listesinternes;

import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.listesinternes.RecapitulatifParGenreCaisse.Entry;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class RecapitulatifParGenreCaisseExcel extends AbstractRecapitulatifListExcel {
    private final HSSFCellStyle styleHeader;
    private final HSSFCellStyle styleTitle;
    private final HSSFCellStyle styleTotal;
    private final HSSFCellStyle styleEven;
    private final HSSFCellStyle styleOdd;
    private final HSSFCellStyle styleEvenText;
    private final HSSFCellStyle styleOddText;

    private final Annee annee;

    private final RecapitulatifParGenreCaisse recap;

    public RecapitulatifParGenreCaisseExcel(BSession session, String docName, String title, Annee annee,
            RecapitulatifParGenreCaisse recap) {
        super(session, docName, title);
        styleTitle = styleTitle();
        styleHeader = styleHeader();
        styleTotal = styleHeader();
        styleTotal.setDataFormat(format.getFormat(NUMERIC));

        styleEven = styleMontantCot();
        styleEvenText = styleMontantCot();
        styleEvenText.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));

        styleOdd = styleMontantCot();
        styleOdd.setFillForegroundColor(HSSFColor.WHITE.index);
        styleOddText = styleMontantCot();
        styleOddText.setFillForegroundColor(HSSFColor.WHITE.index);
        styleOddText.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));

        this.annee = annee;
        this.recap = recap;
    }

    @Override
    public void createContent() {
        try {
            createSheet();
            createRow();
            createMergedRegion(0, 4, 0, 0, getDocumentTitle(), styleTitle);
            createRow();
            createMergedRegion(2, "de février à janvier " + (annee.getValue() + 1), styleHeader);
            createCell("Comptes", styleHeader);
            createCell("Cotisations", styleHeader);
            createCell("Masse salariale", styleHeader);

            int i = 1;
            for (Entry line : recap.entries()) {
                String key = line.getLibelle();
                String comptes = StringUtils.join(line.getComptes(), '\n');
                Montant cotisations = line.getCotisations();
                Montant ms = line.getMasseSalariale();

                HSSFCellStyle style = i % 2 == 0 ? styleEven : styleOdd;
                HSSFCellStyle styleText = i % 2 == 0 ? styleEvenText : styleOddText;

                createRow();
                createCell(i, styleText);
                createCell(key, styleText);
                createCell(comptes, styleText);
                createCell(cotisations, style);
                createCell(ms, style);

                i++;
            }

            createRow();
            createCell("", styleHeader);
            createCell("Total", styleHeader);
            createCell("", styleHeader);
            createCellFormula("SUM(D2:" + joinRowsForCols(3, getRowNum() - 1) + ")", styleTotal);
            createCellFormula("SUM(E2:" + joinRowsForCols(4, getRowNum() - 1) + ")", styleTotal);
        } catch (RuntimeException e) {
            JadeLogger.error(e, e.getMessage()); // at least we print a stacktrace... it's not that much, but could save your ass in a
                                                 // bad day...
            throw e;
        }
    }

    private void createSheet() {
        createSheet("Récap " + annee);
        getCurrentSheet().setColumnWidth((short) 0, (short) 3000);
        getCurrentSheet().setColumnWidth((short) 1, (short) 10000);
        getCurrentSheet().setColumnWidth((short) 2, (short) 6000);
        getCurrentSheet().setColumnWidth((short) 3, (short) 10000);
        getCurrentSheet().setColumnWidth((short) 4, (short) 10000);
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_INTERNES_RECAP_GENRE_CAISSE_TYPE_NUMBER;
    }

}
