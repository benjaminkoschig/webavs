package ch.globaz.vulpecula.documents.listesinternes;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.listesinternes.CaisseKey.Type;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class RecapitulatifParConventionExcel extends AbstractRecapitulatifListExcel {

    private final HSSFCellStyle styleTitle;
    private final HSSFCellStyle styleHeader;
    private final HSSFCellStyle styleIdExterne;
    private final HSSFCellStyle styleCot;
    private final HSSFCellStyle styleMS;
    private final HSSFCellStyle styleMontantCot;
    private final HSSFCellStyle styleMontantMS;

    private final Annee annee;

    private final RecapitulatifParConvention recapitulatifParConvention;

    public RecapitulatifParConventionExcel(BSession session, String docName, String title, Annee annee,
            RecapitulatifParConvention recapitulatifParConvention) {
        super(session, docName, title);
        styleTitle = styleTitle();
        styleHeader = styleHeader();
        styleIdExterne = styleIdExterne();
        styleMS = styleMS();
        styleCot = styleCot();
        styleMontantCot = styleMontantCot();
        styleMontantMS = styleMontantMs();

        this.annee = annee;
        this.recapitulatifParConvention = recapitulatifParConvention;
    }

    private void createSheet(Convention convention) {
        HSSFSheet sheet = createSheet(convention.getCode() + " - " + convention.getDesignation());
        sheet.getPrintSetup().setLandscape(true);
    }

    @Override
    public void createContent() {
        try {
            List<Integer> rowsTotalMs = new ArrayList<Integer>();
            List<Integer> rowsTotalCot = new ArrayList<Integer>();

            for (Entry<Convention, Map<CaisseKey, PeriodeRecapitulatifs>> entry : recapitulatifParConvention.entrySet()) {
                Convention convention = entry.getKey();
                createSheet(convention);
                createRow();
                createMergedRegion(0, 14, 0, 0, getDocumentTitle() + " " + convention.getDesignation(), styleTitle);
                createRow();
                createCell("Caisse", styleHeader);
                createEmptyCell();
                createCell("Février " + annee.getValue(), styleHeader);
                createCell("Mars", styleHeader);
                createCell("Avril", styleHeader);
                createCell("Mai", styleHeader);
                createCell("Juin", styleHeader);
                createCell("Juillet", styleHeader);
                createCell("Août", styleHeader);
                createCell("Septembre", styleHeader);
                createCell("Octobre", styleHeader);
                createCell("Novembre", styleHeader);
                createCell("Décembre", styleHeader);
                createCell("Janvier " + annee.next().getValue(), styleHeader);
                createCell("Total", styleHeader);

                List<Integer> rowsCot = new ArrayList<Integer>();
                List<Integer> rowsMs = new ArrayList<Integer>();

                int i = 0;
                for (Entry<CaisseKey, PeriodeRecapitulatifs> entry2 : entry.getValue().entrySet()) {
                    createRow();
                    if (i % 2 == 0) {
                        createCell(entry2.getKey().getLibelle(), styleHeader);
                    } else {
                        createCell(entry2.getKey().getIdExterne(), styleIdExterne);
                    }

                    CaisseKey caisse = entry2.getKey();
                    if (Type.COT.equals(caisse.getType())) {
                        createCell("COT", styleCot);
                        rowsCot.add(getRowNum());
                    } else {
                        createCell("MS", styleMS);
                        rowsMs.add(getRowNum());
                    }

                    int from = getColNum();
                    PeriodeRecapitulatifs periode = entry2.getValue();
                    if (periode != null) {
                        for (Date date : periode.getDates()) {
                            Montant montant = i % 2 == 0 ? recapitulatifParConvention.getMontant(convention, caisse,
                                    date) : recapitulatifParConvention.getMasse(convention, caisse, date);
                            createCell(montant, i % 2 == 0 ? styleMontantCot : styleMontantMS);
                        }
                    }

                    createCellFormula(
                            "SUM(" + joinRowsForCols(from, getRowNum()) + ":"
                                    + joinRowsForCols(getColNum() - 1, getRowNum()) + ")", i % 2 == 0 ? styleMontantCot
                                    : styleMontantMS);

                    i++;
                }
                createRow();
                createCell("TOTAL", styleHeader);
                createCell("COT", styleCot);
                for (i = 0; i < 13; i++) {
                    createCellFormula(
                            "SUM(" + joinRowsForCols(getColNum(), rowsCot.toArray(new Integer[rowsCot.size()])) + ")",
                            styleMontantCot);
                }
                rowsTotalCot.add(getRowNum());
                createRow();
                createCell("", styleHeader);
                createCell("MS", styleMS);

                for (i = 0; i < 13; i++) {
                    createCellFormula("SUM(" + joinRowsForCols(getColNum(), rowsMs.toArray(new Integer[rowsMs.size()]))
                            + ")", styleMontantMS);
                }
                rowsTotalMs.add(getRowNum());

                createMergedRegion(0, 0, getRowNum() - 1, getRowNum(), "TOTAL", styleHeader);
                autosizeAllColumns(currentSheet);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_INTERNES_RECAP_CONVENTION_TYPE_NUMBER;
    }
}
