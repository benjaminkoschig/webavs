package ch.globaz.vulpecula.documents.listesinternes;

import globaz.globall.db.BSession;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess.Caisse;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class RecapitulatifParConventionParCaisseExcel extends AbstractRecapitulatifListExcel {
    private final HSSFCellStyle styleTitle;
    private final HSSFCellStyle styleHeader;
    private final HSSFCellStyle styleTotal;
    private final HSSFCellStyle styleNoTaux;
    private final HSSFCellStyle styleEven;
    private final HSSFCellStyle styleOdd;

    private final RecapitulatifParConventionParCaisse recap;

    public RecapitulatifParConventionParCaisseExcel(BSession session, String docName, String title,
            RecapitulatifParConventionParCaisse recap) {
        super(session, docName, title);
        styleTitle = styleTitle();
        styleHeader = styleHeader();
        styleTotal = styleHeader();
        styleTotal.setDataFormat(format.getFormat(NUMERIC));

        styleNoTaux = styleMontantCot();
        styleNoTaux.setFillForegroundColor(HSSFColor.ORANGE.index);

        styleEven = styleMontantCot();
        styleOdd = styleMontantCot();
        styleOdd.setFillForegroundColor(HSSFColor.WHITE.index);

        this.recap = recap;
    }

    @Override
    public void createContent() {
        try {
            createSheet("Récap");
            createRow();
            createMergedRegion(0, 3, 0, 0, getDocumentTitle(), styleTitle);
            createRow();

            for (Map.Entry<Convention, Map<Caisse, CaisseRecapitulatifs>> entry : recap.entrySet()) {
                createCell(entry.getKey().getDesignation(), styleHeader);
                createCell("Contributions", styleHeader);
                createCell("Taux", styleHeader);
                createCell("Salaires", styleHeader);
                createRow();

                int i = 0;

                int from = getRowNum();

                for (Map.Entry<Caisse, CaisseRecapitulatifs> ck : entry.getValue().entrySet()) {
                    Caisse key = ck.getKey();
                    CaisseRecapitulatifs value = ck.getValue();

                    Taux taux = value.getTaux();
                    HSSFCellStyle style = taux == null ? styleNoTaux : (i % 2 == 0 ? styleEven : styleOdd);

                    createCell(key.getLibelle(), style);

                    Montant contrib = value.getContributions();
                    createCell(contrib, style);

                    createCell(taux == null ? "" : taux.getValue() + '%', style);

                    Montant masse = value.getMasse();
                    createCell(masse, style);

                    createRow();
                    i++;
                }

                createCell("Total " + entry.getKey().getDesignation(), styleTotal);
                createCellFormula("SUM(" + joinRowsForCols(1, from) + ":" + joinRowsForCols(1, getRowNum() - 1) + ")",
                        styleTotal);
                createCell("", styleTotal);
                createCellFormula("SUM(" + joinRowsForCols(3, from) + ":" + joinRowsForCols(3, getRowNum() - 1) + ")",
                        styleTotal);

                createRow();
                createRow();
            }

            autosizeAllColumns(getCurrentSheet());
        } catch (RuntimeException e) {
            e.printStackTrace(); // at least we have a stacktrace...
            throw e;
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_INTERNES_RECAP_CAISSE_CONVENTION_TYPE_NUMBER;
    }
}
