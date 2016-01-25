/**
 *
 */
package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptecourant.CAConcordanceCACG;
import globaz.osiris.db.comptecourant.CAConcordanceCACGManager;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author SEL
 * 
 */
public class CAListConcordanceCACGExcel extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0269GCA";

    private String dateDebut = null;
    private String dateFin = null;
    private String forIdExterne = null;
    private Boolean printOnlyDiff = null;

    private BProcess process = null;

    /**
     * @param session
     * @param filenameRoot
     * @param documentTitle
     */
    public CAListConcordanceCACGExcel(BSession session) {
        super(session, "ListConcordanceCACG", session.getLabel("COMPTECOURANT_LISTE_CONCORDANCE_CACG"));
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the forIdExterne
     */
    public String getForIdExterne() {
        return forIdExterne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.print.list.CAAbstractListExcel#getNumeroInforom()
     */
    @Override
    public String getNumeroInforom() {
        return CAListConcordanceCACGExcel.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the printOnlyDiff
     */
    public Boolean getPrintOnlyDiff() {
        return printOnlyDiff;
    }

    /**
     * @return the process
     */
    public BProcess getProcess() {
        return process;
    }

    /**
     * Affiche les critères de lancement de la liste
     */
    private void initCritere() {
        if (!JadeStringUtil.isBlankOrZero(getForIdExterne())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTECOURANT_LISTE_FILTRE_RUBRIQUE"), getStyleCritereTitle());
            this.createCell(getForIdExterne(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlankOrZero(getDateDebut())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTECOURANT_LISTE_FILTRE_DATE_DEBUT"), getStyleCritereTitle());
            this.createCell(getDateDebut(), getStyleCritere());
        }
        if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTECOURANT_LISTE_FILTRE_DATE_FIN"), getStyleCritereTitle());
            this.createCell(getDateFin(), getStyleCritere());
        }

        if ((getPrintOnlyDiff() != null) && getPrintOnlyDiff()) {
            createRow();
            this.createCell("", getStyleCritere());
            this.createCell(getSession().getLabel("COMPTECOURANT_LISTE_FILTRE_ONLY_DIFFERENCE"), getStyleCritereTitle());
        }
    }

    /**
     * Initialisation de la liste Excel
     */
    private void initListe() {
        ArrayList<ParamTitle> colTitles = new ArrayList<ParamTitle>();

        colTitles
                .add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_IDJOURNAL_CA"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_COMPTE_CA"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_SOLDE_CA"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_IDJOURNAL_EXTERNE_CA"),
                getStyleListTitleLeft()));
        colTitles
                .add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_IDJOURNAL_CG"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_LIBELLE_JOURNAL_CG"),
                getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_COMPTE_CG"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_SOLDE_CG"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTECOURANT_LISTE_DIFFERENCE"), getStyleListTitleLeft()));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListConcordanceCACGExcel.NUMERO_REFERENCE_INFOROM);

        int numCol = 0;

        // idJournal CA
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500);
        // idCompte CA
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_COMPTEANNEXE);
        // Solde CA
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        // idJournalExterne
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500);
        // idJournal CG
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500);
        // libelle CG
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        // idCompte CG
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_COMPTEANNEXE);
        // Solde CG
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        // Différence
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(CAConcordanceCACGManager manager, BTransaction transaction) throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        CAConcordanceCACG concordance = null;

        initListe();

        // récupération du numéro de la première ligne
        int numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;

        while (((concordance = (CAConcordanceCACG) manager.cursorReadNext(statement)) != null) && !concordance.isNew()
                && !getProcess().isAborted()) {
            if (concordance != null) {
                createRow();
                this.createCell(concordance.getIdJournal(), getStyleListLeft());
                this.createCell(concordance.getIdCompte(), getStyleListLeft());
                this.createCell(JadeStringUtil.parseDouble(concordance.getSoldeCA(), 0), false);
                this.createCell(concordance.getIdJournalExterne(), getStyleListLeft());
                this.createCell(concordance.getNumJournal(), getStyleListLeft());
                this.createCell(concordance.getLibelleCG(), getStyleListLeft());
                this.createCell(concordance.getCompteCG(), getStyleListLeft());
                this.createCell(JadeStringUtil.parseDouble(concordance.getSoldeCG(), 0), false);
                this.createCell(JadeStringUtil.parseDouble(concordance.getDifference(), 0), false);
            }
            getProcess().incProgressCounter();
        }

        createRow();
        this.createCell("Total :", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        createCellFormula("SUM(C" + numPremiereLigne + ":C" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        createCellFormula("SUM(H" + numPremiereLigne + ":H" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());
        createCellFormula("SUM(I" + numPremiereLigne + ":I" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());

        return currentSheet;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param forIdExterne
     *            the forIdExterne to set
     */
    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    /**
     * @param printOnlyDiff
     *            the printOnlyDiff to set
     */
    public void setPrintOnlyDiff(Boolean printOnlyDiff) {
        this.printOnlyDiff = printOnlyDiff;
    }

    /**
     * @param process
     *            the process to set
     */
    public void setProcess(BProcess process) {
        this.process = process;
    }

}
