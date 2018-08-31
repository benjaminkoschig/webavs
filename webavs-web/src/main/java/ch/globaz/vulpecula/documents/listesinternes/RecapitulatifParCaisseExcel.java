package ch.globaz.vulpecula.documents.listesinternes;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.listesinternes.CaisseKey.Type;
import ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess.Caisse;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class RecapitulatifParCaisseExcel extends AbstractRecapitulatifListExcel {

    private static final String CAISSE = "Caisse";
    private final HSSFCellStyle styleTitle;
    private final HSSFCellStyle styleHeader;
    private final HSSFCellStyle styleHeaderVertical;
    private final HSSFCellStyle styleCot;
    private final HSSFCellStyle styleMS;
    private final HSSFCellStyle styleMontantCot;
    private final HSSFCellStyle styleMontantMS;

    private final Annee annee;

    private final RecapitulatifParCaisse recap;

    public RecapitulatifParCaisseExcel(BSession session, String docName, String title, Annee annee,
            RecapitulatifParCaisse recap) {
        super(session, docName, title);

        setWantHeader(false);
        setWantFooter(false);

        styleTitle = styleTitle();
        styleHeader = styleHeader();
        styleHeaderVertical = styleHeader();
        styleHeaderVertical.setRotation((short) 90);

        styleMS = styleMS();
        styleCot = styleCot();
        styleMontantCot = styleMontantCot();
        styleMontantMS = styleMontantMs();

        this.annee = annee;
        this.recap = recap;
    }

    @Override
    public void createContent() {
        try {
            for (Map.Entry<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> entry : recap.entrySet()) {
                String typeLibelle = getSession().getCodeLibelle(entry.getKey());
                createSheet(typeLibelle.replace('\\', '-').replace('/', '-'));
                createRow();
                createMergedRegion(0, 15, 0, 0, getDocumentTitle() + " " + typeLibelle, styleTitle);
                createRow();

                List<Integer> rowsTotalMs = new ArrayList<Integer>();
                List<Integer> rowsTotalCot = new ArrayList<Integer>();

                for (Entry<Caisse, Map<Convention, PeriodeRecapitulatifs>> entry2 : entry.getValue().entrySet()) {
                    Caisse caisse = entry2.getKey();

                    setCurrentCell(0);
                    createCell(CAISSE, styleHeader);
                    createCell("Convention", styleHeader);
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
                    createRow();

                    int rowToMergeCaisse = getRowNum();

                    List<Integer> rowsMs = new ArrayList<Integer>();
                    List<Integer> rowsCot = new ArrayList<Integer>();

                    for (Entry<Convention, PeriodeRecapitulatifs> convAndPeriode : entry2.getValue().entrySet()) {
                        Convention convention = convAndPeriode.getKey();

                        setCurrentCell(0);
                        setColNum(2);

                        int rowToMergeConvention = getRowNum();
                        int i = 0;

                        for (CaisseKey.Type type : CaisseKey.Type.values()) {
                            setCurrentCell(2);
                            createCell(type.toString(), type == Type.COT ? styleCot : styleMS);

                            PeriodeRecapitulatifs periode = convAndPeriode.getValue();
                            CaisseKey caisseKey = new CaisseKey(caisse.getIdExterne(), caisse.getLibelle(), type);

                            int from = getColNum();

                            for (Date date : periode.getDates()) {
                                Montant montant = recap.getMontant(entry.getKey(), convention, caisseKey, date);
                                createCell(montant, type == Type.COT ? styleMontantCot : styleMontantMS);
                            }

                            createCellFormula(
                                    "SUM(" + joinRowsForCols(from, getRowNum()) + ":"
                                            + joinRowsForCols(getColNum() - 1, getRowNum()) + ")",
                                    type == Type.COT ? styleMontantCot : styleMontantMS);

                            if (type == Type.COT) {
                                rowsCot.add(getRowNum());
                            } else {
                                rowsMs.add(getRowNum());
                            }

                            i++;

                            if (i < CaisseKey.Type.values().length) {
                                createRow();
                            }
                        }

                        // ------------------------
                        String desi = convention.getCode() + '\n' + convention.getDesignation();
                        int oldRow = getRowNum();
                        setColNum(1);
                        setCurrentRow(rowToMergeConvention);
                        createCell(desi, styleHeader);
                        createMergedRegion(1, 1, rowToMergeConvention, oldRow, desi, styleHeader);
                        setCurrentRow(oldRow);

                        createRow();
                    }

                    // --------------
                    int rowToMergeTotal = getRowNum();

                    setColNum(1);

                    createCell("TOTAL", styleHeader);
                    createCell("COT", styleCot);

                    for (int i = 0; i < 13; i++) {
                        createCellFormula(
                                "SUM(" + joinRowsForCols(getColNum(), rowsCot.toArray(new Integer[rowsCot.size()]))
                                        + ")", styleMontantCot);
                    }

                    rowsTotalCot.add(getRowNum());
                    createRow();

                    // --------------
                    setColNum(2);
                    createCell("MS", styleMS);
                    for (int i = 0; i < 13; i++) {
                        createCellFormula(
                                "SUM(" + joinRowsForCols(getColNum(), rowsMs.toArray(new Integer[rowsMs.size()])) + ")",
                                styleMontantMS);
                    }

                    rowsTotalMs.add(getRowNum());

                    createMergedRegion(1, 1, rowToMergeTotal, getRowNum(), "TOTAL", styleHeader);

                    // Merge le nom de la caisse --------------
                    String libelle = caisse.getLibelle();
                    int oldRow = getRowNum();
                    setColNum(0);
                    setCurrentRow(rowToMergeCaisse);
                    createCell(libelle, styleHeaderVertical);
                    createMergedRegion(0, 0, rowToMergeCaisse, oldRow, libelle, styleHeaderVertical);
                    setCurrentRow(oldRow);

                    createRow();
                }

                createCell("", styleHeader);
                createCell("TOTAL Général");

                createCell("COT", styleCot);
                for (int i = 0; i < 13; i++) {
                    createCellFormula(
                            "SUM("
                                    + joinRowsForCols(getColNum(),
                                            rowsTotalCot.toArray(new Integer[rowsTotalCot.size()])) + ")",
                            styleMontantCot);
                }

                createRow();

                createCell("", styleHeader);
                createCell("", styleHeader);
                createCell("MS", styleMS);
                for (int i = 0; i < 13; i++) {
                    createCellFormula(
                            "SUM(" + joinRowsForCols(getColNum(), rowsTotalMs.toArray(new Integer[rowsTotalMs.size()]))
                                    + ")", styleMontantMS);
                }

                createMergedRegion(0, 0, getRowNum() - 1, getRowNum(), "", styleHeader);
                createMergedRegion(1, 1, getRowNum() - 1, getRowNum(), "TOTAL Général", styleHeader);
                autosizeAllColumns(getCurrentSheet());
                // set the first column width to a better value...
                getCurrentSheet().setColumnWidth((short) 0, (short) ((CAISSE.length() + 1) * 256)/*
                                                                                                  * yea that's weird,
                                                                                                  * you set the column
                                                                                                  * width as n/256th of
                                                                                                  * a character width...
                                                                                                  */);
            }
        } catch (RuntimeException e) {
            e.printStackTrace(); // at least we have a stacktrace...
            throw e;
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_INTERNES_RECAP_CAISSE_TYPE_NUMBER;
    }
}
