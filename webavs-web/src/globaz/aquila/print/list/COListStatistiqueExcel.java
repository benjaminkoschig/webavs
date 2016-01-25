package globaz.aquila.print.list;

import globaz.aquila.db.access.suiviprocedure.COStatistique;
import globaz.aquila.db.access.suiviprocedure.COStatistiqueManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class COListStatistiqueExcel extends COAbstractListExcel {

    private final static String NUMERO_REFERENCE_INFOROM = "0273GCO";

    private String fromDate;
    private String untilDate;

    /**
     * @param session
     */
    public COListStatistiqueExcel(BSession session) {
        super(session, "ListStatContentieux", session.getLabel("LIST_STAT_TITLE"));
    }

    /**
     * @return
     */
    public String getFromDate() {
        return fromDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.print.list.COAbstractListExcel#getNumeroInforom()
     */
    @Override
    public String getNumeroInforom() {
        return COListStatistiqueExcel.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
	 *
	 */
    private void initCritere() {
        if (!JadeStringUtil.isBlank(getFromDate())) {
            createRow();
            this.createCell(getSession().getLabel("PLAINTE_DU"), getStyleCritereTitle());
            this.createCell(getFromDate(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlank(getUntilDate())) {
            createRow();
            this.createCell(getSession().getLabel("PLAINTE_AU"), getStyleCritereTitle());
            this.createCell(getUntilDate(), getStyleCritere());
        }
    }

    /**
	 *
	 */
    private void initListe() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<String>();
        colTitles.add(getSession().getLabel("LIST_STAT_SEQUENCE"));
        colTitles.add(getSession().getLabel("LIST_STAT_ETAPE"));
        colTitles.add(getSession().getLabel("LIST_STAT_NOMBRE"));
        colTitles.add(getSession().getLabel("LIST_STAT_MONTANT"));
        colTitles.add(getSession().getLabel("LIST_STAT_TAXES"));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COListStatistiqueExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cellules
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, COAbstractListExcel.COLUMN_WIDTH_SECTION);
        currentSheet.setColumnWidth((short) numCol++, COAbstractListExcel.COLUMN_WIDTH_COMPTEANNEXE);
        currentSheet.setColumnWidth((short) numCol++, COAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) numCol++, COAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) numCol++, COAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(COStatistiqueManager manager, BTransaction transaction) throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        COStatistique stat = null;

        initListe();

        int numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;
        while (((stat = (COStatistique) manager.cursorReadNext(statement)) != null) && !stat.isNew()) {
            if (stat != null) {
                createRow();
                this.createCell(stat.getSequenceLibelle(), getStyleListLeft());
                this.createCell(stat.getEtapeLibelle(), getStyleListLeft());
                if (JadeStringUtil.isBlank(stat.getNbEtape())) {
                    this.createCell(Integer.parseInt("0"), getStyleMontant());
                } else {
                    this.createCell(Integer.parseInt(stat.getNbEtape()), getStyleListRight());
                }
                if (JadeStringUtil.isBlank(stat.getSumSolde())) {
                    this.createCell(Double.parseDouble("0"), getStyleMontant());
                } else {
                    this.createCell(Double.parseDouble(stat.getSumSolde()), getStyleMontant());
                }
                if (JadeStringUtil.isBlank(stat.getSumTaxeFrais())) {
                    this.createCell(Double.parseDouble("0"), getStyleMontant());
                } else {
                    this.createCell(Double.parseDouble(stat.getSumTaxeFrais()), getStyleMontant());
                }
            }
        }
        createRow();
        this.createCell("", getStyleCritere());
        this.createCell("", getStyleCritere());
        this.createCell("", getStyleCritere());
        createCellFormula("SUM(D" + numPremiereLigne + ":D" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());
        createCellFormula("SUM(E" + numPremiereLigne + ":E" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());

        return currentSheet;
    }

    /**
     * @param fromDate
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param untilDate
     */
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }
}
