package globaz.aquila.print.list;

import globaz.aquila.db.access.plaintes.COPlainteForExcelList;
import globaz.aquila.db.access.plaintes.COPlainteForExcelListManager;
import globaz.aquila.db.access.plaintes.COPlaintePenale;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class COListPlainteExcel extends COAbstractListExcel {

    private final static String NUMERO_REFERENCE_INFOROM = "0235GCO";

    private String fromDate;
    private int totalIndependant = 0;
    private int totalParitaire = 0;

    private String untilDate;
    private String withoutEndDate;

    /**
     * @param session
     */
    public COListPlainteExcel(BSession session) {
        super(session, session.getLabel("PLAINTE_LIST_PLAINTE_TITLE"), session
                .getLabel("PLAINTE_LIST_PLAINTE_CONTENTIEUX_TITLE"));
    }

    public String getFromDate() {
        return fromDate;
    }

    @Override
    public String getNumeroInforom() {
        return COListPlainteExcel.NUMERO_REFERENCE_INFOROM;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public String getWithoutEndDate() {
        return withoutEndDate;
    }

    /**
	 *
	 */
    public void initBilan() {
        createRow();
        createRow();
        createRow();
        this.createCell("");
        this.createCell(getSession().getLabel("PLAINTE_TOTAL_PARITAIRE"), getStyleListRight());
        this.createCell(totalParitaire, getStyleListRight());
        createRow();
        this.createCell("");
        this.createCell(getSession().getLabel("PLAINTE_TOTAL_INDEPENDANT"), getStyleListRight());
        this.createCell(totalIndependant, getStyleListRight());
        createRow();
        this.createCell("");
        this.createCell(getSession().getLabel("PLAINTE_TOTAL_PLAINTE"), getStyleListTitleRight());
        this.createCell(totalIndependant + totalParitaire, getStyleListTitleRight());
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

        if ("on".equals(getWithoutEndDate())) {
            createRow();
            this.createCell(getSession().getLabel("PLAINTE_SANS_DATE_FIN"), getStyleCritereTitle());
            this.createCell(getSession().getLabel("PLAINTE_OUI"), getStyleCritere());
        } else {
            createRow();
            this.createCell(getSession().getLabel("PLAINTE_SANS_DATE_FIN"), getStyleCritereTitle());
            this.createCell(getSession().getLabel("PLAINTE_NON"), getStyleCritere());
        }
    }

    /**
	 *
	 */
    private void initListe() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(getSession().getLabel("NUMERO_ADMIN")); // N°Administrateur
        colTitles.add(getSession().getLabel("NOM_ADMIN")); // Nom Administrateur
        colTitles.add(getSession().getLabel("SOCIETE")); // Société
        colTitles.add(getSession().getLabel("DATE_PLAINTE")); // Date Plainte
        colTitles.add(getSession().getLabel("DESCRIPTION_PLAINTE")); // Description
        colTitles.add(getSession().getLabel("TYPE")); // Type
        colTitles.add(getSession().getLabel("MOTIF")); // Motif
        colTitles.add(getSession().getLabel("PLAINTE_DU")); // Du
        colTitles.add(getSession().getLabel("PLAINTE_AU")); // Au

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COListPlainteExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cellules
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Numéro Administrateur
        currentSheet.setColumnWidth((short) numCol++, (short) 20000); // Nom Administrateur
        currentSheet.setColumnWidth((short) numCol++, (short) 20000); // Société
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date Plainte
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Description
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // Type
        currentSheet.setColumnWidth((short) numCol++, (short) 8000); // Motif
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Du
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Au
    }

    /**
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(COPlainteForExcelListManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        COPlainteForExcelList plainte = null;

        initListe();

        while (((plainte = (COPlainteForExcelList) manager.cursorReadNext(statement)) != null) && !plainte.isNew()) {
            if (plainte != null) {

                if (COPlaintePenale.CS_PARITAIRE.equals(plainte.getType())) {
                    totalParitaire++;
                } else {
                    totalIndependant++;
                }

                createRow();
                this.createCell(plainte.getNumAdmin(), getStyleListLeft()); // numéro admin
                this.createCell(plainte.getNomAdmin(), getStyleListLeft()); // nom admin
                this.createCell(plainte.getSociete(), getStyleListLeft()); // société
                this.createCell(plainte.getDatePlainte(), getStyleListLeft()); // date de la plainte
                this.createCell(getSession().getCodeLibelle(plainte.getDescription()), getStyleListLeft()); // description
                this.createCell(getSession().getCodeLibelle(plainte.getType()), getStyleListLeft());// type
                this.createCell(getSession().getCodeLibelle(plainte.getMotif()), getStyleListLeft()); // motif
                this.createCell(plainte.getPeriodeDu(), getStyleListLeft()); // période du
                this.createCell(plainte.getPeriodeAu(), getStyleListLeft()); // période au
            }
        }
        initBilan();
        return currentSheet;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    public void setWithoutEndDate(String withoutEndDate) {
        this.withoutEndDate = withoutEndDate;
    }

}
